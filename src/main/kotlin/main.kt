import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import dto.CommentWithAuthor
import dto.PostWithComments
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import ru.netology.nmedia_ind.dto.Author
import ru.netology.nmedia_ind.dto.Comment
import ru.netology.nmedia_ind.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
    .build()

private val gson = Gson()

private const val BASE_URL = "http://127.0.0.1:9999"

fun main() {
    with(CoroutineScope(EmptyCoroutineContext)) {
        launch {
            try {
                val postsWithComments = async {
                    getPosts().map { post ->
                        PostWithComments(
                            post,
                            getAuthor(post.authorId),
                            getComments(post.id).map { comment ->
                                CommentWithAuthor(
                                    comment,
                                    getAuthor(comment.authorId)
                                )
                            }
                        )
                    }
                }.await()
                println(postsWithComments)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Thread.sleep(1000L)
    }
}

suspend fun <T> makeCall(url: String, typeToken: TypeToken<T>): T =
    suspendCoroutine { continuation ->
        Request.Builder()
            .url(url)
            .build()
            .let(client::newCall)
            .enqueue(object : Callback {
                override fun onFailure(cal: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        continuation.resume(gson.fromJson(response.body?.string(), typeToken.type))
                    } catch (e: JsonParseException) {
                        continuation.resumeWithException(e)
                    }
                }
            })
    }

suspend fun getPosts(): List<Post> =
    makeCall("$BASE_URL/api/posts", object : TypeToken<List<Post>>() {})

suspend fun getComments(postId: Long): List<Comment> =
    makeCall("$BASE_URL/api/posts/$postId/comments/", object : TypeToken<List<Comment>>() {})

suspend fun getAuthor(authorId: Long): Author =
    makeCall("$BASE_URL/api/authors/$authorId", object : TypeToken<Author>() {})
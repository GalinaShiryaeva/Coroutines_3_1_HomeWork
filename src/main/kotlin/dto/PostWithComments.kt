package dto

import ru.netology.nmedia_ind.dto.Author
import ru.netology.nmedia_ind.dto.Comment
import ru.netology.nmedia_ind.dto.Post

data class PostWithComments(
    val post: Post,
    val author: Author,
    val comments: List<CommentWithAuthor>
)
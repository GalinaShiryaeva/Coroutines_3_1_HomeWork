package ru.netology.nmedia_ind.dto

data class Post(
    val id: Long,
    val authorId: Long,
    //val author: String,
    //val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val shared: Int = 0,
    val attachment: Attachment? = null
)
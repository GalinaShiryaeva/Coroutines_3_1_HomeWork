package ru.netology.nmedia_ind.dto

data class Attachment(
    val url: String,
    val description: String?,
    val type: AttachmentType
)

enum class AttachmentType {
    IMAGE,
    VIDEO
}
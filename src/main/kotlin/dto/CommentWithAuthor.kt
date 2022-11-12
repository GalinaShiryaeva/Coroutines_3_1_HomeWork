package dto

import ru.netology.nmedia_ind.dto.Author
import ru.netology.nmedia_ind.dto.Comment

data class CommentWithAuthor(
    val comment: Comment,
    val author: Author
)

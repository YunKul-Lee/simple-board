package com.jake.simpleboard.service.dto

import com.jake.simpleboard.domain.Comment
import com.jake.simpleboard.domain.Post

data class CommentCreateRequestDto(
    val content: String,
    val createdBy: String,
)

fun CommentCreateRequestDto.toEntity(post: Post) = Comment(
    content = content,
    createdBy = createdBy,
    post = post
)

package com.jake.simpleboard.service.dto

import com.jake.simpleboard.domain.Post

data class PostCreateRequestDto(
    val title: String,
    val content: String,
    val createdBy: String,
)

fun PostCreateRequestDto.toEntity() = Post(
    title = title,
    content = content,
    createdBy = createdBy,
)
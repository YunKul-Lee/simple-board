package com.jake.simpleboard.controller.dto

import com.jake.simpleboard.service.dto.CommentCreateRequestDto

data class CommentCreateRequest(
    val content: String,
    val createdBy: String,
)
fun CommentCreateRequest.toDto() = CommentCreateRequestDto(
    content = content,
    createdBy = createdBy
)

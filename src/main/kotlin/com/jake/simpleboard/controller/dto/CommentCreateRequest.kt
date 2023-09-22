package com.jake.simpleboard.controller.dto

data class CommentCreateRequest(
    val content: String,
    val createdBy: String,
)

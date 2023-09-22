package com.jake.simpleboard.controller.dto

data class CommentUpdateRequest(
    val content: String,
    val updatedBy: String,
)

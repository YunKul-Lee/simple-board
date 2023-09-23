package com.jake.simpleboard.controller.dto

import com.jake.simpleboard.service.dto.CommentUpdateRequestDto

data class CommentUpdateRequest(
    val content: String,
    val updatedBy: String,
) {
    fun toDto(): CommentUpdateRequestDto {
        return CommentUpdateRequestDto(
            content,
            updatedBy
        )
    }
}

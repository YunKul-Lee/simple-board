package com.jake.simpleboard.event.dto

data class LikeEvent(
    val postId: Long,
    val createdBy: String,
)

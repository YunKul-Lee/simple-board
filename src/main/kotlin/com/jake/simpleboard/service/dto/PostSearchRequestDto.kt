package com.jake.simpleboard.service.dto

data class PostSearchRequestDto(
    val title: String? = null,
    val createdBy: String? = null,
    val tag: String? = null,
)

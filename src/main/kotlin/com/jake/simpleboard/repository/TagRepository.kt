package com.jake.simpleboard.repository

import com.jake.simpleboard.domain.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Long> {

    fun findByPostId(postId: Long): List<Tag>
}

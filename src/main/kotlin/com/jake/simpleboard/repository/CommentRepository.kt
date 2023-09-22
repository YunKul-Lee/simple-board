package com.jake.simpleboard.repository

import com.jake.simpleboard.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long>

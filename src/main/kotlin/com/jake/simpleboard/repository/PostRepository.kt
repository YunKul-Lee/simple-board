package com.jake.simpleboard.repository

import com.jake.simpleboard.domain.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long>

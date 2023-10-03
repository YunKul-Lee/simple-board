package com.jake.simpleboard.repository

import com.jake.simpleboard.domain.Like
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository : JpaRepository<Like, Long>

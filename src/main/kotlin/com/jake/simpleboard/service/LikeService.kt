package com.jake.simpleboard.service

import com.jake.simpleboard.event.dto.LikeEvent
import com.jake.simpleboard.repository.LikeRepository
import com.jake.simpleboard.repository.PostRepository
import com.jake.simpleboard.util.RedisUtil
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LikeService(
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository,
    private val redisUtil: RedisUtil,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    fun createLike(postId: Long, createdBy: String) {
        applicationEventPublisher.publishEvent(LikeEvent(postId, createdBy))
    }

    fun countLike(postId: Long): Long {
        redisUtil.getCount(redisUtil.getLikeCountKey(postId))?.let { return it }

        with(likeRepository.countByPostId(postId)) {
            redisUtil.setDate(redisUtil.getLikeCountKey(postId), this)
            return this
        }
    }
}

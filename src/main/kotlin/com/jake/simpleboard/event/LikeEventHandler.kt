package com.jake.simpleboard.event

import com.jake.simpleboard.domain.Like
import com.jake.simpleboard.event.dto.LikeEvent
import com.jake.simpleboard.exception.PostNotFoundException
import com.jake.simpleboard.repository.LikeRepository
import com.jake.simpleboard.repository.PostRepository
import com.jake.simpleboard.util.RedisUtil
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener

@Service
class LikeEventHandler(
    private val postRepository: PostRepository,
    private val likeRepository: LikeRepository,
    private val redisUtil: RedisUtil,
) {

    @Async
    @TransactionalEventListener(LikeEvent::class)
    fun handle(event: LikeEvent) {
        val post = postRepository.findByIdOrNull(event.postId) ?: throw PostNotFoundException()
        redisUtil.increment(redisUtil.getLikeCountKey(event.postId))
        likeRepository.save(Like(post, event.createdBy))
    }
}

package com.jake.simpleboard.service

import com.jake.simpleboard.domain.Post
import com.jake.simpleboard.exception.PostNotDeletableException
import com.jake.simpleboard.exception.PostNotFoundException
import com.jake.simpleboard.repository.PostRepository
import com.jake.simpleboard.service.dto.PostCreateRequestDto
import com.jake.simpleboard.service.dto.PostUpdateRequestDto
import com.jake.simpleboard.service.dto.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
) {

    @Transactional
    fun createPost(requestDto: PostCreateRequestDto): Long {
        return postRepository.save(requestDto.toEntity()).id
    }

    @Transactional
    fun updatePost(id: Long, requestDto: PostUpdateRequestDto): Long {
        val post: Post = postRepository.findByIdOrNull(id) ?: throw PostNotFoundException()
        post.update(requestDto)
        return id
    }

    @Transactional
    fun deletePost(id: Long, deletedBy: String): Long {
        val post: Post = postRepository.findByIdOrNull(id) ?: throw PostNotFoundException()
        if (post.createdBy != deletedBy) throw PostNotDeletableException()
        postRepository.delete(post)

        return id
    }
}

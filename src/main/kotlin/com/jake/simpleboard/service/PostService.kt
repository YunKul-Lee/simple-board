package com.jake.simpleboard.service

import com.jake.simpleboard.domain.Post
import com.jake.simpleboard.exception.PostNotDeletableException
import com.jake.simpleboard.exception.PostNotFoundException
import com.jake.simpleboard.repository.PostRepository
import com.jake.simpleboard.service.dto.PostCreateRequestDto
import com.jake.simpleboard.service.dto.PostDetailResponseDto
import com.jake.simpleboard.service.dto.PostSearchRequestDto
import com.jake.simpleboard.service.dto.PostSummaryResponseDto
import com.jake.simpleboard.service.dto.PostUpdateRequestDto
import com.jake.simpleboard.service.dto.toDetailResponseDto
import com.jake.simpleboard.service.dto.toEntity
import com.jake.simpleboard.service.dto.toSummaryResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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

    fun getPost(id: Long): PostDetailResponseDto {
        return postRepository.findByIdOrNull(id)?.toDetailResponseDto() ?: throw PostNotFoundException()
    }

    fun findPageBy(pageRequest: Pageable, postSearchRequestDto: PostSearchRequestDto): Page<PostSummaryResponseDto> {
        return postRepository.findPageBy(pageRequest, postSearchRequestDto).toSummaryResponseDto()
    }
}

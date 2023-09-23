package com.jake.simpleboard.service

import com.jake.simpleboard.domain.Comment
import com.jake.simpleboard.domain.Post
import com.jake.simpleboard.exception.CommentNotDeletableException
import com.jake.simpleboard.exception.CommentNotFoundException
import com.jake.simpleboard.exception.PostNotFoundException
import com.jake.simpleboard.repository.CommentRepository
import com.jake.simpleboard.repository.PostRepository
import com.jake.simpleboard.service.dto.CommentCreateRequestDto
import com.jake.simpleboard.service.dto.CommentUpdateRequestDto
import com.jake.simpleboard.service.dto.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
) {
    @Transactional
    fun createComment(postId: Long, createRequestDto: CommentCreateRequestDto): Long {
        val post: Post = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()
        return commentRepository.save(createRequestDto.toEntity(post)).id
    }

    @Transactional
    fun updateComment(id: Long, updateRequestDto: CommentUpdateRequestDto): Long {
        val comment: Comment = commentRepository.findByIdOrNull(id) ?: throw CommentNotFoundException()
        comment.update(updateRequestDto)
        return comment.id
    }

    @Transactional
    fun deleteComment(id: Long, deletedBy: String): Long {
        val comment = commentRepository.findByIdOrNull(id) ?: throw CommentNotFoundException()
        if (comment.createdBy != deletedBy) throw CommentNotDeletableException()

        commentRepository.delete(comment)
        return id
    }
}

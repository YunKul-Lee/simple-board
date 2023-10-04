package com.jake.simpleboard.service

import com.jake.simpleboard.domain.Like
import com.jake.simpleboard.domain.Post
import com.jake.simpleboard.exception.PostNotFoundException
import com.jake.simpleboard.repository.LikeRepository
import com.jake.simpleboard.repository.PostRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class LikeServiceTest(
    private val likeService: LikeService,
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository,
) : BehaviorSpec({
    given("좋아요 생성시") {
        val saved = postRepository.save(Post("jake", "title", "content"))
        When("인풋이 정상적으로 들어오면") {
            val likeId = likeService.createLike(saved.id, "jake")
            then("좋아요가 정상적으로 생성됨을 확인한다.") {
                val like: Like? = likeRepository.findByIdOrNull(likeId)
                like shouldNotBe null
                like?.createdBy shouldBe "jake"
            }
        }
        When("게시글이 존재하지 않으면") {
            then("존재하지 않는 게시글 예외가 발생한다.") {
                shouldThrow<PostNotFoundException> { likeService.createLike(9999L, "jake") }
            }
        }
    }
})

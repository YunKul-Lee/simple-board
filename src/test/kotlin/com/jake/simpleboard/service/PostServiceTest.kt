package com.jake.simpleboard.service

import com.jake.simpleboard.domain.Comment
import com.jake.simpleboard.domain.Post
import com.jake.simpleboard.domain.Tag
import com.jake.simpleboard.exception.PostNotDeletableException
import com.jake.simpleboard.exception.PostNotFoundException
import com.jake.simpleboard.exception.PostNotUpdatableException
import com.jake.simpleboard.repository.CommentRepository
import com.jake.simpleboard.repository.PostRepository
import com.jake.simpleboard.repository.TagRepository
import com.jake.simpleboard.service.dto.PostCreateRequestDto
import com.jake.simpleboard.service.dto.PostDetailResponseDto
import com.jake.simpleboard.service.dto.PostSearchRequestDto
import com.jake.simpleboard.service.dto.PostUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.h2.command.dml.MergeUsing.When
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val tagRepository: TagRepository,
) : BehaviorSpec({
    beforeSpec {
        postRepository.saveAll(
            listOf(
                Post(title = "title1", content = "content1", createdBy = "jake1"  , tags = listOf("tag1", "tag2")  ),
                Post(title = "title12", content = "content2", createdBy = "jake1" , tags = listOf("tag1", "tag2")  ),
                Post(title = "title13", content = "content3", createdBy = "jake1" , tags = listOf("tag1", "tag2")  ),
                Post(title = "title14", content = "content4", createdBy = "jake1" , tags = listOf("tag1", "tag2")  ),
                Post(title = "title15", content = "content5", createdBy = "jake1" , tags = listOf("tag1", "tag2")  ),
                Post(title = "title6", content = "content6", createdBy = "jake2"  , tags = listOf("tag1", "tag5")  ),
                Post(title = "title7", content = "content7", createdBy = "jake2"  , tags = listOf("tag1", "tag5")  ),
                Post(title = "title8", content = "content8", createdBy = "jake2"  , tags = listOf("tag1", "tag5")  ),
                Post(title = "title9", content = "content9", createdBy = "jake2"  , tags = listOf("tag1", "tag5")  ),
                Post(title = "title10", content = "content10", createdBy = "jake2", tags = listOf("tag1", "tag5")  ),
            )
        )
    }

    given("게시글 생성시") {
        When("게시글 인풋이 정상적으로 들어오면") {
            val postId = postService.createPost(
                PostCreateRequestDto(
                    title = "제목",
                    content = "내용",
                    createdBy = "jake"
                )
            )
            then("게시글이 정상적으로 생성됨을 확인한다.") {
                postId shouldBeGreaterThan 0L
                val post = postRepository.findByIdOrNull(postId)
                post shouldNotBe null
                post?.title shouldBe "제목"
                post?.content shouldBe "내용"
                post?.createdBy shouldBe "jake"
            }
        }
        When("태그가 추가되면") {
            val postId = postService.createPost(
                PostCreateRequestDto(
                    title = "제목",
                    content = "내용",
                    createdBy = "jake",
                    tags = listOf("tag1", "tag2")
                )
            )
            then("태그가 정상적으로 추가됨을 확인한다.") {
                val tags = tagRepository.findByPostId(postId)
                tags.size shouldBe 2
                tags[0].name shouldBe "tag1"
                tags[1].name shouldBe "tag2"
            }
        }
    }

    given("게시글 수정시") {
        val saved = postRepository.save(
            Post(title = "title", content = "content", createdBy = "jake", tags = listOf("tag1", "tag2"))
        )
        When("정상 수정시") {
            val updatedId = postService.updatePost(
                saved.id,
                PostUpdateRequestDto(
                    title = "update title",
                    content = "update content",
                    updatedBy = "jake"
                )
            )

            then("게시글이 정상적으로 수정됨을 확인한다.") {
                saved.id shouldBe updatedId
                val updated = postRepository.findByIdOrNull(updatedId)
                updated shouldNotBe null
                updated?.title shouldBe "update title"
                updated?.content shouldBe "update content"
                updated?.updatedBy shouldBe "jake"
            }
        }
        When("게시글이 없을 때") {

            then("게시글을 찾을 수 없다는 예외가 발생한다.") {
                shouldThrow<PostNotFoundException> {
                    postService.updatePost(
                        9999L,
                        PostUpdateRequestDto(
                            title = "update title",
                            content = "update content",
                            updatedBy = "update jake"
                        )
                    )
                }
            }
        }
        When("작성자가 동일하지 않으면") {
            then("수정할 수 없는 게시물 입니다. 예외가 발생한다.") {
                shouldThrow<PostNotUpdatableException> {
                    postService.updatePost(
                        1L,
                        PostUpdateRequestDto(
                            title = "update title",
                            content = "update content",
                            updatedBy = "update jake"
                        )
                    )
                }
            }
        }
        When("태그가 수정되었을 때") {
            val updatedId = postService.updatePost(
                saved.id,
                PostUpdateRequestDto(
                    title = "update title",
                    content = "update content",
                    updatedBy = "jake",
                    tags = listOf("tag1", "tag2", "tag3")
                )
            )
            then("정상적으로 수정됨을 확인한다.") {
                val tags = tagRepository.findByPostId(updatedId)
                tags.size shouldBe 3
                tags[2].name shouldBe "tag3"
            }
            then("태그 순서가 변경되었을때 정상적으로 변경됨을 확인한다.") {
                postService.updatePost(
                    saved.id,
                    PostUpdateRequestDto(
                        title = "update title",
                        content = "update content",
                        updatedBy = "jake",
                        tags = listOf("tag3", "tag2", "tag1")
                    )
                )

                val tags = tagRepository.findByPostId(updatedId)
                tags.size shouldBe 3
                tags[2].name shouldBe "tag1"
            }
        }
    }

    given("게시물 삭제 시") {
        val saved = postRepository.save(Post(title = "title", content = "content", createdBy = "jake"))

        When("정상 삭제시") {
            val postId = postService.deletePost(saved.id, "jake")
            then("게시글이 정상적으로 삭제됨을 확인한다.") {
                postId shouldBe saved.id
                postRepository.findByIdOrNull(postId) shouldBe null
            }
        }
        When("작성자가 동일하지 않으면") {
            val saved2 = postRepository.save(Post(title = "title", content = "content", createdBy = "jake"))

            then("삭제할 수 없는 게시물입니다. 예외가 발생한다.") {
                shouldThrow<PostNotDeletableException> {
                    postService.deletePost(saved2.id, "jake2")
                }
            }
        }
    }

    given("게시글 상세조회시") {
        val saved = postRepository.save(Post(title = "title", content = "content", createdBy = "jake"))
        tagRepository.saveAll(
            listOf(
                Tag(name = "tag1", post = saved, createdBy = saved.createdBy),
                Tag(name = "tag2", post = saved, createdBy = saved.createdBy),
                Tag(name = "tag3", post = saved, createdBy = saved.createdBy),
            )
        )
        When("정상 조회시") {
            val post = postService.getPost(saved.id)
            then("게시글의 내용이 정상적으로 반환됨을 확인한다.") {
                post.id shouldBe saved.id
                post.title shouldBe "title"
                post.content shouldBe "content"
                post.createdBy shouldBe "jake"
            }
            then("태그가 정상적으로 조회됨을 확인한다.") {
                post.tags.size shouldBe 3
                post.tags[0] shouldBe "tag1"
                post.tags[1] shouldBe "tag2"
                post.tags[2] shouldBe "tag3"
            }
        }

        When("게시글이 없을 때") {
            then("게시글을 찾을 수 없다라는 예외가 발생한다.") {
                shouldThrow<PostNotFoundException> {
                    postService.getPost(9999L)
                }
            }
        }

        When("댓글 추가시") {
            commentRepository.save(Comment(content = "댓글 내용1", post = saved, createdBy = "댓글 작성자"))
            commentRepository.save(Comment(content = "댓글 내용2", post = saved, createdBy = "댓글 작성자"))
            commentRepository.save(Comment(content = "댓글 내용3", post = saved, createdBy = "댓글 작성자"))

            val post: PostDetailResponseDto = postService.getPost(saved.id)
            then("댓글이 함께 조회됨을 확인한다.") {
                post.comments.size shouldBe 3
                post.comments[0].content shouldBe "댓글 내용1"
                post.comments[1].content shouldBe "댓글 내용2"
                post.comments[2].content shouldBe "댓글 내용3"

                post.comments[0].createdBy shouldBe "댓글 작성자"
                post.comments[1].createdBy shouldBe "댓글 작성자"
                post.comments[2].createdBy shouldBe "댓글 작성자"
            }
        }
    }

    given("게시글 목록조회시") {
        When("정상 조회시") {
            val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto())
            then("게시글 페이지가 반환된다.") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title"
                postPage.content[0].createdBy shouldContain "jake"
            }
        }
        When("타이틀로 검색") {
            then("타이틀에 해당하는 게실글이 반환된다.") {
                val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto(title = "title1"))

                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title1"
                postPage.content[0].createdBy shouldContain "jake"
            }
        }
        When("작성자로 검색") {
            val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto(createdBy = "jake1"))
            then("작성자에 해당하는 게시글이 반환된다.") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title"
                postPage.content[0].createdBy shouldBe "jake1"
            }
            then("첫번째 태그가 함께 조회됨을 확인한다.") {
                postPage.content.forEach {
                    it.firstTag shouldBe "tag1"
                }
            }
        }
        When("태그로 검색") {
            val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto(tag = "tag5"))
            then("태그에 해당하는 게시글이 반환된다.") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldBe "title10"
                postPage.content[1].title shouldBe "title9"
                postPage.content[2].title shouldBe "title8"
                postPage.content[3].title shouldBe "title7"
                postPage.content[4].title shouldBe "title6"
            }
        }

    }
})

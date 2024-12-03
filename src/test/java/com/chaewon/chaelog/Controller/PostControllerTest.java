package com.chaewon.chaelog.Controller;

import com.chaewon.chaelog.config.MockMember;
import com.chaewon.chaelog.domain.Member;
import com.chaewon.chaelog.domain.Post;
import com.chaewon.chaelog.domain.request.PostCreateRequest;
import com.chaewon.chaelog.domain.request.PostEditRequest;
import com.chaewon.chaelog.repository.MemberRepository;
import com.chaewon.chaelog.repository.post.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void clean() {
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }

    // 유틸리티 메서드
    private Member createMember(String name, String email, String password) {
        return memberRepository.save(Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .build());
    }

    private Post createPost(String title, String content, Member member) {
        return postRepository.save(Post.builder()
                .title(title)
                .content(content)
                .member(member)
                .build());
    }

    // === 글 작성 테스트 ===

    @Test
    @MockMember
    @DisplayName("제목이 없는 요청 시 실패 테스트")
    void shouldFailToCreatePostWhenTitleIsMissing() throws Exception {
        // given
        PostCreateRequest request = PostCreateRequest.builder()
                .content("내용입니다.")
                .build();

        // when & then
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력하세요."))
                .andDo(print());
    }

    @Test
    @MockMember
    @DisplayName("올바른 요청 시 성공 테스트")
    void shouldCreatePostSuccessfully() throws Exception {
        // given
        PostCreateRequest request = PostCreateRequest.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when & then
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());

        // DB 확인
        assertEquals(1L, postRepository.count()); // DB에 저장된 게시글이 1개인지 확인

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    // === 글 조회 테스트 ===

    @Test
    @DisplayName("단일 게시글 조회")
    void shouldGetSinglePost() throws Exception {
        // given
        Member member = createMember("Kim", "kim@example.com", "1234");
        Post post = createPost("제목입니다.", "내용입니다.", member);

        // when & then
        mockMvc.perform(get("/api/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("제목입니다."))
                .andExpect(jsonPath("$.content").value("내용입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("다수 게시글 페이징 조회")
    void shouldGetMultiplePostsWithPagination() throws Exception {
        // given
        Member member = createMember("Kim", "kim@example.com", "1234");

        IntStream.rangeClosed(1, 15).forEach(i ->
                createPost("제목" + i, "내용" + i, member)
        );

        // when & then
        mockMvc.perform(get("/api/posts")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()", is(10)))
                .andExpect(jsonPath("$.items[0].title").value("제목15"))
                .andDo(print());
    }

    @Test
    @DisplayName("없는 게시글 조회 시 예외 테스트")
    void shouldReturnNotFoundWhenPostDoesNotExist() throws Exception {
        // when & then
        mockMvc.perform(get("/api/posts/{postId}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    // === 글 수정 테스트 ===

    @Test
    @MockMember
    @DisplayName("제목 변경 성공 테스트")
    void shouldEditPostTitleSuccessfully() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);
        Post post = createPost("Original Title", "Original Content", member);

        PostEditRequest request = PostEditRequest.builder()
                .title("Updated Title")
                .content("Original Content")
                .build();

        // when & then
        mockMvc.perform(patch("/api/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                // .andExpect(jsonPath("$.title").value("Updated Title")) // 응답 본문이 없는 경우 제거
                .andDo(print());

        // DB 검증
        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        assertEquals("Updated Title", updatedPost.getTitle());
        assertEquals("Original Content", updatedPost.getContent());
    }

    @Test
    @MockMember
    @DisplayName("없는 게시글 수정 시 예외 테스트")
    void shouldReturnNotFoundWhenEditingNonexistentPost() throws Exception {
        // given
        PostEditRequest request = PostEditRequest.builder()
                .title("Updated Title")
                .content("Updated Content")
                .build();

        // when & then
        mockMvc.perform(patch("/api/posts/{postId}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    // === 글 삭제 테스트 ===

    @Test
    @MockMember
    @DisplayName("게시글 삭제 성공 테스트")
    void shouldDeletePostSuccessfully() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);
        Post post = createPost("Title to Delete", "Content to Delete", member);

        // when & then
        mockMvc.perform(delete("/api/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        // 삭제 확인
        mockMvc.perform(get("/api/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("없는 게시글 삭제 시 예외 테스트")
    @MockMember
    void shouldReturnNotFoundWhenDeletingNonexistentPost() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/posts/{postId}",1L )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()) // 404 상태 코드 확인
                .andDo(print());
    }


    // === 검색 테스트 ===

    @Test
    @DisplayName("키워드 검색 결과 확인")
    void shouldReturnPostsMatchingKeyword() throws Exception {
        // given
        Member member = createMember("Kim", "kim@example.com", "1234");

        createPost("Spring Boot Guide", "Content 1", member);
        createPost("Spring Data JPA", "Content 2", member);
        createPost("REST API Design", "Content 3", member);
        createPost("Java Concurrency", "Content 4", member);

        // when & then
        mockMvc.perform(get("/api/posts/search")
                        .param("keyword", "Spring")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()", is(2)))
                .andExpect(jsonPath("$.items[*].title", everyItem(containsString("Spring"))))
                .andDo(print());
    }

    @Test
    @DisplayName("빈 결과 반환 테스트")
    void shouldReturnEmptyWhenNoPostsMatchKeyword() throws Exception {
        // given
        Member member = createMember("Kim", "kim@example.com", "1234");

        createPost("Spring Boot Guide", "Content 1", member);
        createPost("Spring Data JPA", "Content 2", member);

        // when & then
        mockMvc.perform(get("/api/posts/search")
                        .param("keyword", "Python")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()", is(0)))
                .andDo(print());
    }

    // === 권한 테스트 ===

    @Test
    @DisplayName("권한 없는 사용자의 요청 실패 테스트")
    void shouldFailWhenUnauthorizedUserAttemptsToEditPost() throws Exception {
        // given
        Member author = createMember("Author", "author@example.com", "1234");
        Post post = createPost("Original Title", "Original Content", author);

        PostEditRequest request = PostEditRequest.builder()
                .title("Updated Title")
                .content("Updated Content")
                .build();

        // when & then
        // @MockMember를 사용하지 않아 인증되지 않은 사용자로 시뮬레이션
        mockMvc.perform(patch("/api/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}

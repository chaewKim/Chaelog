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
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
    @Test
    @MockMember
    @DisplayName("글 작성 요청시 title값은 필수다.")
    void test2() throws Exception {
        // given
        PostCreateRequest request = PostCreateRequest.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/api/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력하세요."))
                .andDo(print());
    }

    @Test
    @MockMember
    @DisplayName("글 작성")
    void test3() throws Exception {
        // given
        PostCreateRequest request = PostCreateRequest.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/api/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        // given
        Member member = Member.builder()
                .name("Kim")
                .email("chaewon0430@gmail.com")
                .password("1234")
                .build();
        memberRepository.save(member);

        Post post = Post.builder()
                .title("123456789012345")
                .content("내용")
                .member(member)
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(get("/api/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("123456789012345"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        // given
        Member member = Member.builder()
                .name("Kim")
                .email("chaewon0430@gmail.com")
                .password("1234")
                .build();
        memberRepository.save(member);

        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("제목" + i)
                        .content("내용" + i)
                        .member(member)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/api/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()", is(10)))
                .andExpect(jsonPath("$.items[0].title").value("제목19"))
                .andExpect(jsonPath("$.items[0].content").value("내용19"))
                .andDo(print());
    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    void test6() throws Exception {
        // given
        Member member = Member.builder()
                .name("Kim")
                .email("chaewon0430@gmail.com")
                .password("1234")
                .build();
        memberRepository.save(member);

        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("제목" + i)
                        .content("내용" + i)
                        .member(member)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/api/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(4)))
//                .andExpect(jsonPath("$[0].title").value("제목19"))
//                .andExpect(jsonPath("$[0].content").value("내용19"))
                .andDo(print());
    }

    @Test
    @MockMember
    @DisplayName("글 제목 수정")
    void test7() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();
        postRepository.save(post);

        PostEditRequest postEdit = PostEditRequest.builder()
                .title("제목 수정")
                .content("내용")
                .build();

        // expected
        mockMvc.perform(patch("/api/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @MockMember
    @DisplayName("게시글 삭제")
    void test8() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);


        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        postRepository.save(post);

        // expected
        mockMvc.perform(delete("/api/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @MockMember
    @DisplayName("존재하지 않는 게시글 수정")
    void test10() throws Exception {
        // given
        PostEditRequest postEdit = PostEditRequest.builder()
                .title("제목2")
                .content("내용")
                .build();

        // expected
        mockMvc.perform(patch("/api/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @MockMember
    @DisplayName("키워드로 게시글 검색")
    void testSearchPostsByKeyword() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);

        List<Post> requestPosts = IntStream.range(0, 15)
                .mapToObj(i -> Post.builder()
                        .title("키워드 포함 제목" + i)
                        .content("내용" + i)
                        .member(member)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // when & then
        mockMvc.perform(get("/api/posts/search") // 경로 수정
                        .param("keyword", "키워드") // 검색 키워드
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()", is(10))) // 페이지 크기 확인
                .andExpect(jsonPath("$.items[0].title", containsString("키워드")))
                .andDo(print());
    }
    @Test
    @MockMember
    @DisplayName("게시글 수정")
    void testEditPostByAuthor() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);

        Post post = Post.builder()
                .title("Original Title")
                .content("Original Content")
                .member(member)
                .build();
        postRepository.save(post);

        PostEditRequest request = PostEditRequest.builder()
                .title("Updated Title")
                .content("Updated Content")
                .build();

        // when & then
        mockMvc.perform(patch("/api/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }



    @Test
    @MockMember
    @DisplayName("게시글 삭제")
    void testDeletePostByAuthor() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);

        Post post = Post.builder()
                .title("Original Title")
                .content("Original Content")
                .member(member)
                .build();
        postRepository.save(post);
        // when & then
        mockMvc.perform(delete("/api/posts/{postId}", post.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }


}

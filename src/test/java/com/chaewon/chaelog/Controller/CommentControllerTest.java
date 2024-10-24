package com.chaewon.chaelog.Controller;

import com.chaewon.chaelog.config.MockMember;
import com.chaewon.chaelog.domain.Comment;
import com.chaewon.chaelog.domain.Member;
import com.chaewon.chaelog.domain.Post;
import com.chaewon.chaelog.domain.request.CommentCreateRequest;
import com.chaewon.chaelog.domain.request.CommentDeleteRequest;
import com.chaewon.chaelog.repository.MemberRepository;
import com.chaewon.chaelog.repository.comment.CommentRepository;
import com.chaewon.chaelog.repository.post.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @AfterEach
    void clean() {
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 작성")
    void test1() throws Exception {
        //given
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

        CommentCreateRequest request = CommentCreateRequest.builder()
                .author("Park")
                .password("1234567")
                .content("댓글입니다123456789.")
                .build();
        String json = objectMapper.writeValueAsString(request);//json으로 변경

        //expected
        mockMvc.perform(post("/posts/{postId}/comments", post.getId())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(1L, commentRepository.count());

        Comment savedComment = commentRepository.findAll().get(0);
        assertEquals("Park", savedComment.getAuthor());
        assertNotEquals("1234567", savedComment.getPassword());
        assertTrue(passwordEncoder.matches("1234567", savedComment.getPassword()));
        assertEquals("댓글입니다123456789.", savedComment.getContent());
    }

    @Test
    @DisplayName("댓글 삭제")
    public void test2() throws Exception {
        //given
        Member member = Member.builder()
                .name("Kim")
                .email("chaewon0430@gmail.com")
                .password("1234")
                .build();
        memberRepository.save(member);

        Post post = Post.builder()
                .title("제목 123456789012345")
                .content("내용")
                .member(member)
                .build();
        postRepository.save(post);

        Comment comment = Comment.builder()
                .author("Choi")
                .password("1234567")
                .content("12345678910 내용ㅇㅇㅇ")
                .build();
        comment.setPost(post);
        commentRepository.save(comment);
        CommentDeleteRequest request = new CommentDeleteRequest("1234567");
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/comment/{commentId}/delete", comment.getId())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());


    }
}

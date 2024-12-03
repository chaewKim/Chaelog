package com.chaewon.chaelog.Controller;

import com.chaewon.chaelog.config.MockMember;
import com.chaewon.chaelog.domain.Member;
import com.chaewon.chaelog.domain.Post;
import com.chaewon.chaelog.domain.request.PostCreateRequest;
import com.chaewon.chaelog.domain.request.PostEditRequest;
import com.chaewon.chaelog.domain.request.SignupRequest;
import com.chaewon.chaelog.repository.MemberRepository;
import com.chaewon.chaelog.repository.post.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.chaelog.com", uriPort = 443)
@AutoConfigureMockMvc
@Transactional
public class PostControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("글 단건 조회")
    void testGetPost() throws Exception {
        // given
        Member member = Member.builder()
                .name("Author")
                .email("author@example.com")
                .password("1234")
                .build();
        memberRepository.save(member);

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(get("/api/posts/{postId}", post.getId())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-inquiry",
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("authorName").description("작성자 이름")
                        )
                ));
    }

    @Test
    @MockMember
    @DisplayName("글 등록")
    void testCreatePost() throws Exception {
        // given

        PostCreateRequest request = PostCreateRequest.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/api/posts")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-create",
                        requestFields(
                                fieldWithPath("title").description("제목")
                                        .attributes(key("constraint").value("좋은 제목 입력하세요.")),
                                fieldWithPath("content").description("내용").optional()
                        )
                ));
    }

    @Test
    @MockMember
    @DisplayName("글 수정 - 작성자만 가능")
    void testUpdatePostByAuthor() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);


        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();
        postRepository.save(post);

        PostEditRequest postEdit = PostEditRequest.builder()
                .title("수정된 제목")
                .content("수정된 내용")
                .build();

        String json = objectMapper.writeValueAsString(postEdit);

        // expected
        mockMvc.perform(patch("/api/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document("post-update",
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("수정할 제목"),
                                fieldWithPath("content").description("수정할 내용")
                        )
                ));
    }

    @Test
    @MockMember
    @DisplayName("게시글 삭제")
    void testDeletePostByAuthor() throws Exception {
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
                .andDo(document("post-delete",
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        )
                ));
    }
}

package com.chaewon.chaelog.Controller.DocTest;

import com.chaewon.chaelog.domain.Member;
import com.chaewon.chaelog.domain.Post;
import com.chaewon.chaelog.domain.request.CommentCreateRequest;
import com.chaewon.chaelog.domain.request.CommentDeleteRequest;
import com.chaewon.chaelog.exception.InvalidPassword;
import com.chaewon.chaelog.repository.MemberRepository;
import com.chaewon.chaelog.repository.post.PostRepository;
import com.chaewon.chaelog.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.chaelog.com", uriPort = 443)
@AutoConfigureMockMvc
@Transactional
public class CommentControllerDocTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommentService commentService;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }
    @AfterEach
    void clean() {
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }
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
    /*
     * COMMENT
     * */
    @Test
    @DisplayName("댓글 작성 성공")
    void testWriteComment_Success() throws Exception {
        // Given
        CommentCreateRequest request = new CommentCreateRequest();
        request.setAuthor("Kim");
        request.setContent("Test Content");
        request.setPassword("123456");

        mockMvc.perform(post("/api/posts/{postId}/comments", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("comments-create",
                        pathParameters(
                                parameterWithName("postId").description("댓글이 달릴 게시글의 ID")
                        ),
                        requestFields(
                                fieldWithPath("author").description("댓글 작성자"),
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("password").description("댓글 비밀번호")
                        )
                ));
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void testDeleteComment_Success() throws Exception {
        // Given
        CommentDeleteRequest request = new CommentDeleteRequest(1L, "TestPassword");

        mockMvc.perform(delete("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("comments-delete",
                        requestFields(
                                fieldWithPath("id").description("삭제하려는 댓글의 ID"),
                                fieldWithPath("password").description("댓글 삭제를 위한 비밀번호")
                        )
                ));
    }
    @Test
    @DisplayName("댓글 삭제 실패 - 잘못된 비밀번호")
    void testDeleteComment_InvalidPassword() throws Exception {
        // Given
        CommentDeleteRequest request = new CommentDeleteRequest(1L, "InvalidPassword");

        doThrow(new InvalidPassword())
                .when(commentService)
                .delete(ArgumentMatchers.any(CommentDeleteRequest.class));

        // When & Then
        mockMvc.perform(delete("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호가 올바르지 않습니다."))
                .andDo(print())
                .andDo(document("comment-delete-failure",
                        requestFields(
                                fieldWithPath("id").description("삭제할 댓글 ID"),
                                fieldWithPath("password").description("잘못된 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("validation").description("검증 오류에 대한 추가 정보")
                        )
                ));

        verify(commentService).delete(ArgumentMatchers.any(CommentDeleteRequest.class));
    }
}

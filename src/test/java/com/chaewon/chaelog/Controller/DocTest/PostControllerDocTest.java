package com.chaewon.chaelog.Controller.DocTest;

import com.chaewon.chaelog.config.MockMember;
import com.chaewon.chaelog.domain.Member;
import com.chaewon.chaelog.domain.Post;
import com.chaewon.chaelog.domain.request.PostCreateRequest;
import com.chaewon.chaelog.domain.request.PostEditRequest;
import com.chaewon.chaelog.repository.MemberRepository;
import com.chaewon.chaelog.repository.post.PostRepository;
import com.chaewon.chaelog.service.CommentService;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.chaelog.com", uriPort = 443)
@AutoConfigureMockMvc
public class PostControllerDocTest {

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
    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
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
* POST
* */
    @Test
    @MockMember
    @DisplayName("게시글 작성")
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
                .andDo(document("posts-create",
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 단건 조회")
    void shouldGetSinglePost() throws Exception {
        Member member = createMember("Kim", "kim@example.com", "1234");
        Post post = createPost("제목입니다.", "내용입니다.", member);

        mockMvc.perform(get("/api/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andDo(print())
                .andDo(document("post-get",
                        pathParameters(
                                parameterWithName("postId").description("조회할 게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("authorName").description("작성자"),
                                fieldWithPath("content").description("게시글 내용")
                        )
                ));
    }
    @Test
    @MockMember
    @DisplayName("게시글 수정")
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
                .andDo(print())
                .andDo(document("post-edit",
                        pathParameters(
                                parameterWithName("postId").description("수정할 게시글의 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("수정할 게시글의 제목"),
                                fieldWithPath("content").description("수정할 게시글의 내용")
                        )
                ));

        // DB 검증
        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        assertEquals("Updated Title", updatedPost.getTitle());
        assertEquals("Original Content", updatedPost.getContent());
    }
    @Test
    @DisplayName("없는 게시글 조회 시 예외 테스트")
    void shouldReturnNotFoundWhenPostDoesNotExist() throws Exception {
        // when & then
        mockMvc.perform(get("/api/posts/{postId}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andDo(document("post-get-not-found",
                        pathParameters(
                                parameterWithName("postId").description("존재하지 않는 게시글의 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("validation").description("검증 오류에 대한 추가 정보")
                        )
                ));
    }
    @Test
    @MockMember
    @DisplayName("게시글 삭제")
    void shouldDeletePostSuccessfully() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);
        Post post = createPost("Title to Delete", "Content to Delete", member);

        // when & then
        mockMvc.perform(delete("/api/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-delete",
                        pathParameters(
                                parameterWithName("postId").description("삭제할 게시글의 ID")
                        )
                ));

        // 삭제 확인
        mockMvc.perform(get("/api/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("게시글 목록 조회")
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
                .andExpect((ResultMatcher) jsonPath("$.items.length()", is(10)))
                .andExpect(jsonPath("$.items[0].title").value("제목15"))
                .andDo(print())
                .andDo(document("post-get-pagination",
                        responseFields(
                                fieldWithPath("items[]").description("게시글 목록"),
                                fieldWithPath("items[].id").description("게시글 ID"),
                                fieldWithPath("items[].authorName").description("작성자 이름"),
                                fieldWithPath("items[].title").description("게시글 제목"),
                                fieldWithPath("items[].content").description("게시글 내용"),
                                fieldWithPath("page").description("현재 페이지 번호"),
                                fieldWithPath("size").description("페이지 크기"),
                                fieldWithPath("totalCount").description("전체 게시글 수")
                        )
                ));
    }
    @Test
    @DisplayName("키워드 검색 결과 확인")
    void documentKeywordSearch() throws Exception {
        // given
        Member member = createMember("Kim", "kim@example.com", "1234");

        createPost("Spring Boot Guide", "Content 1", member);
        createPost("Spring Data JPA", "Content 2", member);
        createPost("REST API Design", "Content 3", member);
        createPost("Java Concurrency", "Content 4", member);

        // when & then
        // when & then
        mockMvc.perform(get("/api/posts/search")
                        .param("keyword", "Spring")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("posts-search",
                        queryParameters(
                                parameterWithName("keyword").description("검색 키워드"),
                                parameterWithName("page").description("요청 페이지 번호 (0부터 시작)"),
                                parameterWithName("size").description("한 페이지당 게시글 개수")
                        ),
                        responseFields(
                                fieldWithPath("page").description("현재 페이지 번호"),
                                fieldWithPath("size").description("요청한 페이지 크기"),
                                fieldWithPath("items").description("검색된 게시글 목록"),
                                fieldWithPath("totalCount").description("검색 게시글 수"),
                                fieldWithPath("items[].id").description("게시글 ID"),
                                fieldWithPath("items[].title").description("게시글 제목").optional(),
                                fieldWithPath("items[].content").description("게시글 내용").optional(),
                                fieldWithPath("items[].authorName").description("게시글 작성자 이름").optional()
                        )
                ));
    }


}

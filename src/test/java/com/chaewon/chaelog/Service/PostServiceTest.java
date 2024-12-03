package com.chaewon.chaelog.Service;

import com.chaewon.chaelog.config.MockMember;
import com.chaewon.chaelog.domain.Member;
import com.chaewon.chaelog.domain.Post;
import com.chaewon.chaelog.domain.request.PostCreateRequest;
import com.chaewon.chaelog.domain.request.PostEditRequest;
import com.chaewon.chaelog.domain.request.PostSearchRequest;
import com.chaewon.chaelog.domain.response.PagingResponse;
import com.chaewon.chaelog.domain.response.PostResponse;
import com.chaewon.chaelog.exception.PostNotFound;
import com.chaewon.chaelog.exception.Unauthorized;
import com.chaewon.chaelog.repository.MemberRepository;
import com.chaewon.chaelog.repository.post.PostRepository;
import com.chaewon.chaelog.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PostServiceTest {
    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void testCreatePost() {
        // given
        Member member = Member.builder()
                .name("Kim")
                .email("chaewon0430@gmail.com")
                .password("1234")
                .build();
        memberRepository.save(member);

        PostCreateRequest postCreateRequest = PostCreateRequest.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(member.getId(), postCreateRequest);

        // then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @Transactional
    @DisplayName("작성자 이름이 제대로 출력되는지 테스트")
    void testAuthorName() {
        // given
        Member member = Member.builder()
                .name("chaewon")
                .email("test@example.com")
                .password("1234")
                .build();
        memberRepository.save(member);

        Post post = Post.builder()
                .title("Test Title")
                .content("Test Content")
                .member(member)
                .build();
        postRepository.save(post);

        // when
        PostResponse response = postService.get(post.getId());

        // then
        assertEquals("chaewon", response.getAuthorName());
    }

    @Test
    @DisplayName("글 1개 조회")
    void testGetSinglePost() {
        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        // when
        PostResponse response = postService.get(post.getId());

        // then
        assertNotNull(response);
        assertEquals("제목", response.getTitle());
        assertEquals("내용", response.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void testGetMultiplePosts() {
        // given
        List<Post> posts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("제목" + i)
                        .content("내용" + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(posts);

        PostSearchRequest request = PostSearchRequest.builder()
                .page(1)
                .build();

        // when
        PagingResponse<PostResponse> response = postService.getList(request);

        // then
        assertEquals(10, response.getSize());
        assertEquals("제목19", response.getItems().get(0).getTitle());
    }

    @Test
    @DisplayName("글 수정")
    void testEditPost() {
        // given
        Member member = Member.builder()
                .name("Kim")
                .email("chaewon0430@gmail.com")
                .password("1234")
                .build();
        memberRepository.save(member);

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();
        postRepository.save(post);

        PostEditRequest request = PostEditRequest.builder()
                .title("수정된 제목")
                .content("수정된 내용")
                .build();

        // when
        postService.edit(post.getId(), request, member.getId());

        // then
        Post editedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        assertEquals("수정된 제목", editedPost.getTitle());
        assertEquals("수정된 내용", editedPost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void testDeletePost() {
        // given
        Member member = Member.builder()
                .name("Kim")
                .email("chaewon0430@gmail.com")
                .password("1234")
                .build();
        memberRepository.save(member);

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();
        postRepository.save(post);

        // when
        postService.delete(post.getId(), member.getId());

        // then
        assertEquals(0, postRepository.count());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void testGetNonExistentPost() {
        // expected
        assertThrows(PostNotFound.class, () -> postService.get(999L));
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void testEditNonExistentPost() {
        // given
        Member member = Member.builder()
                .name("Kim")
                .email("chaewon0430@gmail.com")
                .password("1234")
                .build();
        memberRepository.save(member);

        PostEditRequest request = PostEditRequest.builder()
                .title("제목")
                .content("내용")
                .build();

        // expected
        assertThrows(PostNotFound.class, () -> postService.edit(999L, request, member.getId()));
    }

    @Test
    @DisplayName("존재하지 않는 게시글 삭제")
    void testDeleteNonExistentPost() {
        // expected
        assertThrows(PostNotFound.class, () -> postService.delete(999L,1L));
    }

    @Test
    @DisplayName("키워드로 게시글 검색")
    void testSearchPostsByKeyword() {
        // given
        Member member = Member.builder()
                .name("Kim")
                .email("chaewon0430@gmail.com")
                .password("1234")
                .build();
        memberRepository.save(member);

        List<Post> requestPosts = IntStream.range(0, 15)
                .mapToObj(i -> Post.builder()
                        .title("제목" + i + " 키워드")
                        .content("내용" + i)
                        .member(member)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        PostSearchRequest searchRequest = PostSearchRequest.builder()
                .page(1)
                .size(10)
                .keyword("키워드") // 키워드 설정
                .build();

        // when
        PagingResponse<PostResponse> response = postService.getList(searchRequest);

        // then
        assertNotNull(response);
        assertEquals(10, response.getSize());
        assertTrue(response.getItems().stream().allMatch(post -> post.getTitle().contains("키워드")));
    }
    @Test
    @DisplayName("게시글 수정 - 작성자만 가능")
    void testEditPostByAuthorOnly_Service() {
        // given
        Member author = Member.builder()
                .name("Author")
                .email("author@example.com")
                .password("password")
                .build();
        memberRepository.save(author);

        Member otherUser = Member.builder()
                .name("OtherUser")
                .email("other@example.com")
                .password("password")
                .build();
        memberRepository.save(otherUser);

        Post post = Post.builder()
                .title("Original Title")
                .content("Original Content")
                .member(author)
                .build();
        postRepository.save(post);

        PostEditRequest editRequest = PostEditRequest.builder()
                .title("Updated Title")
                .content("Updated Content")
                .build();

        // expected - 작성자가 수정 성공
        assertDoesNotThrow(() -> postService.edit(post.getId(), editRequest, author.getId()));

        // expected - 작성자가 아닌 사용자가 수정 실패
        assertThrows(Unauthorized.class, () -> postService.edit(post.getId(), editRequest, otherUser.getId()));
    }

    @Test
    @DisplayName("게시글 삭제 - 작성자만 가능")
    void testDeletePostByAuthorOnly_Service() {
        // given
        Member author = Member.builder()
                .name("Author")
                .email("author@example.com")
                .password("1234")
                .build();
        memberRepository.save(author);

        Member nonAuthor = Member.builder()
                .name("NonAuthor")
                .email("nonauthor@example.com")
                .password("5678")
                .build();
        memberRepository.save(nonAuthor);

        Post post = Post.builder()
                .title("Title")
                .content("Content")
                .member(author)
                .build();
        postRepository.save(post);

        // when & then
        // 작성자가 삭제
        postService.delete(post.getId(), author.getId());
        assertFalse(postRepository.existsById(post.getId()));

        // 게시글 다시 생성 (삭제된 후 테스트 데이터 재준비)
        Post newPost = Post.builder()
                .title("New Title")
                .content("New Content")
                .member(author)
                .build();
        postRepository.save(newPost);

        // 작성자가 아닌 사용자가 삭제 시도(Unauthorized 발생)
        assertThrows(Unauthorized.class, () -> postService.delete(newPost.getId(), nonAuthor.getId()));
    }

}

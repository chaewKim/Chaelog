package com.chaewon.chaelog.Service;

import com.chaewon.chaelog.domain.Comment;
import com.chaewon.chaelog.domain.Post;
import com.chaewon.chaelog.domain.request.CommentCreateRequest;
import com.chaewon.chaelog.domain.request.CommentDeleteRequest;
import com.chaewon.chaelog.exception.InvalidPassword;
import com.chaewon.chaelog.exception.PostNotFound;
import com.chaewon.chaelog.repository.comment.CommentRepository;
import com.chaewon.chaelog.repository.post.PostRepository;
import com.chaewon.chaelog.service.CommentService;
import com.chaewon.chaelog.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class CommentServiceTest {
    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testWriteComment_Success() {
        // Given
        Long postId = 1L;
        CommentCreateRequest request = new CommentCreateRequest();
        request.setAuthor("Test Author");
        request.setContent("Test Content");
        request.setPassword("TestPassword");

        Post post = mock(Post.class);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // When
        assertDoesNotThrow(() -> commentService.write(postId, request));

        // Then
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testWriteComment_PostNotFound() {
        // Given
        Long postId = 1L;
        CommentCreateRequest request = new CommentCreateRequest();
        request.setAuthor("Test Author");
        request.setContent("Test Content");
        request.setPassword("TestPassword");

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PostNotFound.class, () -> commentService.write(postId, request));
    }

    @Test
    void testDeleteComment_InvalidPassword() {
        // Given
        CommentDeleteRequest request = new CommentDeleteRequest(1L, "wrongPassword");

        Comment comment = mock(Comment.class);
        when(commentRepository.findById(request.getId())).thenReturn(Optional.of(comment));
        when(comment.getPassword()).thenReturn("encodedPassword");
        when(passwordEncoder.matches(request.getPassword(), comment.getPassword())).thenReturn(false);

        // When & Then
        assertThrows(InvalidPassword.class, () -> commentService.delete(request));
    }

    @Test
    void testDeleteComment_Success() {
        // Given
        CommentDeleteRequest request = new CommentDeleteRequest(1L, "correctPassword");

        Comment comment = mock(Comment.class);
        when(commentRepository.findById(request.getId())).thenReturn(Optional.of(comment));
        // 여기서 encodedPassword를 실제 비밀번호로 설정하지 말고, passwordEncoder.matches()가 성공하도록 수정합니다.
        when(comment.getPassword()).thenReturn("encodedPassword");
        when(passwordEncoder.matches(request.getPassword(), comment.getPassword())).thenReturn(true); // 이 부분이 true로 반환되도록 설정되어야 합니다.

        // When
        assertDoesNotThrow(() -> commentService.delete(request));

        // Then
        verify(commentRepository, times(1)).delete(comment);
    }
}

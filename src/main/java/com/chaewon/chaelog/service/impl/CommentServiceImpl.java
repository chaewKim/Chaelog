package com.chaewon.chaelog.service.impl;

import com.chaewon.chaelog.domain.Comment;
import com.chaewon.chaelog.domain.Post;
import com.chaewon.chaelog.domain.request.CommentCreateRequest;
import com.chaewon.chaelog.domain.request.CommentDeleteRequest;
import com.chaewon.chaelog.domain.request.CommentSearchRequest;
import com.chaewon.chaelog.domain.response.CommentResponse;
import com.chaewon.chaelog.domain.response.PagingResponse;
import com.chaewon.chaelog.exception.CommentNotFound;
import com.chaewon.chaelog.exception.InvalidPassword;
import com.chaewon.chaelog.exception.PostNotFound;
import com.chaewon.chaelog.repository.comment.CommentRepository;
import com.chaewon.chaelog.repository.post.PostRepository;
import com.chaewon.chaelog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    //댓글 생성
    @Override
    @Transactional
    public void write(Long postId, CommentCreateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        //댓글 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        Comment comment = Comment.builder()
                .author(request.getAuthor())
                .password(encryptedPassword)
                .content(request.getContent())
                .post(post)
                .build();
        //게시글 존재한다면
//        post.addComment(comment);
        commentRepository.save(comment);
    }
    // 댓글 목록 조회
    @Override
    public PagingResponse<CommentResponse> getList(CommentSearchRequest commentSearchRequest) {
        Page<Comment> commentPage = commentRepository.getList(commentSearchRequest);
        return new PagingResponse<>(commentPage, CommentResponse.class);
    }

    //댓글 삭제
    @Override
    @Transactional
    public void delete(CommentDeleteRequest request) {
        Optional<Comment> optionalComment = commentRepository.findById(request.getId());

        optionalComment.ifPresent(comment -> {
            if (!passwordEncoder.matches(request.getPassword(), comment.getPassword())) {
                System.out.println("비밀번호가 일치하지 않음");  // 추가된 로그
                throw new InvalidPassword();
            }
            commentRepository.delete(comment);
        });
    }
}

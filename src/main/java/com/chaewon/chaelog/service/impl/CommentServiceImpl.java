package com.chaewon.chaelog.service.impl;

import com.chaewon.chaelog.domain.Comment;
import com.chaewon.chaelog.domain.Post;
import com.chaewon.chaelog.domain.request.CommentCreateRequest;
import com.chaewon.chaelog.domain.request.CommentDeleteRequest;
import com.chaewon.chaelog.exception.CommentNotFound;
import com.chaewon.chaelog.exception.InvalidPassword;
import com.chaewon.chaelog.exception.PostNotFound;
import com.chaewon.chaelog.repository.comment.CommentRepository;
import com.chaewon.chaelog.repository.post.PostRepository;
import com.chaewon.chaelog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
                .build();
        //게시글 존재한다면
        post.addComment(comment);
    }

    @Override
    public void delete(Long commentId, CommentDeleteRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);

        String encryptedPassword = comment.getPassword();
        if(!passwordEncoder.matches(request.getPassword(), encryptedPassword)) { //요청된 비밀번호와 암호화된 비밀번호가 같은지
            throw new InvalidPassword();


        }

        commentRepository.delete(comment);

    }


}

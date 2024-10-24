package com.chaewon.chaelog.controller;

import com.chaewon.chaelog.domain.request.CommentCreateRequest;
import com.chaewon.chaelog.domain.request.CommentDeleteRequest;
import com.chaewon.chaelog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    //댓글 생성
    @PostMapping("posts/{postId}/comments")
    public void write(@PathVariable("postId") Long postId, @RequestBody @Valid CommentCreateRequest request) {

        commentService.write(postId, request);
    }

    //댓글 삭제
    @PostMapping("/comments/{commentId}/delete")
    public void delete(@PathVariable("commentId") Long commentId, @RequestBody @Valid CommentDeleteRequest request) {
        commentService.delete(commentId, request);
    }

}

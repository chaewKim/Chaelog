package com.chaewon.chaelog.controller;

import com.chaewon.chaelog.domain.Comment;
import com.chaewon.chaelog.domain.request.CommentCreateRequest;
import com.chaewon.chaelog.domain.request.CommentDeleteRequest;
import com.chaewon.chaelog.domain.request.CommentSearchRequest;
import com.chaewon.chaelog.domain.response.CommentResponse;
import com.chaewon.chaelog.domain.response.PagingResponse;
import com.chaewon.chaelog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    //댓글 생성
    @PostMapping("/api/posts/{postId}/comments")
    public void write(@PathVariable("postId") Long postId, @RequestBody @Valid CommentCreateRequest request) {

        commentService.write(postId, request);
    }

    // 댓글 리스트 조회
    @GetMapping("/api/posts/{postId}/comments")
    public PagingResponse<CommentResponse> getList(@PathVariable("postId") Long postId, @ModelAttribute CommentSearchRequest commentSearch) {
        commentSearch.setPostId(postId);
        return commentService.getList(commentSearch);
    }

    // 댓글 삭제
    @DeleteMapping("/api/comments")
    public void delete(@RequestBody @Valid CommentDeleteRequest request) {
        commentService.delete(request);
    }

}

package com.chaewon.chaelog.controller;

import com.chaewon.chaelog.config.ChaelogPermissionEvaluator;
import com.chaewon.chaelog.config.MemberPrincipal;
import com.chaewon.chaelog.domain.Post;
import com.chaewon.chaelog.domain.request.PostCreateRequest;
import com.chaewon.chaelog.domain.request.PostEditRequest;
import com.chaewon.chaelog.domain.request.PostSearchRequest;
import com.chaewon.chaelog.domain.response.PagingResponse;
import com.chaewon.chaelog.domain.response.PostResponse;
import com.chaewon.chaelog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    //글 작성
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/api/posts")
    public void post(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody @Valid PostCreateRequest request) {
        postService.write(memberPrincipal.getMemberId(), request);
    }

    //게시글 단건조회
    @GetMapping("/api/posts/{postId}")
    public PostResponse get(@PathVariable("postId") Long postId) {
        return postService.get(postId);
    }

    //게시글 리스트 조회
    @GetMapping("/api/posts")
    public PagingResponse<PostResponse> getList(@ModelAttribute PostSearchRequest postSearch) {
        return postService.getList(postSearch);
    }

    //수정
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/api/posts/{postId}")
    public void edit(@PathVariable("postId") Long postId, @RequestBody @Valid PostEditRequest request) {
        postService.edit(postId, request);
    }

    //삭제
    // 권한 검사를 위한 PreAuthorize 사용
    @PreAuthorize("hasRole('ROLE_ADMIN') && hasPermission(#p0, 'POST', 'DELETE')")
    @DeleteMapping("/api/posts/{postId}")
    public void delete(@PathVariable("postId") Long postId) {
        log.info("삭제할 게시글 ID: {}", postId);
        postService.delete(postId);  // 권한 검사를 통과한 경우 삭제 수행
    }
}

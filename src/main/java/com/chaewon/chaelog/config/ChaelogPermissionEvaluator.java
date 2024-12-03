package com.chaewon.chaelog.config;

import com.chaewon.chaelog.exception.PostNotFound;
import com.chaewon.chaelog.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

//특정 블로그 포스트에 대한 수정, 삭제 등의 권한이 있는지 여부를 결정
//커스텀 권한 평가기
@Slf4j
@RequiredArgsConstructor
public class ChaelogPermissionEvaluator implements PermissionEvaluator {

    private final PostRepository postRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        log.info("hasPermission called with targetId: {}, targetType: {}, permission: {}", targetId, targetType, permission);

        if (targetId == null) {
            log.error("targetId가 null입니다.");
            return false; // 권한 없음으로 처리
        }

        var memberPrincipal = (MemberPrincipal) authentication.getPrincipal();

        // 게시글 존재 확인 및 권한 확인
        var post = postRepository.findById((Long) targetId).orElse(null);

        if (post == null) {
            log.error("[인가실패] 게시글이 존재하지 않습니다. targetId={}", targetId);
            return false; // 게시글이 없는 경우 권한 없음
        }

        if (!post.getMemberId().equals(memberPrincipal.getMemberId())) {
            log.error("[인가실패] 해당 사용자가 작성한 글이 아닙니다. targetId={}", targetId);
            return false; // 권한 없음
        }

        return true; // 권한 있음
    }




}

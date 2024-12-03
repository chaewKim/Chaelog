package com.chaewon.chaelog.controller;

import com.chaewon.chaelog.config.MemberPrincipal;
import com.chaewon.chaelog.domain.response.MemberResponse;
import com.chaewon.chaelog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/members/me") //현재 인증받느 걸 기반으로 함
    public ResponseEntity<MemberResponse> getMe(@AuthenticationPrincipal MemberPrincipal memberPrincipal) { //@AuthenticationPrincipal을 통해 인증된 사용자 정보 전달
        if (memberPrincipal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //로그인을 하지 않은 경우 -> UNAUTHORIZED
        }
        MemberResponse memberResponse = memberService.getMemberProfile(memberPrincipal.getMemberId()); //그외 -> 사용자 정보 응답
        return ResponseEntity.ok().body(memberResponse);


    }
}

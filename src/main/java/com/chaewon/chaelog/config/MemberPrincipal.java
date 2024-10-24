package com.chaewon.chaelog.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class MemberPrincipal extends User {

    private final Long memberId;
    public MemberPrincipal(com.chaewon.chaelog.domain.Member member){
        super(member.getEmail(), member.getPassword(),
                List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN")));//역할 : ROLE_ADMIN, 권한 : ADMIN -> 구별 잘 하기
        this.memberId = member.getId();

    }

    public Long getMemberId() {
        return memberId;
    }
}

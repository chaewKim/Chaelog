package com.chaewon.chaelog.config;

import com.chaewon.chaelog.domain.Member;
import com.chaewon.chaelog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

@RequiredArgsConstructor
public class MockSecurityContext implements WithSecurityContextFactory<MockMember> {

    private final MemberRepository memberRepository;
    @Override
    public SecurityContext createSecurityContext(MockMember annotation) {
        var member = Member.builder()
                .email(annotation.email())
                .name(annotation.name())
                .password(annotation.password())
                .build();

        memberRepository.save(member);

        var principal = new MemberPrincipal(member);

        var role = new SimpleGrantedAuthority("ROLE_ADMIN");
        var authenticationToken = new UsernamePasswordAuthenticationToken(principal,
                member.getPassword(),
                List.of(role));

        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);

        return context;
    }
}

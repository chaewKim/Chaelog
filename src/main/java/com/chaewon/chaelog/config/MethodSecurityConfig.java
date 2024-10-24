package com.chaewon.chaelog.config;

import com.chaewon.chaelog.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/*
* 메서드 권한 제어
* 특정 사용자가 특정 블로그 포스에대해 어떤 권한이 있는지 검사
* */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class MethodSecurityConfig {

    private final PostRepository postRepository;

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        var handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(new ChaelogPermissionEvaluator(postRepository));
        return handler;
        //DefaultMethodSecurityExpressionHandler를 생성한 후 커스텀 권한 평가기 ChaelogPermissionEvaluator를 설정.

    }
}

package com.chaewon.chaelog.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockSecurityContext.class)
public @interface MockMember {

    String name() default "Kim";

    String email() default "chaewon0430@gmail.com";

    String password() default "1234";
}
//    String role() default "ROLE_ADMIN";


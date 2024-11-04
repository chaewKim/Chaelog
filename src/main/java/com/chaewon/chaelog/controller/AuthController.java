package com.chaewon.chaelog.controller;

import com.chaewon.chaelog.config.AppConfig;
import com.chaewon.chaelog.domain.request.SignupRequest;
import com.chaewon.chaelog.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;

    @PostMapping("/api/auth/signup")
    public void signup(@RequestBody SignupRequest signup) {
        authService.signup(signup);
    }

}

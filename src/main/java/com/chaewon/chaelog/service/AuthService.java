package com.chaewon.chaelog.service;

import com.chaewon.chaelog.domain.request.SignupRequest;

public interface AuthService {
    public void signup(SignupRequest signupRequest);
}

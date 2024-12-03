package com.chaewon.chaelog.service.impl;

import com.chaewon.chaelog.domain.Member;
import com.chaewon.chaelog.domain.request.SignupRequest;
import com.chaewon.chaelog.exception.AlreadyExistsEmailException;
import com.chaewon.chaelog.repository.MemberRepository;
import com.chaewon.chaelog.service.AuthService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    @Override
    public void signup(SignupRequest signupRequest) {
        Optional<Member> memberOptional = memberRepository.findByEmail(signupRequest.getEmail());
        //이메일 중복 검사
        if(memberOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        //이메일 중복 아니면 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(signupRequest.getPassword());

        Member member = Member.builder()
                .name(signupRequest.getName())
                .password(encryptedPassword)
                .email(signupRequest.getEmail())
                .build();

        memberRepository.save(member); //엔티티가 아니기 때문에 DTO -> Entity 변환

    }


}

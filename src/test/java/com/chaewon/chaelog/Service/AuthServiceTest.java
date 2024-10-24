package com.chaewon.chaelog.Service;

import com.chaewon.chaelog.domain.Member;
import com.chaewon.chaelog.domain.request.SignupRequest;
import com.chaewon.chaelog.exception.AlreadyExistsEmailException;
import com.chaewon.chaelog.repository.MemberRepository;
import com.chaewon.chaelog.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthServiceImpl authService;

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() throws Exception {
        //given
        SignupRequest signup = SignupRequest.builder()
                .email("chaewon0430@naver.com")
                .password("1234")
                .name("채원")
                .build();

        //when
        authService.signup(signup);

        //then
        assertEquals(1,memberRepository.count());
        Member member = memberRepository.findAll().iterator().next();
        assertEquals("chaewon0430@naver.com", member.getEmail());
        assertNotNull(member.getPassword());
        assertNotEquals("1234", member.getPassword());
        assertEquals("채원", member.getName());


    }
    @Test
    @DisplayName("회원가입시 중복된 이메일")
    void test2() throws Exception {
        //given
        Member member = Member.builder()
                .email("chaewon0430@naver.com")
                .password("1234")
                .name("아무개")
                .build();
        memberRepository.save(member);

        SignupRequest signup = SignupRequest.builder()
                .password("1234")
                .email("chaewon0430@naver.com")
                .name("채원")
                .build();

        //expected
        assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signup));
    }

}

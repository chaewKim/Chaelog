package com.chaewon.chaelog.Controller;

import com.chaewon.chaelog.config.filter.EmailPasswordAuthFilter;
import com.chaewon.chaelog.domain.Member;
import com.chaewon.chaelog.domain.request.SignupRequest;
import com.chaewon.chaelog.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }
    @Test
    @DisplayName("회원가입")
    void test6() throws Exception {
        // given
        SignupRequest signup = SignupRequest.builder()
                .email("anjh7127@naver.com")
                .password("1234")
                .name("채원")
                .build();

        // expected
        mockMvc.perform(post("/api/auth/signup")
                        .content(objectMapper.writeValueAsString(signup))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("로그인 성공 테스트")
    void shouldLoginSuccessfully() throws Exception {
        // given
        memberRepository.save(Member.builder()
                .email("test@example.com")
                .password(passwordEncoder.encode("password123"))
                .name("Test User")
                .build());

        EmailPasswordAuthFilter.EmailPassword loginRequest = new EmailPasswordAuthFilter.EmailPassword();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 비밀번호 불일치")
    void shouldFailWhenPasswordIsIncorrect() throws Exception {
        // given
        memberRepository.save(Member.builder()
                .email("test@example.com")
                .password(passwordEncoder.encode("password123"))
                .name("Test User")
                .build());

        EmailPasswordAuthFilter.EmailPassword loginRequest = new EmailPasswordAuthFilter.EmailPassword();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongpassword");

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("아이디 혹은 비밀번호가 올바르지 않습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 사용자 미존재")
    void shouldFailWhenUserDoesNotExist() throws Exception {
        // given
        EmailPasswordAuthFilter.EmailPassword loginRequest = new EmailPasswordAuthFilter.EmailPassword();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("password123");

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("아이디 혹은 비밀번호가 올바르지 않습니다."))
                .andDo(print());
    }
}

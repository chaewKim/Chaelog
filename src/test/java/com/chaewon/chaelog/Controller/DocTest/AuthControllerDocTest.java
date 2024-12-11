package com.chaewon.chaelog.Controller.DocTest;

import com.chaewon.chaelog.config.filter.EmailPasswordAuthFilter;
import com.chaewon.chaelog.domain.Member;
import com.chaewon.chaelog.domain.request.SignupRequest;
import com.chaewon.chaelog.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class AuthControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;
    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 API 문서화")
    void documentSignup() throws Exception {
        SignupRequest signupRequest = SignupRequest.builder()
                .email("test@example.com")
                .password("password123")
                .name("Test User")
                .build();

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("auth-signup",
                        requestFields(
                                fieldWithPath("email").description("사용자 이메일")
                                        .attributes(key("constraints").value(
                                                getConstraintMessage(SignupRequest.class,"email")
                                        )),
                                fieldWithPath("password").description("사용자 비밀번호")
                                        .attributes(key("constraints").value(
                                                getConstraintMessage(SignupRequest.class,"password")
                                        )),
                                fieldWithPath("name").description("사용자 이름")
                                        .attributes(key("constraints").value(
                                                getConstraintMessage(SignupRequest.class,"name")
                                        ))
                        )
                        // No response fields since the body is empty.
                ));
    }

    @Test
    @DisplayName("로그인 성공 API 문서화")
    void documentLoginSuccess() throws Exception {
        memberRepository.save(Member.builder()
                .email("test@example.com")
                .password(passwordEncoder.encode("password123"))
                .name("Test User")
                .build());

        EmailPasswordAuthFilter.EmailPassword loginRequest = new EmailPasswordAuthFilter.EmailPassword();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("auth-login-success",
                        requestFields(
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("password").description("사용자 비밀번호")
                        )
                        // No response fields since the body is empty.
                ));
    }

    @Test
    @DisplayName("로그인 실패 API 문서화 - 비밀번호 불일치")
    void documentLoginFailure() throws Exception {
        memberRepository.save(Member.builder()
                .email("test@example.com")
                .password(passwordEncoder.encode("password123"))
                .name("Test User")
                .build());
        EmailPasswordAuthFilter.EmailPassword loginRequest = new EmailPasswordAuthFilter.EmailPassword();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("auth-login-failure",
                        requestFields(
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("password").description("사용자 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("오류 메시지"),
                                fieldWithPath("validation").description("검증 오류에 대한 추가 정보").optional()
                        )
                ));
    }

    private static String getConstraintMessage(Class<?> constraintClassType, String propertyName) {
        ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(constraintClassType);
        List<String> nameDescription = constraintDescriptions.descriptionsForProperty(propertyName);
        return String.join("\n", nameDescription);    }
    private static Attributes.Attribute constrainsAttribute(Class<?> constraintClassType, String propertyName) {
        return key("constraints").value(getConstraintMessage(constraintClassType, propertyName));    }
}
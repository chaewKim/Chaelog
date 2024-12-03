package com.chaewon.chaelog.Controller;


import com.chaewon.chaelog.domain.request.CommentCreateRequest;
import com.chaewon.chaelog.domain.request.CommentDeleteRequest;
import com.chaewon.chaelog.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    @DisplayName("댓글 작성 성공")
    void testWriteComment_Success() throws Exception {
        // Given
        CommentCreateRequest request = new CommentCreateRequest();
        request.setAuthor("Kim");
        request.setContent("Test Content");
        request.setPassword("123456");

        // When & Then
        mockMvc.perform(post("/api/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());

        verify(commentService, times(1)).write(anyLong(), any(CommentCreateRequest.class));
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void testDeleteComment_Success() throws Exception {
        // Given
        CommentDeleteRequest request = new CommentDeleteRequest(1L, "TestPassword");

        Mockito.doNothing().when(commentService).delete(Mockito.any(CommentDeleteRequest.class));

        // When & Then
        mockMvc.perform(delete("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());

        verify(commentService, times(1)).delete(any(CommentDeleteRequest.class));
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 잘못된 비밀번호")
    void testDeleteComment_InvalidPassword() throws Exception {
        // Given
        CommentDeleteRequest request = new CommentDeleteRequest(1L, "InvalidPassword");

        doThrow(new RuntimeException("Invalid password")).when(commentService).delete(any(CommentDeleteRequest.class));

        // When & Then
        mockMvc.perform(delete("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Invalid password"))
                .andDo(print());

        verify(commentService, times(1)).delete(any(CommentDeleteRequest.class));
    }
}

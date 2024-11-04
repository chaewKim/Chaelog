package com.chaewon.chaelog.service;

import com.chaewon.chaelog.domain.Comment;
import com.chaewon.chaelog.domain.request.CommentCreateRequest;
import com.chaewon.chaelog.domain.request.CommentDeleteRequest;
import com.chaewon.chaelog.domain.request.CommentSearchRequest;
import com.chaewon.chaelog.domain.response.CommentResponse;
import com.chaewon.chaelog.domain.response.PagingResponse;

import java.util.List;

public interface CommentService {
    void write(Long postId, CommentCreateRequest request);
    PagingResponse<CommentResponse> getList(CommentSearchRequest request);

    void delete(CommentDeleteRequest request);
}


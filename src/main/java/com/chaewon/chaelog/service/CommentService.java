package com.chaewon.chaelog.service;

import com.chaewon.chaelog.domain.request.CommentCreateRequest;
import com.chaewon.chaelog.domain.request.CommentDeleteRequest;

public interface CommentService {

    void write(Long postId, CommentCreateRequest request);

    void delete(Long commentId, CommentDeleteRequest request);
}


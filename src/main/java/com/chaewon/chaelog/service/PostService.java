package com.chaewon.chaelog.service;

import com.chaewon.chaelog.domain.request.PostCreateRequest;
import com.chaewon.chaelog.domain.request.PostEditRequest;
import com.chaewon.chaelog.domain.request.PostSearchRequest;
import com.chaewon.chaelog.domain.response.PagingResponse;
import com.chaewon.chaelog.domain.response.PostResponse;

import java.util.List;

public interface PostService {
    public void write(Long memberId,PostCreateRequest postCreateRequest);
    public PostResponse get(Long id);
    public PagingResponse<PostResponse> getList(PostSearchRequest postSearchRequest);
    PagingResponse<PostResponse> search(PostSearchRequest request);

    public void edit(Long id, PostEditRequest postEditRequest, Long memberId);
    public void delete(Long id,Long memberId);
}

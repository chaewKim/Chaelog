package com.chaewon.chaelog.repository.post;

import com.chaewon.chaelog.domain.Post;
import com.chaewon.chaelog.domain.request.PostSearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostRepositoryCustom {

    Page<Post> getList(PostSearchRequest postSearch);
}

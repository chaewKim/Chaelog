package com.chaewon.chaelog.repository.post;

import com.chaewon.chaelog.domain.Post;
import com.chaewon.chaelog.domain.request.PostSearchRequest;
import org.springframework.data.domain.Page;

public interface PostRepositoryCustom {
    // 특정 카테고리에 속한 게시글을 페이지네이션으로 조회
//    Page<Post> findByCategory(Category category, Pageable pageable);
    Page<Post> getList(PostSearchRequest postSearch);
    Page<Post> searchByKeyword(PostSearchRequest request);
}

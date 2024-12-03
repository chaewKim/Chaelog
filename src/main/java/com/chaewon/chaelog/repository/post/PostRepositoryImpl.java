package com.chaewon.chaelog.repository.post;


import com.chaewon.chaelog.domain.Post;
import com.chaewon.chaelog.domain.request.PostSearchRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static com.chaewon.chaelog.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<Post> getList(PostSearchRequest postSearch) {
       //게시글 전체 개수 조회
        Long totalCount = jpaQueryFactory.select(post.count())
                .from(post)
                .fetchFirst();

        List<Post> items = jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(post.id.desc())
                .fetch();

        return new PageImpl<>(items, postSearch.getPageable(), totalCount);
    }
    @Override
    public Page<Post> searchByKeyword(PostSearchRequest request) {
        List<Post> posts = jpaQueryFactory
                .selectFrom(post)
                .where(post.title.stringValue().lower().contains(request.getKeyword().toLowerCase()))
                //.where(containsKeyword(request.getKeyword()))
                .offset(request.getPageable().getOffset())
                .limit(request.getPageable().getPageSize())
                .orderBy(post.id.desc())
                .fetch();

        long totalCount = jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(containsKeyword(request.getKeyword()))
                .fetchFirst();

        return new PageImpl<>(posts, request.getPageable(), totalCount);
    }
    private BooleanExpression containsKeyword(String keyword) {
        return keyword == null ? null : post.title.containsIgnoreCase(keyword)
                .or(post.content.containsIgnoreCase(keyword));
    }
}

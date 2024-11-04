package com.chaewon.chaelog.repository.comment;

import com.chaewon.chaelog.domain.Comment;
import com.chaewon.chaelog.domain.request.CommentSearchRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static com.chaewon.chaelog.domain.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Comment> getList(CommentSearchRequest commentSearch) {
        // 댓글 전체 개수 조회
        Long totalCount = jpaQueryFactory.select(comment.count())
                .from(comment)
                .fetchFirst();

        List<Comment> items = jpaQueryFactory.selectFrom(comment)
                .where(comment.post.id.eq(commentSearch.getPostId()))
                .limit(commentSearch.getSize())
                .offset(commentSearch.getOffset())
                .orderBy(comment.id.desc())
                .fetch();

        return new PageImpl<>(items, commentSearch.getPageable(), totalCount);
    }
}

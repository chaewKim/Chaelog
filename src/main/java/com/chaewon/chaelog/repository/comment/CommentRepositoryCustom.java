package com.chaewon.chaelog.repository.comment;

import com.chaewon.chaelog.domain.Comment;
import com.chaewon.chaelog.domain.request.CommentSearchRequest;
import org.springframework.data.domain.Page;


public interface CommentRepositoryCustom {

    Page<Comment> getList(CommentSearchRequest commentSearch);


}

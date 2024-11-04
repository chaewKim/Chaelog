package com.chaewon.chaelog.domain.response;


import com.chaewon.chaelog.domain.Comment;
import lombok.Getter;

@Getter
public class CommentResponse {
    private final Long id;
    private final String author;
    private final String content;



    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.author = comment.getAuthor();
        this.content = comment.getContent();
    }
}

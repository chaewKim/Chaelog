package com.chaewon.chaelog.domain.request;

import lombok.Getter;

@Getter
public class CommentDeleteRequest {

    private String password;
    
    public CommentDeleteRequest() {
        
    }
    public CommentDeleteRequest(String password) {
        this.password = password;
    }
}

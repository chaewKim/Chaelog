package com.chaewon.chaelog.domain.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDeleteRequest {
    private Long id;
    private String password;

    public CommentDeleteRequest(Long id, String password) {
        this.id = id;
        this.password = password;
    }
}

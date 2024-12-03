package com.chaewon.chaelog.domain.response;

import com.chaewon.chaelog.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {
    private final Long id;
    private final String title;
    private final String content;

    private final String authorName; // 작성자 이름 추가
  //list 응답
    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorName = post.getMember() != null ? post.getMember().getName() : "Unknown"; // Null-safe 처리
    }

}

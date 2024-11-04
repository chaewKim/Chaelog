package com.chaewon.chaelog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        indexes = {
                @Index(name = "IDX_COMMENT_POST_ID",columnList = "post_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content; //댓글 내용
    @Column(nullable = false)
    private String author; //댓글 작성자
    @Column(nullable = false)
    private String password;

    //생성일자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Comment(String content, String author, String password, Post post) {
        this.content = content;
        this.author = author;
        this.password = password;
        this.post = post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}

package com.chaewon.chaelog.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor extends BaseTimeEntity{
    private final String title;
    private final String content;


    @Builder
    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
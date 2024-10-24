package com.chaewon.chaelog.domain.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {

    @Length(min = 1, max=8, message = "작성자는 1~8글자로 입력해주세요.")
    @NotBlank(message = "작성자를 입력해주세요.")
    private String author;

    @Length(min = 6, max=20, message = "비밀번호는 6~20글자로 입력해주세요.")

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @Length(min = 10, max=500, message = "내용은 500글자까지 입력해주세요.")
    @NotBlank(message = "내용를 입력해주세요.")
    private String content;

    @Builder
    public CommentCreateRequest(String author, String password, String content) {
        this.author = author;
        this.password = password;
        this.content = content;
    }
}

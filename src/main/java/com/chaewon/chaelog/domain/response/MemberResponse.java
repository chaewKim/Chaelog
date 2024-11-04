package com.chaewon.chaelog.domain.response;

import com.chaewon.chaelog.domain.Member;
import lombok.Getter;

@Getter
public class MemberResponse {
    private final Long id;
    private final String name;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.name = member.getName();
    }


}

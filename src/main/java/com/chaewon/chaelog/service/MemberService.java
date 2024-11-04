package com.chaewon.chaelog.service;

import com.chaewon.chaelog.domain.response.MemberResponse;

public interface MemberService {

    public MemberResponse getMemberProfile(Long memberId);
}

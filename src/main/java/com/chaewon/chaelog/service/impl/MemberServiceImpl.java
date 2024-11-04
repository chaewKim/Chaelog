package com.chaewon.chaelog.service.impl;

import com.chaewon.chaelog.domain.Member;
import com.chaewon.chaelog.domain.response.MemberResponse;
import com.chaewon.chaelog.exception.MemberNotFound;
import com.chaewon.chaelog.repository.MemberRepository;
import com.chaewon.chaelog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    @Override
    public MemberResponse getMemberProfile(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        return new MemberResponse(member);
    }
}

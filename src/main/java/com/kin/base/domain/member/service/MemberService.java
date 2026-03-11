package com.kin.base.domain.member.service;

import com.kin.base.domain.member.dto.MemberLoginDto;
import com.kin.base.domain.member.entity.Member;
import com.kin.base.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberLoginDto login(MemberLoginDto memberLoginDto) {
       Member member = memberRepository.login(memberLoginDto.getLoginId(), memberLoginDto.getPw()).orElseThrow(
               () -> new IllegalArgumentException("잘못된 로그인 정보 입니다!"));

       return new MemberLoginDto(member);

    }

}

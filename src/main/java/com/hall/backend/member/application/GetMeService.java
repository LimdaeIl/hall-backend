package com.hall.backend.member.application;

import com.hall.backend.auth.exception.AuthErrorCode;
import com.hall.backend.auth.exception.AuthException;
import com.hall.backend.auth.infrastructure.security.MemberPrincipal;
import com.hall.backend.member.domain.Member;
import com.hall.backend.member.infrastructure.jpa.MemberRepository;
import com.hall.backend.member.presentation.dto.response.GetMeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetMeService {

    private final MemberRepository memberRepository;


    @Transactional(readOnly = true)
    public GetMeResponse me(MemberPrincipal principal) {
        Member member = memberRepository.findById(principal.memberId())
                .orElseThrow(() -> new AuthException(AuthErrorCode.MEMBER_NOT_FOUND));

        return GetMeResponse.from(member);
    }
}

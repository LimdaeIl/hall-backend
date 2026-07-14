package com.hall.backend.member.application;

import com.hall.backend.auth.exception.AuthErrorCode;
import com.hall.backend.auth.exception.AuthException;
import com.hall.backend.member.domain.Member;
import com.hall.backend.member.infrastructure.jpa.MemberRepository;
import com.hall.backend.member.presentation.dto.response.GetMeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UpdateRoleService {

    private final MemberRepository memberRepository;

    @Transactional
    public GetMeResponse updateRole(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.MEMBER_NOT_FOUND));

        member.updateRoleByAdmin();

        return GetMeResponse.from(member);
    }
}

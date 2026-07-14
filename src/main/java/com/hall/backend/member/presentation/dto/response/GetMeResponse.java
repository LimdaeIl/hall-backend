package com.hall.backend.member.presentation.dto.response;

import com.hall.backend.member.domain.Member;

public record GetMeResponse(
        Long id,
        String email,
        String password,
        String name,
        String phone,
        String role,
        String status
) {

    public static GetMeResponse from(Member member) {
        return new GetMeResponse(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getName(),
                member.getPhone(),
                member.getRole().name(),
                member.getStatus().name()
        );
    }
}

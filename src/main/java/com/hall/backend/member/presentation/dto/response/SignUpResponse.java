package com.hall.backend.member.presentation.dto.response;

import com.hall.backend.member.domain.Member;

public record SignUpResponse(
        Long id,
        String email,
        String name,
        String phone,
        String role,
        String status
) {

    public static SignUpResponse from(Member member) {
        return new SignUpResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getPhone(),
                member.getRole().getDescription(),
                member.getStatus().getDescription()
        );
    }
}

package com.hall.backend.auth.infrastructure.security;

public record MemberPrincipal(
        Long memberId,
        String role
) {

}

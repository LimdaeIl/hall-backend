package com.hall.backend.auth.infrastructure.security;

public record AuthenticatedMember(
        Long memberId,
        String role
) {

}

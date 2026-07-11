package com.hall.backend.auth.infrastructure.auth;

public record AuthenticatedMember(
        Long memberId,
        String role
) {

}

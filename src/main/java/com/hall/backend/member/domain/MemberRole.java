package com.hall.backend.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MemberRole {
    MEMBER("회원"),
    ADMIN("관리자");

    private final String description;
}

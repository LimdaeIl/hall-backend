package com.hall.backend.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MemberStatus {
    ACTIVATE("활성"),
    DEACTIVATE("비활성");

    private final String description;
}

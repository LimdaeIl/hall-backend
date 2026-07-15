package com.hall.backend.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {

    CARD("신용카드"),
    BANK_TRANSFER("계좌이체"),
    MOCK("모의 결제");

    private final String description;
}

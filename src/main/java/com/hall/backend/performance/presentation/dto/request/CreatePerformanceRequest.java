package com.hall.backend.performance.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record CreatePerformanceRequest(

        @NotNull(message = "콘서트 ID는 필수입니다.")
        Long concertId,

        @NotNull(message = "공연 시작 시각은 필수입니다.")
        LocalDateTime startsAt,

        @NotNull(message = "예약 시작 시각은 필수입니다.")
        LocalDateTime reservationOpensAt,

        @NotNull(message = "예약 종료 시각은 필수입니다.")
        LocalDateTime reservationClosesAt,

        @NotNull(message = "회원당 최대 예매 수량은 필수입니다.")
        @Positive(message = "회원당 최대 예매 수량은 1 이상이어야 합니다.")
        Integer maxTicketsPerMember

) {
}

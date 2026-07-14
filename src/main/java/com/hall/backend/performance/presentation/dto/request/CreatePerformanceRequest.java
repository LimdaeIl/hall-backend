package com.hall.backend.performance.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

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
        Integer maxTicketsPerMember,

        @NotEmpty(message = "좌석 등급별 가격은 필수입니다.")
        List<@Valid SeatPrice> seatPrices
) {

    public record SeatPrice(

            @NotNull(message = "좌석 등급은 필수입니다.")
            String grade,

            @NotNull(message = "좌석 가격은 필수입니다.")
            @PositiveOrZero(message = "좌석 가격은 0원 이상이어야 합니다.")
            Long price
    ) {
    }
}

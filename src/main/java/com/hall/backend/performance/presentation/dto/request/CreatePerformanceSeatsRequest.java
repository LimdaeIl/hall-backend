package com.hall.backend.performance.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;

public record CreatePerformanceSeatsRequest(

        @NotEmpty(message = "좌석 등급별 가격 목록은 비어 있을 수 없습니다.")
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

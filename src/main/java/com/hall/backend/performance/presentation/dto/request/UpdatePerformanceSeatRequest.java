package com.hall.backend.performance.presentation.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record UpdatePerformanceSeatRequest(

        @Size(
                min = 1,
                max = 20,
                message = "좌석 번호는 1자 이상 20자 이하여야 합니다."
        )
        String seatNumber,

        String grade,

        @PositiveOrZero(
                message = "좌석 가격은 0원 이상이어야 합니다."
        )
        Long price,

        @Positive(
                message = "좌석 행 번호는 1 이상이어야 합니다."
        )
        Integer rowNumber,

        @Positive(
                message = "좌석 열 번호는 1 이상이어야 합니다."
        )
        Integer columnNumber,

        String status

) {
}

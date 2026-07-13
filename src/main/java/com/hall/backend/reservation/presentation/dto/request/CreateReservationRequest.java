package com.hall.backend.reservation.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateReservationRequest(

        @NotNull(message = "공연 ID는 필수입니다.")
        Long performanceId,

        @NotEmpty(message = "예약할 좌석을 선택해 주세요.")
        @Size(
                max = 4,
                message = "한 예약당 최대 4개 좌석까지 선택할 수 있습니다."
        )
        List<@NotNull Long> performanceSeatIds
) {
}

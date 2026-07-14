package com.hall.backend.reservation.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CreateReservationRequest(

        @NotEmpty(message = "좌석을 한 개 이상 선택해야 합니다.")
        List<Long> performanceSeatIds
) {
}

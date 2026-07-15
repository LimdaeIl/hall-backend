package com.hall.backend.concert.presentation.dto.request;

import com.hall.backend.concert.domain.ConcertStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateConcertStatusRequest(

        @NotNull(message = "변경할 공연 상태는 필수입니다.")
        ConcertStatus status
) {
}


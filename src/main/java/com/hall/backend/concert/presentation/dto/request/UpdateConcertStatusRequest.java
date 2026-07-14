package com.hall.backend.concert.presentation.dto.request;

import com.hall.backend.concert.domain.ConcertStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateConcertStatusRequest(

        @NotNull
        ConcertStatus status
) {
}

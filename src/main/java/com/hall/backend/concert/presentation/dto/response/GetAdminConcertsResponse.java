package com.hall.backend.concert.presentation.dto.response;

import com.hall.backend.concert.domain.Concert;
import com.hall.backend.concert.domain.ConcertStatus;
import java.time.Instant;

public record GetAdminConcertsResponse(
        Long concertId,
        String title,
        String artist,
        String description,
        ConcertStatus status,
        Instant createdAt,
        Instant updatedAt,
        Long createdBy,
        Long updatedBy
) {

    public static GetAdminConcertsResponse from(
            Concert concert
    ) {
        return new GetAdminConcertsResponse(
                concert.getId(),
                concert.getTitle(),
                concert.getArtist(),
                concert.getDescription(),
                concert.getStatus(),
                concert.getCreatedAt(),
                concert.getUpdatedAt(),
                concert.getCreatedBy(),
                concert.getUpdatedBy()
        );
    }
}
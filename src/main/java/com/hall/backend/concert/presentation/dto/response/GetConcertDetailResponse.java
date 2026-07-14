package com.hall.backend.concert.presentation.dto.response;

import com.hall.backend.concert.domain.Concert;
import com.hall.backend.concert.domain.ConcertStatus;
import java.time.Instant;

public record GetConcertDetailResponse(
        Long concertId,
        String title,
        String artist,
        String description,
        ConcertStatus status,
        Instant createdAt,
        Instant updatedAt
) {

    public static GetConcertDetailResponse from(Concert concert) {
        return new GetConcertDetailResponse(
                concert.getId(),
                concert.getTitle(),
                concert.getArtist(),
                concert.getDescription(),
                concert.getStatus(),
                concert.getCreatedAt(),
                concert.getUpdatedAt()
        );
    }
}

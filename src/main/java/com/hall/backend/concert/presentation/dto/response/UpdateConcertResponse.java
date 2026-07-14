package com.hall.backend.concert.presentation.dto.response;

import com.hall.backend.concert.domain.Concert;
import com.hall.backend.concert.domain.ConcertStatus;
import java.time.Instant;

public record UpdateConcertResponse(
        Long concertId,
        String title,
        String artist,
        String description,
        ConcertStatus status,
        Instant updatedAt
) {

    public static UpdateConcertResponse from(Concert concert) {
        return new UpdateConcertResponse(
                concert.getId(),
                concert.getTitle(),
                concert.getArtist(),
                concert.getDescription(),
                concert.getStatus(),
                concert.getUpdatedAt()
        );
    }
}

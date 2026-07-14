package com.hall.backend.concert.presentation.dto.response;

import com.hall.backend.concert.domain.Concert;
import com.hall.backend.concert.domain.ConcertStatus;
import java.time.Instant;

public record GetConcertsResponse(
        Long concertId,
        String title,
        String artist,
        String description,
        ConcertStatus status,
        Instant createdAt
) {

    public static GetConcertsResponse from(Concert concert) {
        return new GetConcertsResponse(
                concert.getId(),
                concert.getTitle(),
                concert.getArtist(),
                concert.getDescription(),
                concert.getStatus(),
                concert.getCreatedAt()
        );
    }
}

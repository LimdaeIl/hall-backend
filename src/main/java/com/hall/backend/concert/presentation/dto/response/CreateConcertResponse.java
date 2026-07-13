package com.hall.backend.concert.presentation.dto.response;

import com.hall.backend.concert.domain.Concert;

public record CreateConcertResponse(
        Long id,
        String title,
        String artist,
        String description,
        String status
) {

    public static CreateConcertResponse from(Concert concert) {
        return new CreateConcertResponse(
                concert.getId(),
                concert.getTitle(),
                concert.getArtist(),
                concert.getDescription(),
                concert.getStatus().name()
        );
    }
}

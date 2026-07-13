package com.hall.backend.concert.presentation.dto.request;

public record CreateConcertRequest(
        String title,
        String artist,
        String description
) {
}

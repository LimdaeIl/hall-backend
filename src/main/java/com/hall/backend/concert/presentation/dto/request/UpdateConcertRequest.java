package com.hall.backend.concert.presentation.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateConcertRequest(

        @Size(max = 100)
        String title,

        @Size(max = 100)
        String artist,

        @Size(max = 1000)
        String description
) {

        public boolean hasNoChanges() {
                return title == null
                        && artist == null
                        && description == null;
        }
}

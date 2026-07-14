package com.hall.backend.concert.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateConcertRequest(

        @NotBlank(message = "콘서트 제목은 필수입니다.")
        @Size(max = 100, message = "콘서트 제목은 100자를 초과할 수 없습니다.")
        String title,

        @NotBlank(message = "아티스트명은 필수입니다.")
        @Size(max = 100, message = "아티스트명은 100자를 초과할 수 없습니다.")
        String artist,

        @Size(max = 1000, message = "콘서트 설명은 1000자를 초과할 수 없습니다.")
        String description

) {
}

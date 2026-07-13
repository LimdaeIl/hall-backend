package com.hall.backend.concert.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateSeatRequest(

        @NotEmpty(message = "생성할 좌석 목록은 비어 있을 수 없습니다.")
        List<@Valid Seats> seats

) {

    public record Seats(

            @NotBlank(message = "좌석 번호는 필수입니다.")
            @Size(max = 20, message = "좌석 번호는 20자를 초과할 수 없습니다.")
            String seatNumber,

            @NotNull(message = "좌석 행 번호는 필수입니다.")
            @Positive(message = "좌석 행 번호는 1 이상이어야 합니다.")
            Integer rowNumber,

            @NotNull(message = "좌석 열 번호는 필수입니다.")
            @Positive(message = "좌석 열 번호는 1 이상이어야 합니다.")
            Integer columnNumber,

            @NotBlank(message = "좌석 등급은 필수입니다.")
            String grade

    ) {
    }
}

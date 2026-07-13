package com.hall.backend.reservation.presentation;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.reservation.application.CreateReservationService;
import com.hall.backend.reservation.presentation.dto.request.CreateReservationRequest;
import com.hall.backend.reservation.presentation.dto.response.CreateReservationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
@RestController
public class ReservationController {

    private final CreateReservationService createReservationService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateReservationResponse>> create(
            @Valid @RequestBody CreateReservationRequest request
    ) {
        CreateReservationResponse response = createReservationService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(
                                "공연 예약에 성공 했습니다.",
                                response)
                );
    }
}

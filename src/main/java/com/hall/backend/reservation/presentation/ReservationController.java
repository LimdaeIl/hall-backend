package com.hall.backend.reservation.presentation;

import com.hall.backend.auth.infrastructure.security.MemberPrincipal;
import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.reservation.application.CreateReservationService;
import com.hall.backend.reservation.presentation.dto.request.CreateReservationRequest;
import com.hall.backend.reservation.presentation.dto.response.CreateReservationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/performances")
@RestController
public class ReservationController {

    private final CreateReservationService createReservationService;

    @PostMapping("/{performanceId}/reservations")
    public ResponseEntity<ApiResponse<CreateReservationResponse>> createReservation(
            @PathVariable Long performanceId,
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody CreateReservationRequest request
    ) {
        CreateReservationResponse response = createReservationService.create(performanceId,
                principal, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.created(
                                "공연 예약: 공연 예약이 완료되었습니다.",
                                response
                        )
                );
    }
}

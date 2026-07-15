package com.hall.backend.reservation.presentation;

import com.hall.backend.auth.infrastructure.security.MemberPrincipal;
import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.reservation.application.CancelReservationService;
import com.hall.backend.reservation.application.GetReservationDetailService;
import com.hall.backend.reservation.presentation.dto.response.CancelReservationResponse;
import com.hall.backend.reservation.presentation.dto.response.GetReservationDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
@RestController
public class ReservationQueryController {

    private final GetReservationDetailService
            getReservationDetailService;

    private final CancelReservationService
            cancelReservationService;

    @GetMapping("/{reservationId}")
    public ResponseEntity<
            ApiResponse<GetReservationDetailResponse>
            > getReservation(
            @PathVariable Long reservationId,

            @AuthenticationPrincipal
            MemberPrincipal principal
    ) {
        GetReservationDetailResponse response =
                getReservationDetailService
                        .getReservation(
                                reservationId,
                                principal
                        );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "예약 상세 조회: 예약 상세 조회에 성공했습니다.",
                        response
                )
        );
    }

    @PatchMapping("/{reservationId}/cancel")
    public ResponseEntity<
            ApiResponse<CancelReservationResponse>
            > cancelReservation(
            @PathVariable Long reservationId,

            @AuthenticationPrincipal
            MemberPrincipal principal
    ) {
        CancelReservationResponse response =
                cancelReservationService.cancel(
                        reservationId,
                        principal
                );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "예약 취소: 예약 취소가 완료되었습니다.",
                        response
                )
        );
    }
}

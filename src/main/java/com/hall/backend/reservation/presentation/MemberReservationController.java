package com.hall.backend.reservation.presentation;

import com.hall.backend.auth.infrastructure.security.MemberPrincipal;
import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.common.response.PageResponse;
import com.hall.backend.reservation.application.GetMyReservationsService;
import com.hall.backend.reservation.domain.ReservationStatus;
import com.hall.backend.reservation.presentation.dto.request.ReservationSortType;
import com.hall.backend.reservation.presentation.dto.response.GetMyReservationsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/members/me/reservations")
@RestController
public class MemberReservationController {

    private final GetMyReservationsService
            getMyReservationsService;

    @GetMapping
    public ResponseEntity<
            ApiResponse<PageResponse<GetMyReservationsResponse>>
            > getMyReservations(
            @AuthenticationPrincipal
            MemberPrincipal principal,

            @RequestParam(required = false)
            ReservationStatus status,

            @RequestParam(defaultValue = "0")
            Integer page,

            @RequestParam(defaultValue = "20")
            Integer size,

            @RequestParam(defaultValue = "LATEST")
            ReservationSortType sort
    ) {
        PageResponse<GetMyReservationsResponse> response =
                getMyReservationsService.getReservations(
                        principal,
                        status,
                        page,
                        size,
                        sort
                );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "내 예약 목록 조회: 예약 목록 조회에 성공했습니다.",
                        response
                )
        );
    }
}

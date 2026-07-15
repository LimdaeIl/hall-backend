package com.hall.backend.reservation.presentation;

import com.hall.backend.auth.infrastructure.security.MemberPrincipal;
import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.common.response.PageResponse;
import com.hall.backend.reservation.application.CancelReservationService;
import com.hall.backend.reservation.application.CreateReservationService;
import com.hall.backend.reservation.application.GetMyReservationsService;
import com.hall.backend.reservation.application.GetReservationDetailService;
import com.hall.backend.reservation.domain.ReservationStatus;
import com.hall.backend.reservation.presentation.dto.request.CreateReservationRequest;
import com.hall.backend.reservation.presentation.dto.request.ReservationSortType;
import com.hall.backend.reservation.presentation.dto.response.CancelReservationResponse;
import com.hall.backend.reservation.presentation.dto.response.CreateReservationResponse;
import com.hall.backend.reservation.presentation.dto.response.GetMyReservationsResponse;
import com.hall.backend.reservation.presentation.dto.response.GetReservationDetailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ReservationController implements ReservationControllerDocs {

    private final CreateReservationService
            createReservationService;

    private final GetReservationDetailService
            getReservationDetailService;

    private final CancelReservationService
            cancelReservationService;

    private final GetMyReservationsService
            getMyReservationsService;

    @PostMapping(
            "/performances/{performanceId}/reservations"
    )
    public ResponseEntity<
            ApiResponse<CreateReservationResponse>
            > createReservation(
            @PathVariable Long performanceId,

            @AuthenticationPrincipal
            MemberPrincipal principal,

            @Valid
            @RequestBody
            CreateReservationRequest request
    ) {
        CreateReservationResponse response =
                createReservationService.create(
                        performanceId,
                        principal,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.created(
                                "공연 예약: 공연 예약이 완료되었습니다.",
                                response
                        )
                );
    }

    @GetMapping(
            "/reservations/{reservationId}"
    )
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

    @PatchMapping(
            "/reservations/{reservationId}/cancel"
    )
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

    @GetMapping(
            "/members/me/reservations"
    )
    public ResponseEntity<
            ApiResponse<
                    PageResponse<GetMyReservationsResponse>
                    >
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
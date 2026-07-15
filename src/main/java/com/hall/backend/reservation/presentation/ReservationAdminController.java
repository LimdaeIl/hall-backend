package com.hall.backend.reservation.presentation;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.common.response.PageResponse;
import com.hall.backend.payment.domain.PaymentStatus;
import com.hall.backend.reservation.application.GetAdminReservationsService;
import com.hall.backend.reservation.domain.ReservationStatus;
import com.hall.backend.reservation.presentation.dto.request.AdminReservationSortType;
import com.hall.backend.reservation.presentation.dto.response.GetAdminReservationsResponse;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/reservations")
@RestController
public class ReservationAdminController implements ReservationAdminControllerDocs {

    private final GetAdminReservationsService getAdminReservationsService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<GetAdminReservationsResponse>>> getReservations(
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) String memberEmail,
            @RequestParam(required = false) Long concertId,
            @RequestParam(required = false) Long performanceId,
            @RequestParam(required = false) ReservationStatus reservationStatus,
            @RequestParam(required = false) PaymentStatus paymentStatus,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdFrom,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate createdTo,

            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "LATEST") AdminReservationSortType sort
    ) {
        PageResponse<GetAdminReservationsResponse> response =
                getAdminReservationsService
                        .getReservations(
                                memberId,
                                memberEmail,
                                concertId,
                                performanceId,
                                reservationStatus,
                                paymentStatus,
                                createdFrom,
                                createdTo,
                                page,
                                size,
                                sort
                        );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "관리자 예약 목록 조회: 예약 목록 조회에 성공했습니다.",
                        response
                )
        );
    }
}

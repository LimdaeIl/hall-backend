package com.hall.backend.reservation.presentation;

import static com.hall.backend.common.config.SwaggerConfig.SECURITY_SCHEME_NAME;

import com.hall.backend.auth.infrastructure.security.MemberPrincipal;
import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.common.response.PageResponse;
import com.hall.backend.reservation.domain.ReservationStatus;
import com.hall.backend.reservation.presentation.dto.request.CreateReservationRequest;
import com.hall.backend.reservation.presentation.dto.request.ReservationSortType;
import com.hall.backend.reservation.presentation.dto.response.CancelReservationResponse;
import com.hall.backend.reservation.presentation.dto.response.CreateReservationResponse;
import com.hall.backend.reservation.presentation.dto.response.GetMyReservationsResponse;
import com.hall.backend.reservation.presentation.dto.response.GetReservationDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "예약",
        description = "공연 예약, 예약 조회 및 예약 취소 API"
)
@SecurityRequirement(name = SECURITY_SCHEME_NAME)
public interface ReservationControllerDocs {

    @Operation(
            summary = "공연 예약",
            description = "선택한 공연 좌석을 예약합니다."
    )
    ResponseEntity<ApiResponse<CreateReservationResponse>> createReservation(
            @Parameter(
                    description = "공연 회차 식별자",
                    example = "1",
                    required = true
            )
            Long performanceId,

            @Parameter(hidden = true)
            MemberPrincipal principal,

            CreateReservationRequest request
    );

    @Operation(
            summary = "예약 상세 조회",
            description = "현재 회원이 소유한 예약의 상세 정보를 조회합니다."
    )
    ResponseEntity<ApiResponse<GetReservationDetailResponse>> getReservation(
            @Parameter(
                    description = "예약 식별자",
                    example = "1",
                    required = true
            )
            Long reservationId,

            @Parameter(hidden = true)
            MemberPrincipal principal
    );

    @Operation(
            summary = "예약 취소",
            description = "현재 회원이 소유한 예약을 취소합니다."
    )
    ResponseEntity<ApiResponse<CancelReservationResponse>> cancelReservation(
            @Parameter(
                    description = "예약 식별자",
                    example = "1",
                    required = true
            )
            Long reservationId,

            @Parameter(hidden = true)
            MemberPrincipal principal
    );

    @Operation(
            summary = "내 예약 목록 조회",
            description = "현재 로그인한 회원의 예약 목록을 페이지 단위로 조회합니다."
    )
    ResponseEntity<ApiResponse<PageResponse<GetMyReservationsResponse>>>
    getMyReservations(
            @Parameter(hidden = true)
            MemberPrincipal principal,

            @Parameter(description = "예약 상태")
            ReservationStatus status,

            @Parameter(description = "페이지 번호", example = "0")
            Integer page,

            @Parameter(description = "페이지당 조회 개수", example = "20")
            Integer size,

            @Parameter(description = "정렬 기준", example = "LATEST")
            ReservationSortType sort
    );
}

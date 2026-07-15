package com.hall.backend.reservation.presentation;

import static com.hall.backend.common.config.SwaggerConfig.SECURITY_SCHEME_NAME;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.common.response.PageResponse;
import com.hall.backend.payment.domain.PaymentStatus;
import com.hall.backend.reservation.domain.ReservationStatus;
import com.hall.backend.reservation.presentation.dto.request.AdminReservationSortType;
import com.hall.backend.reservation.presentation.dto.response.GetAdminReservationsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "관리자 예약",
        description = "관리자 전용 예약 목록 조회 API"
)
@SecurityRequirement(name = SECURITY_SCHEME_NAME)
public interface ReservationAdminControllerDocs {

    @Operation(
            summary = "관리자 예약 목록 조회",
            description = """
                    전체 예약 목록을 페이지 단위로 조회합니다.
                    회원, 콘서트, 공연, 예약 상태, 결제 상태 및 생성일로 필터링할 수 있습니다.
                    """
    )
    ResponseEntity<ApiResponse<PageResponse<GetAdminReservationsResponse>>>
    getReservations(
            @Parameter(description = "회원 식별자", example = "1")
            Long memberId,

            @Parameter(
                    description = "회원 이메일",
                    example = "member@example.com"
            )
            String memberEmail,

            @Parameter(description = "콘서트 식별자", example = "1")
            Long concertId,

            @Parameter(description = "공연 회차 식별자", example = "1")
            Long performanceId,

            @Parameter(description = "예약 상태")
            ReservationStatus reservationStatus,

            @Parameter(description = "결제 상태")
            PaymentStatus paymentStatus,

            @Parameter(
                    description = "예약 생성일 검색 범위의 시작일",
                    example = "2026-07-01"
            )
            LocalDate createdFrom,

            @Parameter(
                    description = "예약 생성일 검색 범위의 종료일",
                    example = "2026-07-31"
            )
            LocalDate createdTo,

            @Parameter(description = "페이지 번호", example = "0")
            Integer page,

            @Parameter(description = "페이지당 조회 개수", example = "20")
            Integer size,

            @Parameter(description = "정렬 기준", example = "LATEST")
            AdminReservationSortType sort
    );
}

package com.hall.backend.performance.presentation;

import static com.hall.backend.common.config.SwaggerConfig.SECURITY_SCHEME_NAME;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.common.response.PageResponse;
import com.hall.backend.performance.domain.PerformanceStatus;
import com.hall.backend.performance.presentation.dto.request.CreatePerformanceRequest;
import com.hall.backend.performance.presentation.dto.request.CreatePerformanceSeatsRequest;
import com.hall.backend.performance.presentation.dto.request.PerformanceSortType;
import com.hall.backend.performance.presentation.dto.request.UpdatePerformanceRequest;
import com.hall.backend.performance.presentation.dto.request.UpdatePerformanceSeatRequest;
import com.hall.backend.performance.presentation.dto.request.UpdatePerformanceStatusRequest;
import com.hall.backend.performance.presentation.dto.response.CreatePerformanceResponse;
import com.hall.backend.performance.presentation.dto.response.CreatePerformanceSeatsResponse;
import com.hall.backend.performance.presentation.dto.response.DeletePerformanceResponse;
import com.hall.backend.performance.presentation.dto.response.DeletePerformanceSeatResponse;
import com.hall.backend.performance.presentation.dto.response.GetAdminPerformanceSeatsResponse;
import com.hall.backend.performance.presentation.dto.response.GetAdminPerformancesResponse;
import com.hall.backend.performance.presentation.dto.response.UpdatePerformanceResponse;
import com.hall.backend.performance.presentation.dto.response.UpdatePerformanceSeatResponse;
import com.hall.backend.performance.presentation.dto.response.UpdatePerformanceStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "관리자 공연",
        description = "관리자 전용 공연 회차 및 공연 좌석 관리 API"
)
@SecurityRequirement(name = SECURITY_SCHEME_NAME)
public interface PerformanceAdminControllerDocs {

    @Operation(
            summary = "공연 회차 생성",
            description = "특정 콘서트에 새로운 공연 회차를 생성합니다."
    )
    ResponseEntity<ApiResponse<CreatePerformanceResponse>> createPerformance(
            CreatePerformanceRequest request
    );

    @Operation(
            summary = "관리자 공연 목록 조회",
            description = "검색 조건을 이용하여 전체 공연 회차 목록을 조회합니다."
    )
    ResponseEntity<ApiResponse<PageResponse<GetAdminPerformancesResponse>>>
    getPerformances(
            @Parameter(description = "콘서트 식별자", example = "1")
            Long concertId,

            @Parameter(description = "콘서트 제목")
            String concertTitle,

            @Parameter(description = "공연 상태")
            PerformanceStatus status,

            @Parameter(
                    description = "공연 시작 일시 검색 범위의 시작값",
                    example = "2026-08-01T00:00:00"
            )
            LocalDateTime startsAtFrom,

            @Parameter(
                    description = "공연 시작 일시 검색 범위의 종료값",
                    example = "2026-08-31T23:59:59"
            )
            LocalDateTime startsAtTo,

            @Parameter(
                    description = "예약 시작 일시 검색 범위의 시작값",
                    example = "2026-07-01T00:00:00"
            )
            LocalDateTime reservationOpensAtFrom,

            @Parameter(
                    description = "예약 시작 일시 검색 범위의 종료값",
                    example = "2026-07-31T23:59:59"
            )
            LocalDateTime reservationOpensAtTo,

            @Parameter(
                    description = "예약 종료 일시 검색 범위의 시작값",
                    example = "2026-08-01T00:00:00"
            )
            LocalDateTime reservationClosesAtFrom,

            @Parameter(
                    description = "예약 종료 일시 검색 범위의 종료값",
                    example = "2026-08-31T23:59:59"
            )
            LocalDateTime reservationClosesAtTo,

            @Parameter(description = "페이지 번호", example = "0")
            Integer page,

            @Parameter(description = "페이지당 조회 개수", example = "20")
            Integer size,

            @Parameter(description = "정렬 기준", example = "LATEST")
            PerformanceSortType sort
    );

    @Operation(
            summary = "공연 회차 수정",
            description = "공연 회차의 기본 정보를 수정합니다."
    )
    ResponseEntity<ApiResponse<UpdatePerformanceResponse>> updatePerformance(
            @Parameter(
                    description = "공연 회차 식별자",
                    example = "1",
                    required = true
            )
            Long performanceId,

            UpdatePerformanceRequest request
    );

    @Operation(
            summary = "공연 회차 삭제 또는 취소",
            description = """
                    공연 회차를 삭제합니다.
                    예약 이력이 존재하면 실제 삭제 대신 취소 상태로 변경될 수 있습니다.
                    """
    )
    ResponseEntity<ApiResponse<DeletePerformanceResponse>> deletePerformance(
            @Parameter(
                    description = "공연 회차 식별자",
                    example = "1",
                    required = true
            )
            Long performanceId
    );

    @Operation(
            summary = "공연 상태 변경",
            description = "공연 회차의 운영 상태를 변경합니다."
    )
    ResponseEntity<ApiResponse<UpdatePerformanceStatusResponse>>
    updatePerformanceStatus(
            @Parameter(
                    description = "공연 회차 식별자",
                    example = "1",
                    required = true
            )
            Long performanceId,

            UpdatePerformanceStatusRequest request
    );

    @Operation(
            summary = "공연 좌석 일괄 생성",
            description = "공연장 좌석을 기반으로 해당 공연 회차의 좌석을 생성합니다."
    )
    ResponseEntity<ApiResponse<CreatePerformanceSeatsResponse>>
    createPerformanceSeats(
            @Parameter(
                    description = "공연 회차 식별자",
                    example = "1",
                    required = true
            )
            Long performanceId,

            CreatePerformanceSeatsRequest request
    );

    @Operation(
            summary = "관리자 공연 좌석 조회",
            description = "특정 공연 회차의 전체 좌석과 좌석 상태를 조회합니다."
    )
    ResponseEntity<ApiResponse<GetAdminPerformanceSeatsResponse>>
    getPerformanceSeats(
            @Parameter(
                    description = "공연 회차 식별자",
                    example = "1",
                    required = true
            )
            Long performanceId
    );

    @Operation(
            summary = "공연 좌석 수정",
            description = "특정 공연 좌석의 가격, 등급 또는 상태 정보를 수정합니다."
    )
    ResponseEntity<ApiResponse<UpdatePerformanceSeatResponse>>
    updatePerformanceSeat(
            @Parameter(
                    description = "공연 좌석 식별자",
                    example = "1",
                    required = true
            )
            Long performanceSeatId,

            UpdatePerformanceSeatRequest request
    );

    @Operation(
            summary = "공연 좌석 비활성화",
            description = "특정 공연 좌석을 사용 불가 상태로 변경합니다."
    )
    ResponseEntity<ApiResponse<DeletePerformanceSeatResponse>>
    deletePerformanceSeat(
            @Parameter(
                    description = "공연 좌석 식별자",
                    example = "1",
                    required = true
            )
            Long performanceSeatId
    );
}

package com.hall.backend.performance.presentation;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.performance.presentation.dto.response.GetAvailablePerformanceSeatsResponse;
import com.hall.backend.performance.presentation.dto.response.GetPerformanceDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "공연",
        description = "공연 상세 정보 및 예약 가능 좌석 조회 API"
)
public interface PerformanceControllerDocs {

    @Operation(
            summary = "예약 가능 공연 좌석 조회",
            description = "특정 공연 회차에서 현재 예약 가능한 좌석을 조회합니다."
    )
    ResponseEntity<ApiResponse<GetAvailablePerformanceSeatsResponse>>
    getAvailableSeats(
            @Parameter(
                    description = "공연 회차 식별자",
                    example = "1",
                    required = true
            )
            Long performanceId
    );

    @Operation(
            summary = "공연 상세 조회",
            description = "공연 회차의 시간, 상태 및 콘서트 정보를 조회합니다."
    )
    ResponseEntity<ApiResponse<GetPerformanceDetailResponse>>
    getPerformanceDetail(
            @Parameter(
                    description = "공연 회차 식별자",
                    example = "1",
                    required = true
            )
            Long performanceId
    );
}

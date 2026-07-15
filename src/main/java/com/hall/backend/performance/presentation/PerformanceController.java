package com.hall.backend.performance.presentation;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.performance.application.GetAvailablePerformanceSeatsService;
import com.hall.backend.performance.application.GetPerformanceDetailService;
import com.hall.backend.performance.presentation.dto.response.GetAvailablePerformanceSeatsResponse;
import com.hall.backend.performance.presentation.dto.response.GetPerformanceDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/performances")
@RestController
public class PerformanceController implements PerformanceControllerDocs {

    private final GetAvailablePerformanceSeatsService getAvailablePerformanceSeatsService;
    private final GetPerformanceDetailService getPerformanceDetailService;

    @GetMapping("/{performanceId}/seats")
    public ResponseEntity<ApiResponse<GetAvailablePerformanceSeatsResponse>> getAvailableSeats(
            @PathVariable Long performanceId
    ) {
        GetAvailablePerformanceSeatsResponse response =
                getAvailablePerformanceSeatsService.getAvailableSeats(performanceId);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "공연 좌석 조회: 예약 가능한 좌석 조회에 성공했습니다.",
                        response
                )
        );
    }
    @GetMapping("/{performanceId}")
    public ResponseEntity<ApiResponse<GetPerformanceDetailResponse>> getPerformanceDetail(
            @PathVariable Long performanceId
    ) {
        GetPerformanceDetailResponse response =
                getPerformanceDetailService.getDetail(performanceId);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "공연 상세 조회에 성공했습니다.",
                        response
                )
        );
    }

}

package com.hall.backend.performance.presentation;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.performance.application.GetAvailablePerformanceSeatsService;
import com.hall.backend.performance.presentation.dto.response.GetAvailablePerformanceSeatsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/performances")
@RestController
public class PerformanceController {

    private final GetAvailablePerformanceSeatsService
            getAvailablePerformanceSeatsService;

    @GetMapping("/{performanceId}/seats")
    public ResponseEntity<ApiResponse<GetAvailablePerformanceSeatsResponse>> getAvailableSeats(
            @PathVariable Long performanceId
    ) {
        GetAvailablePerformanceSeatsResponse response = getAvailablePerformanceSeatsService.getAvailableSeats(
                performanceId);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "공연 좌석 조회: 예약 가능한 좌석 조회에 성공했습니다.",
                        response
                )
        );
    }


}

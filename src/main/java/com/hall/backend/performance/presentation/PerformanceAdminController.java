package com.hall.backend.performance.presentation;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.performance.application.CreatePerformanceSeatsService;
import com.hall.backend.performance.application.CreatePerformanceService;
import com.hall.backend.performance.application.GetAdminPerformanceSeatsService;
import com.hall.backend.performance.application.UpdatePerformanceStatusService;
import com.hall.backend.performance.presentation.dto.request.CreatePerformanceRequest;
import com.hall.backend.performance.presentation.dto.request.CreatePerformanceSeatsRequest;
import com.hall.backend.performance.presentation.dto.request.UpdatePerformanceStatusRequest;
import com.hall.backend.performance.presentation.dto.response.CreatePerformanceResponse;
import com.hall.backend.performance.presentation.dto.response.CreatePerformanceSeatsResponse;
import com.hall.backend.performance.presentation.dto.response.GetAdminPerformanceSeatsResponse;
import com.hall.backend.performance.presentation.dto.response.UpdatePerformanceStatusResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/performances")
@RestController
public class PerformanceAdminController {

    private final CreatePerformanceService
            createPerformanceService;

    private final CreatePerformanceSeatsService
            createPerformanceSeatsService;

    private final UpdatePerformanceStatusService
            updatePerformanceStatusService;

    private final GetAdminPerformanceSeatsService
            getAdminPerformanceSeatsService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<
            ApiResponse<CreatePerformanceResponse>
            > createPerformance(
            @Valid
            @RequestBody
            CreatePerformanceRequest request
    ) {
        CreatePerformanceResponse response =
                createPerformanceService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.created(
                                "공연 생성: 공연 생성에 성공했습니다.",
                                response
                        )
                );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{performanceId}/seats")
    public ResponseEntity<
            ApiResponse<CreatePerformanceSeatsResponse>
            > createPerformanceSeats(
            @PathVariable Long performanceId,

            @Valid
            @RequestBody
            CreatePerformanceSeatsRequest request
    ) {
        CreatePerformanceSeatsResponse response =
                createPerformanceSeatsService.create(
                        performanceId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.created(
                                "공연 좌석 생성: 공연 좌석 일괄 생성에 성공했습니다.",
                                response
                        )
                );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{performanceId}/seats")
    public ResponseEntity<
            ApiResponse<GetAdminPerformanceSeatsResponse>
            > getPerformanceSeats(
            @PathVariable Long performanceId
    ) {
        GetAdminPerformanceSeatsResponse response =
                getAdminPerformanceSeatsService.getSeats(
                        performanceId
                );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "관리자 공연 좌석 조회: 전체 좌석 상태 조회에 성공했습니다.",
                        response
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{performanceId}/status")
    public ResponseEntity<
            ApiResponse<UpdatePerformanceStatusResponse>
            > updateStatus(
            @PathVariable Long performanceId,

            @Valid
            @RequestBody
            UpdatePerformanceStatusRequest request
    ) {
        UpdatePerformanceStatusResponse response =
                updatePerformanceStatusService.updateStatus(
                        performanceId,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "공연 상태 변경: 공연 상태가 변경되었습니다.",
                        response
                )
        );
    }
}

package com.hall.backend.performance.presentation;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.performance.application.CreatePerformanceService;
import com.hall.backend.performance.presentation.dto.request.CreatePerformanceRequest;
import com.hall.backend.performance.presentation.dto.response.CreatePerformanceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/performances")
@RestController
public class PerformanceAdminController {

    private final CreatePerformanceService createPerformanceService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreatePerformanceResponse>> createPerformance(
            @Valid @RequestBody CreatePerformanceRequest request
    ) {
        CreatePerformanceResponse response = createPerformanceService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(
                        "공연 생성에 성공했습니다.",
                        response)
                );

    }

}

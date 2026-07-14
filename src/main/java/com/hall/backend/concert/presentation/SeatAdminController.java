package com.hall.backend.concert.presentation;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.concert.application.CreateSeatService;
import com.hall.backend.concert.presentation.dto.request.CreateSeatRequest;
import com.hall.backend.concert.presentation.dto.response.CreateSeatResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/seats")
@RestController
public class SeatAdminController {

    private final CreateSeatService createSeatService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateSeatResponse>> create(
            @Valid @RequestBody CreateSeatRequest request
    ) {
        CreateSeatResponse response = createSeatService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(
                        "콘서트 좌석 생성에 성공했습니다.",
                        response)
                );
    }
}

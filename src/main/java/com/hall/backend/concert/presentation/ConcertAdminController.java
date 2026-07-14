package com.hall.backend.concert.presentation;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.concert.application.CreateConcertService;
import com.hall.backend.concert.application.UpdateConcertService;
import com.hall.backend.concert.application.UpdateConcertStatusService;
import com.hall.backend.concert.presentation.dto.request.CreateConcertRequest;
import com.hall.backend.concert.presentation.dto.request.UpdateConcertRequest;
import com.hall.backend.concert.presentation.dto.request.UpdateConcertStatusRequest;
import com.hall.backend.concert.presentation.dto.response.CreateConcertResponse;
import com.hall.backend.concert.presentation.dto.response.UpdateConcertResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/concerts")
@RestController
public class ConcertAdminController {

    private final CreateConcertService createConcertService;
    private final UpdateConcertService updateConcertService;
    private final UpdateConcertStatusService updateConcertStatusService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateConcertResponse>> create(
            @Valid @RequestBody CreateConcertRequest request
    ) {
        CreateConcertResponse response = createConcertService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(
                        "콘서트: 콘서트 생성에 성공했습니다.",
                        response
                ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{concertId}")
    public ResponseEntity<ApiResponse<UpdateConcertResponse>> updateConcert(
            @PathVariable Long concertId,
            @Valid @RequestBody UpdateConcertRequest request
    ) {
        UpdateConcertResponse response = updateConcertService.update(concertId, request);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "콘서트 수정: 콘서트 정보가 수정되었습니다.",
                        response
                )
        );
    }

    @PatchMapping("/{concertId}/status")
    public ResponseEntity<ApiResponse<UpdateConcertResponse>> updateConcertStatus(
            @PathVariable Long concertId,
            @Valid @RequestBody UpdateConcertStatusRequest request
    ) {
        UpdateConcertResponse response =
                updateConcertStatusService.updateStatus(concertId, request);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "콘서트 상태 변경: 콘서트 상태가 변경되었습니다.",
                        response
                )
        );
    }

}

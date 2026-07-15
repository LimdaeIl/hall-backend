package com.hall.backend.concert.presentation;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.concert.application.CreateSeatService;
import com.hall.backend.concert.presentation.dto.request.CreateSeatRequest;
import com.hall.backend.concert.presentation.dto.response.CreateSeatResponse;
import com.hall.backend.performance.application.DeletePerformanceSeatService;
import com.hall.backend.performance.application.UpdatePerformanceSeatService;
import com.hall.backend.performance.presentation.dto.request.UpdatePerformanceSeatRequest;
import com.hall.backend.performance.presentation.dto.response.DeletePerformanceSeatResponse;
import com.hall.backend.performance.presentation.dto.response.UpdatePerformanceSeatResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/seats")
@RestController
public class SeatAdminController {

    private final CreateSeatService createSeatService;

    private final UpdatePerformanceSeatService
            updatePerformanceSeatService;

    private final DeletePerformanceSeatService
            deletePerformanceSeatService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<
            ApiResponse<CreateSeatResponse>
            > create(
            @Valid
            @RequestBody
            CreateSeatRequest request
    ) {
        CreateSeatResponse response =
                createSeatService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.created(
                                "좌석 생성: 공연장 좌석 생성에 성공했습니다.",
                                response
                        )
                );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{seatId}")
    public ResponseEntity<
            ApiResponse<UpdatePerformanceSeatResponse>
            > updatePerformanceSeat(
            @PathVariable Long seatId,

            @Valid
            @RequestBody
            UpdatePerformanceSeatRequest request
    ) {
        UpdatePerformanceSeatResponse response =
                updatePerformanceSeatService.update(
                        seatId,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "공연 좌석 수정: 좌석 정보가 수정되었습니다.",
                        response
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{seatId}")
    public ResponseEntity<
            ApiResponse<DeletePerformanceSeatResponse>
            > deletePerformanceSeat(
            @PathVariable Long seatId
    ) {
        DeletePerformanceSeatResponse response =
                deletePerformanceSeatService.delete(
                        seatId
                );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "공연 좌석 비활성화: 좌석이 사용 불가 상태로 변경되었습니다.",
                        response
                )
        );
    }
}

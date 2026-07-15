package com.hall.backend.performance.presentation;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.common.response.PageResponse;
import com.hall.backend.performance.application.CreatePerformanceSeatsService;
import com.hall.backend.performance.application.CreatePerformanceService;
import com.hall.backend.performance.application.DeletePerformanceSeatService;
import com.hall.backend.performance.application.DeletePerformanceService;
import com.hall.backend.performance.application.GetAdminPerformanceSeatsService;
import com.hall.backend.performance.application.GetAdminPerformancesService;
import com.hall.backend.performance.application.UpdatePerformanceSeatService;
import com.hall.backend.performance.application.UpdatePerformanceService;
import com.hall.backend.performance.application.UpdatePerformanceStatusService;
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
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@RestController
public class PerformanceAdminController {

    private final CreatePerformanceService
            createPerformanceService;

    private final GetAdminPerformancesService
            getAdminPerformancesService;

    private final UpdatePerformanceService
            updatePerformanceService;

    private final DeletePerformanceService
            deletePerformanceService;

    private final UpdatePerformanceStatusService
            updatePerformanceStatusService;

    private final CreatePerformanceSeatsService
            createPerformanceSeatsService;

    private final GetAdminPerformanceSeatsService
            getAdminPerformanceSeatsService;

    private final UpdatePerformanceSeatService
            updatePerformanceSeatService;

    private final DeletePerformanceSeatService
            deletePerformanceSeatService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/performances")
    public ResponseEntity<
            ApiResponse<CreatePerformanceResponse>
            > createPerformance(
            @Valid
            @RequestBody
            CreatePerformanceRequest request
    ) {
        CreatePerformanceResponse response =
                createPerformanceService.create(
                        request
                );

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
    @GetMapping("/performances")
    public ResponseEntity<
            ApiResponse<
                    PageResponse<GetAdminPerformancesResponse>
                    >
            > getPerformances(
            @RequestParam(required = false)
            Long concertId,

            @RequestParam(required = false)
            String concertTitle,

            @RequestParam(required = false)
            PerformanceStatus status,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime startsAtFrom,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime startsAtTo,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime reservationOpensAtFrom,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime reservationOpensAtTo,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime reservationClosesAtFrom,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime reservationClosesAtTo,

            @RequestParam(defaultValue = "0")
            Integer page,

            @RequestParam(defaultValue = "20")
            Integer size,

            @RequestParam(defaultValue = "LATEST")
            PerformanceSortType sort
    ) {
        PageResponse<GetAdminPerformancesResponse> response =
                getAdminPerformancesService
                        .getPerformances(
                                concertId,
                                concertTitle,
                                status,
                                startsAtFrom,
                                startsAtTo,
                                reservationOpensAtFrom,
                                reservationOpensAtTo,
                                reservationClosesAtFrom,
                                reservationClosesAtTo,
                                page,
                                size,
                                sort
                        );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "관리자 공연 목록 조회에 성공했습니다.",
                        response
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/performances/{performanceId}")
    public ResponseEntity<
            ApiResponse<UpdatePerformanceResponse>
            > updatePerformance(
            @PathVariable Long performanceId,

            @Valid
            @RequestBody
            UpdatePerformanceRequest request
    ) {
        UpdatePerformanceResponse response =
                updatePerformanceService.update(
                        performanceId,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "공연 수정: 공연 정보가 수정되었습니다.",
                        response
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/performances/{performanceId}")
    public ResponseEntity<
            ApiResponse<DeletePerformanceResponse>
            > deletePerformance(
            @PathVariable Long performanceId
    ) {
        DeletePerformanceResponse response =
                deletePerformanceService.delete(
                        performanceId
                );

        String message =
                response.deleteType()
                        == DeletePerformanceResponse
                        .DeleteType.DELETED
                        ? "공연 삭제: 공연이 삭제되었습니다."
                        : "공연 취소: 예약 이력이 있어 공연이 취소 처리되었습니다.";

        return ResponseEntity.ok(
                ApiResponse.ok(
                        message,
                        response
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(
            "/performances/{performanceId}/status"
    )
    public ResponseEntity<
            ApiResponse<UpdatePerformanceStatusResponse>
            > updatePerformanceStatus(
            @PathVariable Long performanceId,

            @Valid
            @RequestBody
            UpdatePerformanceStatusRequest request
    ) {
        UpdatePerformanceStatusResponse response =
                updatePerformanceStatusService
                        .updateStatus(
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(
            "/performances/{performanceId}/seats"
    )
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
    @GetMapping(
            "/performances/{performanceId}/seats"
    )
    public ResponseEntity<
            ApiResponse<GetAdminPerformanceSeatsResponse>
            > getPerformanceSeats(
            @PathVariable Long performanceId
    ) {
        GetAdminPerformanceSeatsResponse response =
                getAdminPerformanceSeatsService
                        .getSeats(
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
    @PatchMapping("/performance-seats/{performanceSeatId}")
    public ResponseEntity<
            ApiResponse<UpdatePerformanceSeatResponse>
            > updatePerformanceSeat(
            @PathVariable Long performanceSeatId,

            @Valid
            @RequestBody
            UpdatePerformanceSeatRequest request
    ) {
        UpdatePerformanceSeatResponse response =
                updatePerformanceSeatService.update(
                        performanceSeatId,
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
    @DeleteMapping("/performance-seats/{performanceSeatId}")
    public ResponseEntity<
            ApiResponse<DeletePerformanceSeatResponse>
            > deletePerformanceSeat(
            @PathVariable Long performanceSeatId
    ) {
        DeletePerformanceSeatResponse response =
                deletePerformanceSeatService.delete(
                        performanceSeatId
                );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "공연 좌석 비활성화: 좌석이 사용 불가 상태로 변경되었습니다.",
                        response
                )
        );
    }
}

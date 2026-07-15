package com.hall.backend.concert.presentation;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.common.response.PageResponse;
import com.hall.backend.concert.application.CreateConcertService;
import com.hall.backend.concert.application.CreateSeatService;
import com.hall.backend.concert.application.DeleteConcertService;
import com.hall.backend.concert.application.GetAdminConcertsService;
import com.hall.backend.concert.application.UpdateConcertService;
import com.hall.backend.concert.application.UpdateConcertStatusService;
import com.hall.backend.concert.domain.ConcertStatus;
import com.hall.backend.concert.presentation.dto.request.ConcertSortType;
import com.hall.backend.concert.presentation.dto.request.CreateConcertRequest;
import com.hall.backend.concert.presentation.dto.request.CreateSeatRequest;
import com.hall.backend.concert.presentation.dto.request.UpdateConcertRequest;
import com.hall.backend.concert.presentation.dto.request.UpdateConcertStatusRequest;
import com.hall.backend.concert.presentation.dto.response.CreateConcertResponse;
import com.hall.backend.concert.presentation.dto.response.CreateSeatResponse;
import com.hall.backend.concert.presentation.dto.response.GetAdminConcertsResponse;
import com.hall.backend.concert.presentation.dto.response.UpdateConcertResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class ConcertAdminController {

    private final CreateConcertService
            createConcertService;

    private final GetAdminConcertsService
            getAdminConcertsService;

    private final UpdateConcertService
            updateConcertService;

    private final UpdateConcertStatusService
            updateConcertStatusService;

    private final DeleteConcertService
            deleteConcertService;

    private final CreateSeatService
            createSeatService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/concerts")
    public ResponseEntity<
            ApiResponse<CreateConcertResponse>
            > createConcert(
            @Valid
            @RequestBody
            CreateConcertRequest request
    ) {
        CreateConcertResponse response =
                createConcertService.create(
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.created(
                                "콘서트 생성: 콘서트 생성에 성공했습니다.",
                                response
                        )
                );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/concerts")
    public ResponseEntity<
            ApiResponse<
                    PageResponse<GetAdminConcertsResponse>
                    >
            > getConcerts(
            @RequestParam(required = false)
            String title,

            @RequestParam(required = false)
            String artist,

            @RequestParam(required = false)
            ConcertStatus status,

            @RequestParam(defaultValue = "0")
            Integer page,

            @RequestParam(defaultValue = "20")
            Integer size,

            @RequestParam(defaultValue = "LATEST")
            ConcertSortType sort
    ) {
        PageResponse<GetAdminConcertsResponse> response =
                getAdminConcertsService.getConcerts(
                        title,
                        artist,
                        status,
                        page,
                        size,
                        sort
                );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "관리자 콘서트 목록 조회에 성공했습니다.",
                        response
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/concerts/{concertId}")
    public ResponseEntity<
            ApiResponse<UpdateConcertResponse>
            > updateConcert(
            @PathVariable Long concertId,

            @Valid
            @RequestBody
            UpdateConcertRequest request
    ) {
        UpdateConcertResponse response =
                updateConcertService.update(
                        concertId,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "콘서트 수정: 콘서트 정보가 수정되었습니다.",
                        response
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/concerts/{concertId}/status")
    public ResponseEntity<
            ApiResponse<UpdateConcertResponse>
            > updateConcertStatus(
            @PathVariable Long concertId,

            @Valid
            @RequestBody
            UpdateConcertStatusRequest request
    ) {
        UpdateConcertResponse response =
                updateConcertStatusService.updateStatus(
                        concertId,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "콘서트 상태 변경: 콘서트 상태가 변경되었습니다.",
                        response
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/concerts/{concertId}")
    public ResponseEntity<ApiResponse<Void>>
    deleteConcert(
            @PathVariable Long concertId
    ) {
        deleteConcertService.delete(
                concertId
        );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "콘서트 삭제: 콘서트가 삭제되었습니다.",
                        null
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/seats")
    public ResponseEntity<
            ApiResponse<CreateSeatResponse>
            > createSeat(
            @Valid
            @RequestBody
            CreateSeatRequest request
    ) {
        CreateSeatResponse response =
                createSeatService.create(
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.created(
                                "좌석 생성: 공연장 좌석 생성에 성공했습니다.",
                                response
                        )
                );
    }
}

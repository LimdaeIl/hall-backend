package com.hall.backend.concert.presentation;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.concert.application.CreateConcertService;
import com.hall.backend.concert.presentation.dto.request.CreateConcertRequest;
import com.hall.backend.concert.presentation.dto.response.CreateConcertResponse;
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
@RequestMapping("/api/v1/admin/concerts")
@RestController
public class ConcertAdminController {

    private final CreateConcertService createConcertService;

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

}

package com.hall.backend.concert.presentation;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.common.response.PageResponse;
import com.hall.backend.concert.application.GetConcertDetailService;
import com.hall.backend.concert.application.GetConcertPerformancesService;
import com.hall.backend.concert.application.GetConcertsService;
import com.hall.backend.concert.presentation.dto.request.ConcertSortType;
import com.hall.backend.concert.presentation.dto.response.GetConcertDetailResponse;
import com.hall.backend.concert.presentation.dto.response.GetConcertPerformancesResponse;
import com.hall.backend.concert.presentation.dto.response.GetConcertsResponse;
import com.hall.backend.member.presentation.MemberControllerDocs;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/concerts")
@RestController
public class ConcertController implements ConcertControllerDocs {

    private final GetConcertsService getConcertsService;
    private final GetConcertDetailService getConcertDetailService;
    private final GetConcertPerformancesService getConcertPerformancesService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<GetConcertsResponse>>> getConcerts(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String artist,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "LATEST") ConcertSortType sort) {
        PageResponse<GetConcertsResponse> response = getConcertsService.getConcerts(title, artist, page, size, sort);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "콘서트 목록 조회: 콘서트 목록 조회에 성공했습니다.",
                        response
                )
        );
    }

    @GetMapping("/{concertId}")
    public ResponseEntity<ApiResponse<GetConcertDetailResponse>> getConcert(
            @PathVariable Long concertId) {
        GetConcertDetailResponse response = getConcertDetailService.getConcert(concertId);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "콘서트 상세 조회: 콘서트 상세 조회에 성공했습니다.",
                        response
                )
        );
    }

    @GetMapping("/{concertId}/performances")
    public ResponseEntity<ApiResponse<List<GetConcertPerformancesResponse>>> getConcertPerformances(
            @PathVariable Long concertId
    ) {
        List<GetConcertPerformancesResponse> response =
                getConcertPerformancesService.getPerformances(concertId);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "공연 회차 목록 조회: 공연 회차 목록 조회에 성공했습니다.",
                        response
                )
        );
    }
}

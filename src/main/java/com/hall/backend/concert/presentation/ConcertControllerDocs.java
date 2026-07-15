package com.hall.backend.concert.presentation;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.common.response.PageResponse;
import com.hall.backend.concert.presentation.dto.request.ConcertSortType;
import com.hall.backend.concert.presentation.dto.response.GetConcertDetailResponse;
import com.hall.backend.concert.presentation.dto.response.GetConcertPerformancesResponse;
import com.hall.backend.concert.presentation.dto.response.GetConcertsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "콘서트",
        description = "콘서트 목록, 상세 정보 및 공연 회차 조회 API"
)
public interface ConcertControllerDocs {

    @Operation(
            summary = "콘서트 목록 조회",
            description = """
                    공개된 콘서트 목록을 페이지 단위로 조회합니다.
                    콘서트 제목과 아티스트 이름으로 검색할 수 있습니다.
                    """
    )
    ResponseEntity<ApiResponse<PageResponse<GetConcertsResponse>>> getConcerts(
            @Parameter(
                    description = "검색할 콘서트 제목",
                    example = "2026 SUMMER CONCERT"
            )
            String title,

            @Parameter(
                    description = "검색할 아티스트 이름",
                    example = "Hall Band"
            )
            String artist,

            @Parameter(
                    description = "페이지 번호. 0부터 시작합니다.",
                    example = "0"
            )
            Integer page,

            @Parameter(
                    description = "페이지당 조회 개수",
                    example = "20"
            )
            Integer size,

            @Parameter(
                    description = "콘서트 정렬 기준",
                    example = "LATEST"
            )
            ConcertSortType sort
    );

    @Operation(
            summary = "콘서트 상세 조회",
            description = "콘서트 식별자를 이용하여 콘서트 상세 정보를 조회합니다."
    )
    ResponseEntity<ApiResponse<GetConcertDetailResponse>> getConcert(
            @Parameter(
                    description = "콘서트 식별자",
                    example = "1",
                    required = true
            )
            Long concertId
    );

    @Operation(
            summary = "콘서트 공연 회차 목록 조회",
            description = "특정 콘서트에 등록된 공연 회차 목록을 조회합니다."
    )
    ResponseEntity<ApiResponse<List<GetConcertPerformancesResponse>>>
    getConcertPerformances(
            @Parameter(
                    description = "콘서트 식별자",
                    example = "1",
                    required = true
            )
            Long concertId
    );
}

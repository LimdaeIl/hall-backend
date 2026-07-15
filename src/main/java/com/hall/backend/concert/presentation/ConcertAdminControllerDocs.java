package com.hall.backend.concert.presentation;

import static com.hall.backend.common.config.SwaggerConfig.SECURITY_SCHEME_NAME;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.common.response.PageResponse;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "관리자 콘서트",
        description = "관리자 전용 콘서트 및 공연장 좌석 관리 API"
)
@SecurityRequirement(name = SECURITY_SCHEME_NAME)
public interface ConcertAdminControllerDocs {

    @Operation(
            summary = "콘서트 생성",
            description = "새로운 콘서트를 생성합니다. 관리자 권한이 필요합니다."
    )
    ResponseEntity<ApiResponse<CreateConcertResponse>> createConcert(
            CreateConcertRequest request
    );

    @Operation(
            summary = "관리자 콘서트 목록 조회",
            description = """
                    관리자가 전체 콘서트 목록을 조회합니다.
                    제목, 아티스트 및 콘서트 상태로 필터링할 수 있습니다.
                    """
    )
    ResponseEntity<ApiResponse<PageResponse<GetAdminConcertsResponse>>> getConcerts(
            @Parameter(description = "콘서트 제목")
            String title,

            @Parameter(description = "아티스트 이름")
            String artist,

            @Parameter(description = "콘서트 상태")
            ConcertStatus status,

            @Parameter(description = "페이지 번호", example = "0")
            Integer page,

            @Parameter(description = "페이지당 조회 개수", example = "20")
            Integer size,

            @Parameter(description = "정렬 기준", example = "LATEST")
            ConcertSortType sort
    );

    @Operation(
            summary = "콘서트 정보 수정",
            description = "콘서트의 기본 정보를 수정합니다."
    )
    ResponseEntity<ApiResponse<UpdateConcertResponse>> updateConcert(
            @Parameter(
                    description = "콘서트 식별자",
                    example = "1",
                    required = true
            )
            Long concertId,

            UpdateConcertRequest request
    );

    @Operation(
            summary = "콘서트 상태 변경",
            description = "콘서트의 운영 상태를 변경합니다."
    )
    ResponseEntity<ApiResponse<UpdateConcertResponse>> updateConcertStatus(
            @Parameter(
                    description = "콘서트 식별자",
                    example = "1",
                    required = true
            )
            Long concertId,

            UpdateConcertStatusRequest request
    );

    @Operation(
            summary = "콘서트 삭제",
            description = "콘서트를 삭제합니다."
    )
    ResponseEntity<ApiResponse<Void>> deleteConcert(
            @Parameter(
                    description = "콘서트 식별자",
                    example = "1",
                    required = true
            )
            Long concertId
    );

    @Operation(
            summary = "공연장 좌석 생성",
            description = "공연에서 사용할 공연장 좌석 정보를 생성합니다."
    )
    ResponseEntity<ApiResponse<CreateSeatResponse>> createSeat(
            CreateSeatRequest request
    );
}

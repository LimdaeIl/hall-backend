package com.hall.backend.auth.presentation;

import com.hall.backend.auth.presentation.dto.request.SignInRequest;
import com.hall.backend.auth.presentation.dto.response.ReissueResponse;
import com.hall.backend.auth.presentation.dto.response.SignInResponse;
import com.hall.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "인증",
        description = "로그인, 토큰 재발급 및 로그아웃 API"
)
public interface AuthControllerDocs {

    @Operation(
            summary = "로그인",
            description = """
                    이메일과 비밀번호를 사용하여 로그인합니다.

                    로그인 성공 시 Access Token을 응답 본문으로 반환하고,
                    Refresh Token을 HttpOnly 쿠키로 설정합니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    useReturnTypeSchema = true
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "요청값 검증 실패",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "이메일 형식 오류",
                                    value = """
                                            {
                                              "timestamp": "2026-07-15T11:30:00",
                                              "status": 400,
                                              "code": "INVALID_INPUT",
                                              "message": "로그인: 올바른 이메일 형식이 아닙니다."
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "이메일 또는 비밀번호 불일치"
            )
    })
    ResponseEntity<ApiResponse<SignInResponse>> signIn(
            SignInRequest request,
            @Parameter(hidden = true)
            HttpServletResponse servletResponse
    );

    @Operation(
            summary = "토큰 재발급",
            description = """
                    Refresh Token 쿠키를 검증한 후 새로운 Access Token과
                    Refresh Token을 발급합니다.

                    Refresh Token Rotation 정책에 따라 기존 Refresh Token은
                    더 이상 사용할 수 없습니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "토큰 재발급 성공",
                    useReturnTypeSchema = true
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Refresh Token이 없거나 유효하지 않음"
            )
    })
    ResponseEntity<ReissueResponse> reissue(
            @Parameter(
                    name = "refreshToken",
                    description = "HttpOnly Refresh Token 쿠키",
                    required = true,
                    in = ParameterIn.COOKIE,
                    schema = @Schema(type = "string")
            )
            String refreshToken,

            @Parameter(hidden = true)
            HttpServletResponse servletResponse
    );

    @Operation(
            summary = "로그아웃",
            description = """
                    전달받은 Refresh Token을 폐기하고
                    브라우저의 Refresh Token 쿠키를 삭제합니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "로그아웃 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 Refresh Token"
            )
    })
    ResponseEntity<Void> signOut(
            @Parameter(
                    name = "refreshToken",
                    description = "HttpOnly Refresh Token 쿠키",
                    required = false,
                    in = ParameterIn.COOKIE,
                    schema = @Schema(type = "string")
            )
            String refreshToken,

            @Parameter(hidden = true)
            HttpServletResponse response
    );
}

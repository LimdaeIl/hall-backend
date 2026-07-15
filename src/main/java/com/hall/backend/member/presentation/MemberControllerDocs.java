package com.hall.backend.member.presentation;

import static com.hall.backend.common.config.SwaggerConfig.SECURITY_SCHEME_NAME;

import com.hall.backend.auth.infrastructure.security.MemberPrincipal;
import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.member.presentation.dto.request.SignUpRequest;
import com.hall.backend.member.presentation.dto.response.GetMeResponse;
import com.hall.backend.member.presentation.dto.response.SignUpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "회원",
        description = "회원 가입, 내 정보 조회 및 회원 탈퇴 API"
)
public interface MemberControllerDocs {

    @Operation(
            summary = "회원 가입",
            description = "이메일과 비밀번호 등의 회원 정보를 이용하여 신규 회원을 생성합니다."
    )
    ResponseEntity<ApiResponse<SignUpResponse>> signUp(
            SignUpRequest request
    );

    @Operation(
            summary = "내 정보 조회",
            description = "현재 로그인한 회원의 정보를 조회합니다."
    )
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    ResponseEntity<ApiResponse<GetMeResponse>> getMember(
            @Parameter(hidden = true)
            MemberPrincipal principal
    );

    @Operation(
            summary = "회원 탈퇴",
            description = """
                    현재 로그인한 회원을 탈퇴 처리합니다.
                    저장된 Refresh Token을 폐기하고 Refresh Token 쿠키를 삭제합니다.
                    """
    )
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    ResponseEntity<Void> deleteMember(
            @Parameter(
                    name = "refreshToken",
                    description = "HttpOnly Refresh Token 쿠키",
                    hidden = true
            )
            String refreshToken,

            @Parameter(hidden = true)
            HttpServletResponse response,

            @Parameter(hidden = true)
            MemberPrincipal principal
    );
}

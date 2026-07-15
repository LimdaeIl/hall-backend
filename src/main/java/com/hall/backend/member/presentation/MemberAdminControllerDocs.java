package com.hall.backend.member.presentation;

import static com.hall.backend.common.config.SwaggerConfig.SECURITY_SCHEME_NAME;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.member.presentation.dto.response.GetMeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "관리자 회원",
        description = "관리자 전용 회원 관리 API"
)
@SecurityRequirement(name = SECURITY_SCHEME_NAME)
public interface MemberAdminControllerDocs {

    @Operation(
            summary = "회원 권한 변경",
            description = "지정한 회원의 권한을 변경합니다. 관리자 권한이 필요합니다."
    )
    ResponseEntity<ApiResponse<GetMeResponse>> updateRole(
            @Parameter(
                    description = "권한을 변경할 회원 식별자",
                    example = "1",
                    required = true
            )
            Long memberId
    );
}

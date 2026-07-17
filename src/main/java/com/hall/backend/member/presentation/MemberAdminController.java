package com.hall.backend.member.presentation;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.member.application.UpdateRoleService;
import com.hall.backend.member.presentation.dto.response.GetMeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/members")
@RestController
public class MemberAdminController implements MemberAdminControllerDocs {

    private final UpdateRoleService updateRoleService;

    // @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{memberId}/role")
    public ResponseEntity<ApiResponse<GetMeResponse>> updateRole(
            @PathVariable Long memberId
    ) {
        GetMeResponse response = updateRoleService.updateRole(memberId);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "회원 권한 수정: 회원 권한 수정에 성공했습니다.",
                        response
                )
        );
    }
}

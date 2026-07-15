package com.hall.backend.member.presentation;

import com.hall.backend.auth.application.RefreshTokenCookieProvider;
import com.hall.backend.auth.infrastructure.security.MemberPrincipal;
import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.member.application.DeleteMeService;
import com.hall.backend.member.application.GetMeService;
import com.hall.backend.member.application.SignUpService;
import com.hall.backend.member.presentation.dto.request.SignUpRequest;
import com.hall.backend.member.presentation.dto.response.GetMeResponse;
import com.hall.backend.member.presentation.dto.response.SignUpResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@RestController
public class MemberController implements MemberControllerDocs {

    private final SignUpService signUpService;
    private final GetMeService getMeService;
    private final DeleteMeService deleteMeService;

    private final RefreshTokenCookieProvider cookieProvider;


    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(
            @Valid @RequestBody SignUpRequest request
    ) {
        SignUpResponse response = signUpService.signUp(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(
                        "회원 가입: 회원 가입이 완료되었습니다.",
                        response
                ));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<GetMeResponse>> getMember(
            @AuthenticationPrincipal MemberPrincipal principal
    ) {
        GetMeResponse response = getMeService.me(principal);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "나의 정보 조회: 나의 정보 조회에 성공했습니다.",
                        response
                )
        );
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMember(
            @CookieValue(value = "refreshToken", required = false)
            String refreshToken,
            HttpServletResponse response,
            @AuthenticationPrincipal MemberPrincipal principal
    ) {
        deleteMeService.delete(principal, refreshToken);
        cookieProvider.removeRefreshTokenCookie(response);

        return ResponseEntity.noContent().build();
    }

}

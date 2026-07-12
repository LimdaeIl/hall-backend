package com.hall.backend.auth.presentation;

import com.hall.backend.auth.application.RefreshTokenCookieProvider;
import com.hall.backend.auth.application.SignInService;
import com.hall.backend.auth.presentation.dto.request.SignInRequest;
import com.hall.backend.auth.presentation.dto.response.SignInResponse;
import com.hall.backend.auth.presentation.dto.response.SignInResult;
import com.hall.backend.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {


    private final SignInService signInService;
    private final RefreshTokenCookieProvider cookieProvider;


    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<SignInResponse>> signIn(
            @RequestBody @Valid SignInRequest request,
            HttpServletResponse servletResponse

    ) {
        SignInResult signInResult = signInService.signIn(request);

        cookieProvider.addRefreshTokenCookie(
                servletResponse,
                signInResult.refreshToken(),
                signInResult.refreshTokenRemainingSecond()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.ok(
                        "로그인: 로그인에 성공했습니다.",
                        SignInResponse.of(
                                signInResult.memberId(),
                                signInResult.accessToken(),
                                signInResult.refreshToken()
                        ))
                );
    }

    
}

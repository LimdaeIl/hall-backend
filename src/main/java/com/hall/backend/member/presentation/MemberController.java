package com.hall.backend.member.presentation;

import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.member.application.SignUpService;
import com.hall.backend.member.presentation.dto.request.SignUpRequest;
import com.hall.backend.member.presentation.dto.response.SignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@RestController
public class MemberController {

    private final SignUpService signUpService;

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(
            SignUpRequest request
    ) {
        SignUpResponse response = signUpService.signUp(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(
                        "회원 가입: 회원 가입이 완료되었습니다.",
                        response
                ));
    }

}

package com.hall.backend.member.application;


import com.hall.backend.member.domain.Member;
import com.hall.backend.member.exception.MemberErrorCode;
import com.hall.backend.member.exception.MemberException;
import com.hall.backend.member.infrastructure.jpa.MemberRepository;
import com.hall.backend.member.presentation.dto.request.SignUpRequest;
import com.hall.backend.member.presentation.dto.response.SignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SignUpService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new MemberException(MemberErrorCode.DUPLICATE_EMAIL);
        }

        if (memberRepository.existsByPhone(request.phone())) {
            throw new MemberException(MemberErrorCode.DUPLICATE_PHONE);
        }

        if (memberRepository.findByName(request.name())) {
            throw new MemberException(MemberErrorCode.DUPLICATE_NAME);
        }

        Member member = Member.create(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.name(),
                request.phone()
        );

        memberRepository.save(member);

        return SignUpResponse.from(member);
    }

}

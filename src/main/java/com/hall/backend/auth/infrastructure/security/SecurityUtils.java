package com.hall.backend.auth.infrastructure.security;

import com.hall.backend.auth.exception.AuthErrorCode;
import com.hall.backend.auth.exception.AuthException;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtils {

    public static MemberPrincipal getAuthenticatedMember() {
        return findAuthenticatedMember()
                .orElseThrow(() -> new AuthException(AuthErrorCode.UNAUTHENTICATED_MEMBER));
    }

    public static Optional<MemberPrincipal> findAuthenticatedMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!isAuthenticated(authentication)) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof MemberPrincipal memberPrincipal)) {
            return Optional.empty();
        }

        return Optional.of(memberPrincipal);
    }

    public static Long getCurrentMemberId() {
        return getAuthenticatedMember().memberId();
    }

    public static String getCurrentRole() {
        return getAuthenticatedMember().role();
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return isAuthenticated(authentication);
    }

    private static boolean isAuthenticated(
            Authentication authentication
    ) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}

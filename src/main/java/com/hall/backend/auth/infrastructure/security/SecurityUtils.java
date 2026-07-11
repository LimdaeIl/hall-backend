package com.hall.backend.auth.infrastructure.security;

import com.hall.backend.auth.exception.AuthErrorCode;
import com.hall.backend.auth.exception.AuthException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static AuthenticatedMember getAuthenticatedMember() {
        return findAuthenticatedMember()
                .orElseThrow(() ->
                        new AuthException(
                                AuthErrorCode.UNAUTHENTICATED_MEMBER
                        )
                );
    }

    public static Optional<AuthenticatedMember> findAuthenticatedMember() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (!isAuthenticated(authentication)) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof AuthenticatedMember authenticatedMember)) {
            return Optional.empty();
        }

        return Optional.of(authenticatedMember);
    }

    public static Long getCurrentMemberId() {
        return getAuthenticatedMember().memberId();
    }

    public static String getCurrentRole() {
        return getAuthenticatedMember().role();
    }

    public static boolean isAuthenticated() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return isAuthenticated(authentication);
    }

    private static boolean isAuthenticated(
            Authentication authentication
    ) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication
                instanceof AnonymousAuthenticationToken);
    }
}

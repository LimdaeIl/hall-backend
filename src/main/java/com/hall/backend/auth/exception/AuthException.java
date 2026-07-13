package com.hall.backend.auth.exception;

import com.hall.backend.common.exception.CommonException;

public class AuthException extends CommonException {

    public AuthException(AuthErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(AuthErrorCode errorCode, Object... arguments) {
        super(errorCode, arguments);
    }

}
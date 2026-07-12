package com.hall.backend.member.exception;

import com.hall.backend.common.exception.CommonException;
import com.hall.backend.common.exception.ErrorCode;

public class MemberException extends CommonException {

    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MemberException(ErrorCode errorCode, Object... arguments) {
        super(errorCode, arguments);
    }
}

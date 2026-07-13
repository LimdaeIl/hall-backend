package com.hall.backend.concert.exception;

import com.hall.backend.common.exception.CommonException;
import com.hall.backend.common.exception.ErrorCode;

public class ConcertException extends CommonException {

    public ConcertException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ConcertException(ErrorCode errorCode, Object... arguments) {
        super(errorCode, arguments);
    }
}

package com.hall.backend.performance.exception;

import com.hall.backend.common.exception.CommonException;
import com.hall.backend.common.exception.ErrorCode;

public class PerformanceException extends CommonException {

    public PerformanceException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PerformanceException(ErrorCode errorCode, Object... arguments) {
        super(errorCode, arguments);
    }
}

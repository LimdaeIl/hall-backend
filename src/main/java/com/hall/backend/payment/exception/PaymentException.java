package com.hall.backend.payment.exception;

import com.hall.backend.common.exception.CommonException;
import com.hall.backend.common.exception.ErrorCode;

public class PaymentException extends CommonException {

    public PaymentException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PaymentException(ErrorCode errorCode, Object... arguments) {
        super(errorCode, arguments);
    }
}

package com.hall.backend.reservation.exception;

import com.hall.backend.common.exception.CommonException;
import com.hall.backend.common.exception.ErrorCode;

public class ReservationException  extends CommonException {

    public ReservationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ReservationException(ErrorCode errorCode, Object... arguments) {
        super(errorCode, arguments);
    }
}

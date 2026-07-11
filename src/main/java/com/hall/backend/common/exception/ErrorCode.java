package com.hall.backend.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    HttpStatus status();

    String message();

    default String format(Object... arguments) {
        if (arguments == null || arguments.length == 0) {
            return message();
        }

        return message().formatted(arguments);
    }
}

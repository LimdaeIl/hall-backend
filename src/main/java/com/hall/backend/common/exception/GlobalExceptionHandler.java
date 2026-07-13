package com.hall.backend.common.exception;

import com.hall.backend.common.response.ErrorResponse;
import com.hall.backend.common.response.ErrorResponse.FieldError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> handleCommonException(
            CommonException exception,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = exception.getErrorCode();

        log.warn(
                "비즈니스 예외 발생. type={}, status={}, method={}, path={}, message={}",
                errorCode,
                errorCode.status().value(),
                request.getMethod(),
                request.getRequestURI(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(errorCode.status())
                .body(ErrorResponse.of(
                        errorCode,
                        exception.getMessage(),
                        request
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<FieldError> errors = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> FieldError.of(
                        error.getField(),
                        error.getDefaultMessage() == null
                                ? "요청 값이 올바르지 않습니다."
                                : error.getDefaultMessage()
                ))
                .distinct()
                .sorted(Comparator.comparing(FieldError::field))
                .toList();

        return badRequest(
                CommonErrorCode.INVALID_INPUT_VALUE,
                request,
                errors
        );
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException exception,
            HttpServletRequest request
    ) {
        List<FieldError> errors = exception
                .getConstraintViolations()
                .stream()
                .map(violation -> FieldError.of(
                        extractFieldName(
                                violation.getPropertyPath().toString()
                        ),
                        violation.getMessage()
                ))
                .distinct()
                .sorted(Comparator.comparing(FieldError::field))
                .toList();

        return badRequest(
                CommonErrorCode.INVALID_INPUT_VALUE,
                request,
                errors
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestParameterException(
            MissingServletRequestParameterException exception,
            HttpServletRequest request
    ) {
        List<FieldError> errors = List.of(
                FieldError.of(
                        exception.getParameterName(),
                        "필수 요청 파라미터입니다."
                )
        );

        return badRequest(
                CommonErrorCode.MISSING_REQUEST_PARAMETER,
                request,
                errors
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request
    ) {
        List<FieldError> errors = List.of(
                FieldError.of(
                        exception.getName(),
                        "요청 값의 타입이 올바르지 않습니다."
                )
        );

        return badRequest(
                CommonErrorCode.TYPE_MISMATCH,
                request,
                errors
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(
                        CommonErrorCode.INVALID_JSON_FORMAT,
                        request
                ));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(CommonErrorCode.METHOD_NOT_ALLOWED.status())
                .body(ErrorResponse.of(
                        CommonErrorCode.METHOD_NOT_ALLOWED,
                        request
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(CommonErrorCode.ACCESS_DENIED.status())
                .body(ErrorResponse.of(
                        CommonErrorCode.ACCESS_DENIED,
                        request
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request
    ) {
        log.error(
                "처리되지 않은 예외 발생. method={}, path={}",
                request.getMethod(),
                request.getRequestURI(),
                exception
        );

        return ResponseEntity
                .status(CommonErrorCode.INTERNAL_SERVER_ERROR.status())
                .body(ErrorResponse.of(
                        CommonErrorCode.INTERNAL_SERVER_ERROR,
                        request
                ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception,
            HttpServletRequest request
    ) {
        log.warn(
                "데이터 제약조건 위반. method={}, path={}, message={}",
                request.getMethod(),
                request.getRequestURI(),
                exception.getMostSpecificCause().getMessage()
        );

        return ResponseEntity
                .status(CommonErrorCode.DATA_INTEGRITY_VIOLATION.status())
                .body(ErrorResponse.of(
                        CommonErrorCode.DATA_INTEGRITY_VIOLATION,
                        request
                ));
    }

    private ResponseEntity<ErrorResponse> badRequest(
            ErrorCode errorCode,
            HttpServletRequest request,
            List<FieldError> errors
    ) {
        return ResponseEntity
                .status(errorCode.status())
                .body(ErrorResponse.withErrors(
                        errorCode,
                        request,
                        errors
                ));
    }

    private String extractFieldName(String propertyPath) {
        int lastDotIndex = propertyPath.lastIndexOf('.');

        if (lastDotIndex < 0) {
            return propertyPath;
        }

        return propertyPath.substring(lastDotIndex + 1);
    }
}

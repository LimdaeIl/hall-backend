package com.hall.backend.concert.exception;

import com.hall.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
public enum ConcertErrorCode implements ErrorCode {
    TITLE_CANNOT_BE_NULL_OR_BLANK(HttpStatus.BAD_REQUEST, "제목은 null 또는 공백일 수 없습니다."),
    ARTIST_CANNOT_BE_NULL_OR_BLANK(HttpStatus.BAD_REQUEST, "아티스트는 null 또는 공백일 수 없습니다."),
    TITLE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 제목입니다."),
    SEAT_NUMBER_REQUIRED(HttpStatus.BAD_REQUEST, "좌석 번호는 필수입니다."),
    SEAT_NUMBER_TOO_LONG(HttpStatus.BAD_REQUEST, "좌석 번호는 20자를 초과할 수 없습니다."),
    SEAT_GRADE_REQUIRED(HttpStatus.BAD_REQUEST, "좌석 등급은 필수입니다."),
    SEAT_ROW_NUMBER_INVALID(HttpStatus.BAD_REQUEST, "좌석 행 번호는 1 이상이어야 합니다."),
    SEAT_COLUMN_NUMBER_INVALID(HttpStatus.BAD_REQUEST, "좌석 열 번호는 1 이상이어야 합니다."),
    INVALID_SEAT_GRADE(HttpStatus.BAD_REQUEST, "유효하지 않은 좌석 등급입니다."),
    SEAT_NUMBER_DUPLICATED(HttpStatus.BAD_REQUEST, "중복된 좌석 번호입니다."),
    SEAT_POSITION_DUPLICATED(HttpStatus.BAD_REQUEST, "중복된 좌석 위치입니다."
    ), SEAT_GRADE_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 좌석 등급입니다."),
    SEAT_DUPLICATED(HttpStatus.BAD_REQUEST, "중복된 좌석입니다."),
    DUPLICATE_SEAT_NUMBER(HttpStatus.BAD_REQUEST, "중복된 좌석 번호입니다."),
    DUPLICATE_SEAT_POSITION(HttpStatus.BAD_REQUEST, "중복된 좌석 위치입니다."),
    INVALID_PAGE_NUMBER(HttpStatus.BAD_REQUEST, "페이지 번호는 1 이상이어야 합니다."),
    INVALID_PAGE_SIZE(HttpStatus.BAD_REQUEST, "페이지 크기는 1 이상이어야 합니다."),
    DESCRIPTION_TOO_LONG(HttpStatus.BAD_REQUEST, "설명은 1000자를 초과할 수 없습니다."),
    NO_CONCERT_UPDATE_FIELDS(HttpStatus.BAD_REQUEST, "업데이트할 공연 필드가 없습니다."),
    CONCERT_NOT_FOUND(HttpStatus.NOT_FOUND, "공연을 찾을 수 없습니다."),
    DUPLICATE_CONCERT_TITLE(HttpStatus.BAD_REQUEST, "이미 존재하는 공연 제목입니다."),
    INVALID_CONCERT_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "유효하지 않은 공연 상태 전환입니다."),
    CONCERT_STATUS_REQUIRED(HttpStatus.BAD_REQUEST, "공연 상태는 필수입니다."),
    CONCERT_STATUS_NOT_CHANGED(HttpStatus.BAD_REQUEST, "공연 상태가 변경되지 않았습니다."),
    CONCERT_HAS_RESERVATIONS(HttpStatus.CONFLICT, "예약 이력이 존재하는 콘서트는 삭제할 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus status() {
        return status;
    }

    @Override
    public String message() {
        return message;
    }
}

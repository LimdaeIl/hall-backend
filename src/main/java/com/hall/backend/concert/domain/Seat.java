package com.hall.backend.concert.domain;

import com.hall.backend.common.persistence.entity.BaseAuditEntity;
import com.hall.backend.concert.exception.ConcertErrorCode;
import com.hall.backend.concert.exception.ConcertException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "v1_seats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_v1_seats_seat_number",
                        columnNames = "seat_number"
                ),
                @UniqueConstraint(
                        name = "uk_v1_seats_position",
                        columnNames = {
                                "seat_row",
                                "seat_column"
                        }
                )
        }
)
@Entity
public class Seat extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_number", nullable = false, length = 20)
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade", nullable = false)
    private SeatGrade grade;

    @Column(name = "seat_row", nullable = false)
    private int rowNumber;

    @Column(name = "seat_column", nullable = false)
    private int columnNumber;

    private Seat(String seatNumber, SeatGrade grade, int rowNumber, int columnNumber) {
        String normalizedSeatNumber = normalizeSeatNumber(seatNumber);
        validateSeatNumber(normalizedSeatNumber);
        validateGrade(grade);
        validatePosition(rowNumber, columnNumber);

        this.seatNumber = normalizedSeatNumber;
        this.grade = grade;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
    }

    public static Seat create(String seatNumber, SeatGrade grade, int rowNumber, int columnNumber) {
        return new Seat(seatNumber, grade, rowNumber, columnNumber);
    }

    public void updateLayout(String seatNumber, Integer rowNumber, Integer columnNumber) {
        if (seatNumber != null) {
            String normalizedSeatNumber = normalizeSeatNumber(seatNumber);
            validateSeatNumber(normalizedSeatNumber);
            this.seatNumber = normalizedSeatNumber;
        }

        int updatedRowNumber = rowNumber == null
                        ? this.rowNumber
                        : rowNumber;

        int updatedColumnNumber = columnNumber == null
                        ? this.columnNumber
                        : columnNumber;

        validatePosition(updatedRowNumber, updatedColumnNumber);

        this.rowNumber = updatedRowNumber;
        this.columnNumber = updatedColumnNumber;
    }

    private static String normalizeSeatNumber(String seatNumber) {
        if (seatNumber == null) {
            return null;
        }

        return seatNumber.trim().toUpperCase(Locale.ROOT);
    }

    private static void validateSeatNumber(String seatNumber) {
        if (seatNumber == null || seatNumber.isBlank()) {
            throw new ConcertException(ConcertErrorCode.SEAT_NUMBER_REQUIRED);
        }

        if (seatNumber.length() > 20) {
            throw new ConcertException(ConcertErrorCode.SEAT_NUMBER_TOO_LONG);
        }
    }

    private static void validateGrade(SeatGrade grade) {
        if (grade == null) {
            throw new ConcertException(ConcertErrorCode.SEAT_GRADE_REQUIRED);
        }
    }

    private static void validatePosition(int rowNumber, int columnNumber) {
        if (rowNumber <= 0) {
            throw new ConcertException(ConcertErrorCode.SEAT_ROW_NUMBER_INVALID);
        }

        if (columnNumber <= 0) {
            throw new ConcertException(ConcertErrorCode.SEAT_COLUMN_NUMBER_INVALID);
        }
    }
}

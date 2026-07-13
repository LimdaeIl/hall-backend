package com.hall.backend.concert.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
                        name = "uk_v1_seats_row_column",
                        columnNames = {"row_number", "column_number"}
                )
        }
)
@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_number", nullable = false, length = 20)
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade", nullable = false, length = 20)
    private SeatGrade grade;

    @Column(name = "row_number", nullable = false)
    private int rowNumber;

    @Column(name = "column_number", nullable = false)
    private int columnNumber;

    private Seat(String seatNumber, SeatGrade grade, int rowNumber, int columnNumber) {
        validateSeatNumber(seatNumber);
        validateGrade(grade);
        validatePosition(rowNumber, columnNumber);

        this.seatNumber = seatNumber;
        this.grade = grade;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
    }

    public static Seat create(String seatNumber, SeatGrade grade, int rowNumber, int columnNumber) {
        return new Seat(seatNumber, grade, rowNumber, columnNumber);
    }

    private static void validateSeatNumber(String seatNumber) {
        if (seatNumber == null || seatNumber.isBlank()) {
            throw new IllegalArgumentException("좌석 번호는 비어 있을 수 없습니다.");
        }

        if (seatNumber.length() > 20) {
            throw new IllegalArgumentException("좌석 번호는 20자를 초과할 수 없습니다.");
        }
    }

    private static void validateGrade(SeatGrade grade) {
        if (grade == null) {
            throw new IllegalArgumentException("좌석 등급은 필수입니다.");
        }
    }

    private static void validatePosition(int rowNumber, int columnNumber) {
        if (rowNumber <= 0) {
            throw new IllegalArgumentException("좌석 행 번호는 1 이상이어야 합니다.");
        }

        if (columnNumber <= 0) {
            throw new IllegalArgumentException("좌석 열 번호는 1 이상이어야 합니다.");
        }
    }
}

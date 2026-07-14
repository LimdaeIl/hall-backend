package com.hall.backend.reservation.domain;

import com.hall.backend.concert.domain.SeatGrade;
import com.hall.backend.performance.domain.PerformanceSeat;
import com.hall.backend.reservation.exception.ReservationErrorCode;
import com.hall.backend.reservation.exception.ReservationException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "v1_reservation_seats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_reservation_performance_seat",
                        columnNames = {
                                "reservation_id",
                                "performance_seat_id"
                        }
                )
        }
)
@Entity
public class ReservationSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "performance_seat_id", nullable = false)
    private PerformanceSeat performanceSeat;

    @Column(
            name = "seat_number",
            nullable = false,
            length = 20
    )
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeatGrade grade;

    @Column(nullable = false)
    private long price;

    private ReservationSeat(Reservation reservation, PerformanceSeat performanceSeat) {
        validateReservation(reservation);
        validatePerformanceSeat(performanceSeat);

        this.reservation = reservation;
        this.performanceSeat = performanceSeat;
        this.seatNumber = performanceSeat.getSeat().getSeatNumber();
        this.grade = performanceSeat.getGrade();
        this.price = performanceSeat.getPrice();
    }

    static ReservationSeat create(Reservation reservation, PerformanceSeat performanceSeat) {
        return new ReservationSeat(reservation, performanceSeat);
    }

    void complete() {
        performanceSeat.reserve();
    }

    void release() {
        performanceSeat.release();
    }

    private static void validateReservation(Reservation reservation) {
        if (reservation == null) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_REQUIRED);
        }
    }

    private static void validatePerformanceSeat(PerformanceSeat performanceSeat) {
        if (performanceSeat == null) {
            throw new ReservationException(ReservationErrorCode.PERFORMANCE_SEAT_REQUIRED);
        }
    }
}

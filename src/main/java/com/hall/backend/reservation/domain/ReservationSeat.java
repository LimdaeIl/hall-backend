package com.hall.backend.reservation.domain;

import com.hall.backend.performance.domain.PerformanceSeat;
import com.hall.backend.reservation.exception.ReservationErrorCode;
import com.hall.backend.reservation.exception.ReservationException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "v1_reservation_seats")
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

    @Column(name = "price", nullable = false)
    private long price;

    private ReservationSeat(Reservation reservation, PerformanceSeat performanceSeat) {
        validateReservation(reservation);
        validatePerformanceSeat(performanceSeat);

        this.reservation = reservation;
        this.performanceSeat = performanceSeat;
        this.price = performanceSeat.getPrice();
    }

    public static ReservationSeat create(Reservation reservation, PerformanceSeat performanceSeat) {
        return new ReservationSeat(reservation, performanceSeat);
    }

    public void complete() {
        performanceSeat.reserve();
    }

    public void cancel() {
        performanceSeat.cancelReservation();
    }

    private static void validateReservation(Reservation reservation) {
        if (reservation == null) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_REQUIRED);
        }
    }

    private static void validatePerformanceSeat(PerformanceSeat performanceSeat) {
        if (performanceSeat == null) {
            throw new ReservationException(ReservationErrorCode.SEAT_REQUIRED);
        }
    }
}

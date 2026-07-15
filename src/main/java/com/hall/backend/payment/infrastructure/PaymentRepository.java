package com.hall.backend.payment.infrastructure;

import com.hall.backend.payment.domain.Payment;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByReservationId(Long reservationId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT p
            FROM Payment p
            JOIN FETCH p.reservation
            WHERE p.reservation.id = :reservationId
            """)
    Optional<Payment> findByReservationIdForUpdate(
            @Param("reservationId") Long reservationId
    );
}


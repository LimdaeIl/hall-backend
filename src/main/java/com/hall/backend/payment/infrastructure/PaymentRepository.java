package com.hall.backend.payment.infrastructure;

import com.hall.backend.payment.domain.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    Optional<Payment> findByReservationId(Long reservationId);

    boolean existsByReservationId(Long reservationId);

    Optional<Payment> findByTransactionKey(String transactionKey);
}
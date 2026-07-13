package com.hall.backend.concert.infrastructure;

import com.hall.backend.concert.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {

}

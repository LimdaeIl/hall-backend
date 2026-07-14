package com.hall.backend.concert.infrastructure;

import com.hall.backend.concert.domain.Seat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    boolean existsBySeatNumberIn(List<String> seatNumbers);

    boolean existsByRowNumberAndColumnNumber(int rowNumber, int columnNumber);

}

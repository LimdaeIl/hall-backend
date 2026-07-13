package com.hall.backend.performance.infrastructure;

import com.hall.backend.performance.domain.Performance;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    boolean existsByConcertIdAndStartsAt(Long concertId, LocalDateTime startsAt);
}

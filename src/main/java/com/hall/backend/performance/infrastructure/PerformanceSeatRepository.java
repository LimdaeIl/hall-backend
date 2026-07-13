package com.hall.backend.performance.infrastructure;

import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceSeatRepository extends JpaRepository<PerformanceSeat, Long> {

}

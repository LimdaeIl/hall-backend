package com.hall.backend.performance.infrastructure.projection;

import com.hall.backend.performance.domain.PerformanceStatus;
import java.time.LocalDateTime;

public interface ConcertPerformanceProjection {

    Long getPerformanceId();

    LocalDateTime getStartsAt();

    LocalDateTime getReservationOpensAt();

    LocalDateTime getReservationClosesAt();

    Integer getMaxTicketsPerMember();

    PerformanceStatus getStatus();

    Long getAvailableSeatCount();
}

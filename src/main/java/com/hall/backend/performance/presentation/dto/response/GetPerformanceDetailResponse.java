package com.hall.backend.performance.presentation.dto.response;

import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceSeat;
import com.hall.backend.performance.domain.PerformanceSeatStatus;
import com.hall.backend.performance.domain.PerformanceStatus;
import java.time.LocalDateTime;
import java.util.List;

public record GetPerformanceDetailResponse(
        Long performanceId,
        ConcertResponse concert,
        LocalDateTime startsAt,
        LocalDateTime reservationOpensAt,
        LocalDateTime reservationClosesAt,
        int maxTicketsPerMember,
        PerformanceStatus status,
        SeatSummary seatSummary
) {

    public static GetPerformanceDetailResponse of(Performance performance,
            List<PerformanceSeat> performanceSeats) {
        return new GetPerformanceDetailResponse(
                performance.getId(),
                ConcertResponse.from(performance),
                performance.getStartsAt(),
                performance.getReservationOpensAt(),
                performance.getReservationClosesAt(),
                performance.getMaxTicketsPerMember(),
                performance.getStatus(),
                SeatSummary.from(performanceSeats)
        );
    }

    public record ConcertResponse(
            Long concertId,
            String title,
            String artist,
            String description,
            String status
    ) {

        private static ConcertResponse from(Performance performance) {
            var concert = performance.getConcert();

            return new ConcertResponse(
                    concert.getId(),
                    concert.getTitle(),
                    concert.getArtist(),
                    concert.getDescription(),
                    concert.getStatus().name()
            );
        }
    }

    public record SeatSummary(
            int totalSeatCount,
            long availableSeatCount,
            long heldSeatCount,
            long reservedSeatCount,
            long blockedSeatCount
    ) {

        private static SeatSummary from(List<PerformanceSeat> performanceSeats) {
            return new SeatSummary(
                    performanceSeats.size(),
                    count(performanceSeats, PerformanceSeatStatus.AVAILABLE),
                    count(performanceSeats, PerformanceSeatStatus.HELD),
                    count(performanceSeats, PerformanceSeatStatus.RESERVED),
                    count(performanceSeats, PerformanceSeatStatus.BLOCKED)
            );
        }

        private static long count(List<PerformanceSeat> performanceSeats,
                PerformanceSeatStatus status) {
            return performanceSeats.stream()
                    .filter(performanceSeat -> performanceSeat.getStatus() == status).count();
        }
    }
}

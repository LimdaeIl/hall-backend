package com.hall.backend.performance.application;

import com.hall.backend.concert.domain.Concert;
import com.hall.backend.concert.domain.Seat;
import com.hall.backend.concert.domain.SeatGrade;
import com.hall.backend.concert.infrastructure.ConcertRepository;
import com.hall.backend.concert.infrastructure.SeatRepository;
import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceSeat;
import com.hall.backend.performance.exception.PerformanceErrorCode;
import com.hall.backend.performance.exception.PerformanceException;
import com.hall.backend.performance.infrastructure.PerformanceRepository;
import com.hall.backend.performance.infrastructure.PerformanceSeatRepository;
import com.hall.backend.performance.presentation.dto.request.CreatePerformanceRequest;
import com.hall.backend.performance.presentation.dto.response.CreatePerformanceResponse;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreatePerformanceService {

    private final ConcertRepository concertRepository;
    private final PerformanceRepository performanceRepository;
    private final SeatRepository seatRepository;
    private final PerformanceSeatRepository performanceSeatRepository;

    @Transactional
    public CreatePerformanceResponse create(
            CreatePerformanceRequest request
    ) {
        Concert concert = concertRepository
                .findById(request.concertId())
                .orElseThrow(() ->
                        new PerformanceException(
                                PerformanceErrorCode.CONCERT_NOT_FOUND
                        )
                );

        validateDuplicatedPerformance(request);

        Map<SeatGrade, Long> priceByGrade =
                createPriceByGrade(request);

        List<Seat> seats = seatRepository.findAll();

        if (seats.isEmpty()) {
            throw new PerformanceException(
                    PerformanceErrorCode.SEAT_NOT_FOUND
            );
        }

        validateSeatGradePrices(
                seats,
                priceByGrade
        );

        Performance performance = Performance.create(
                concert,
                request.startsAt(),
                request.reservationOpensAt(),
                request.reservationClosesAt(),
                request.maxTicketsPerMember()
        );

        Performance savedPerformance =
                performanceRepository.save(performance);

        List<PerformanceSeat> performanceSeats =
                seats.stream()
                        .map(seat ->
                                PerformanceSeat.create(
                                        savedPerformance,
                                        seat,
                                        priceByGrade.get(
                                                seat.getGrade()
                                        )
                                )
                        )
                        .toList();

        performanceSeatRepository.saveAll(performanceSeats);

        return CreatePerformanceResponse.of(
                savedPerformance,
                performanceSeats.size()
        );
    }

    private void validateDuplicatedPerformance(
            CreatePerformanceRequest request
    ) {
        boolean exists =
                performanceRepository
                        .existsByConcertIdAndStartsAt(
                                request.concertId(),
                                request.startsAt()
                        );

        if (exists) {
            throw new PerformanceException(
                    PerformanceErrorCode
                            .PERFORMANCE_ALREADY_EXISTS
            );
        }
    }

    private Map<SeatGrade, Long> createPriceByGrade(
            CreatePerformanceRequest request
    ) {
        Map<SeatGrade, Long> priceByGrade =
                new EnumMap<>(SeatGrade.class);

        Set<SeatGrade> requestedGrades =
                new HashSet<>();

        request.seatPrices().forEach(seatPrice -> {
            SeatGrade grade =
                    SeatGrade.from(seatPrice.grade());

            if (!requestedGrades.add(grade)) {
                throw new PerformanceException(
                        PerformanceErrorCode
                                .DUPLICATE_SEAT_GRADE_PRICE
                );
            }

            priceByGrade.put(
                    grade,
                    seatPrice.price()
            );
        });

        return priceByGrade;
    }

    private void validateSeatGradePrices(
            List<Seat> seats,
            Map<SeatGrade, Long> priceByGrade
    ) {
        Set<SeatGrade> requiredGrades =
                seats.stream()
                        .map(Seat::getGrade)
                        .collect(
                                java.util.stream.Collectors.toSet()
                        );

        if (!priceByGrade.keySet()
                .containsAll(requiredGrades)) {
            throw new PerformanceException(
                    PerformanceErrorCode
                            .SEAT_GRADE_PRICE_REQUIRED
            );
        }
    }
}

package com.hall.backend.performance.application;

import static java.util.stream.Collectors.toSet;

import com.hall.backend.concert.domain.Seat;
import com.hall.backend.concert.domain.SeatGrade;
import com.hall.backend.concert.infrastructure.SeatRepository;
import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceSeat;
import com.hall.backend.performance.exception.PerformanceErrorCode;
import com.hall.backend.performance.exception.PerformanceException;
import com.hall.backend.performance.infrastructure.PerformanceRepository;
import com.hall.backend.performance.infrastructure.PerformanceSeatRepository;
import com.hall.backend.performance.presentation.dto.request.CreatePerformanceSeatsRequest;
import com.hall.backend.performance.presentation.dto.response.CreatePerformanceSeatsResponse;
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
public class CreatePerformanceSeatsService {

    private final PerformanceRepository performanceRepository;
    private final PerformanceSeatRepository performanceSeatRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public CreatePerformanceSeatsResponse create(Long performanceId,
            CreatePerformanceSeatsRequest request) {
        validatePerformanceId(performanceId);

        Performance performance =
                performanceRepository.findById(performanceId)
                        .orElseThrow(() -> new PerformanceException(
                                PerformanceErrorCode.PERFORMANCE_NOT_FOUND));

        validatePerformanceSeatsNotCreated(performanceId);
        Map<SeatGrade, Long> priceByGrade = createPriceByGrade(request);
        List<Seat> seats = seatRepository.findAll();

        if (seats.isEmpty()) {
            throw new PerformanceException(PerformanceErrorCode.NO_AVAILABLE_BASE_SEATS);
        }
        validateSeatGradePrices(seats, priceByGrade);

        List<PerformanceSeat> performanceSeats = seats.stream()
                .map(
                        seat -> PerformanceSeat.create(performance, seat,
                                priceByGrade.get(seat.getGrade())
                        )
                )
                .toList();

        List<PerformanceSeat> savedPerformanceSeats = performanceSeatRepository.saveAllAndFlush(performanceSeats);

        return CreatePerformanceSeatsResponse.of(performance, savedPerformanceSeats);
    }

    private void validatePerformanceSeatsNotCreated(Long performanceId) {
        boolean alreadyExists = performanceSeatRepository.existsByPerformanceId(performanceId);

        if (alreadyExists) {
            throw new PerformanceException(PerformanceErrorCode.PERFORMANCE_SEATS_ALREADY_EXIST);
        }
    }

    private Map<SeatGrade, Long> createPriceByGrade(CreatePerformanceSeatsRequest request) {
        Map<SeatGrade, Long> priceByGrade = new EnumMap<>(SeatGrade.class);

        Set<SeatGrade> requestedGrades = new HashSet<>();

        request.seatPrices().forEach(
                seatPrice -> {
                    SeatGrade grade = SeatGrade.from(seatPrice.grade());

                    if (!requestedGrades.add(grade)) {
                        throw new PerformanceException(PerformanceErrorCode.DUPLICATE_SEAT_GRADE_PRICE);
                    }
                    priceByGrade.put(grade, seatPrice.price());
                }
        );

        return priceByGrade;
    }

    private void validateSeatGradePrices(List<Seat> seats, Map<SeatGrade, Long> priceByGrade) {
        Set<SeatGrade> requiredGrades = seats.stream()
                        .map(Seat::getGrade)
                        .collect(toSet());

        if (!priceByGrade.keySet().containsAll(requiredGrades)) {
            throw new PerformanceException(PerformanceErrorCode.SEAT_GRADE_PRICE_REQUIRED);
        }
    }

    private void validatePerformanceId(Long performanceId) {
        if (performanceId == null || performanceId <= 0) {
            throw new PerformanceException(PerformanceErrorCode.PERFORMANCE_NOT_FOUND);
        }
    }
}

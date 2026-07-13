package com.hall.backend.reservation.application;

import com.hall.backend.auth.infrastructure.security.SecurityUtils;
import com.hall.backend.member.domain.Member;
import com.hall.backend.member.infrastructure.jpa.MemberRepository;
import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceSeat;
import com.hall.backend.performance.infrastructure.PerformanceRepository;
import com.hall.backend.performance.infrastructure.PerformanceSeatRepository;
import com.hall.backend.reservation.domain.Reservation;
import com.hall.backend.reservation.infrastructure.ReservationRepository;
import com.hall.backend.reservation.presentation.dto.request.CreateReservationRequest;
import com.hall.backend.reservation.presentation.dto.response.CreateReservationResponse;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateReservationService {

    private static final int MAX_SEAT_COUNT = 4;
    private static final int RESERVATION_HOLD_MINUTES = 5;

    private final MemberRepository memberRepository;
    private final PerformanceRepository performanceRepository;
    private final PerformanceSeatRepository performanceSeatRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationNumberGenerator reservationNumberGenerator;
    private final Clock clock;

    @Transactional
    public CreateReservationResponse create(CreateReservationRequest request) {
        Long memberId = SecurityUtils.getCurrentMemberId();

        LocalDateTime now = LocalDateTime.now(clock);

        Member member = getMember(memberId);
        Performance performance = getPerformance(request.performanceId());
        validatePerformanceReservable(performance, now);

        List<Long> seatIds = validateAndSortSeatIds(request.performanceSeatIds());

        List<PerformanceSeat> performanceSeats = performanceSeatRepository.findAllByIdInForUpdate(
                seatIds);

        validateSeatCount(seatIds, performanceSeats);
        validateSeatsBelongToPerformance(performance.getId(), performanceSeats);
        validateAllSeatsAvailable(performanceSeats);

        String reservationNumber = reservationNumberGenerator.generate();

        LocalDateTime expiredAt = now.plusMinutes(RESERVATION_HOLD_MINUTES);

        Reservation reservation = Reservation.create(
                reservationNumber,
                member,
                performance,
                expiredAt
        );

        for (PerformanceSeat performanceSeat : performanceSeats) {
            performanceSeat.hold();
            reservation.addSeat(performanceSeat);
        }

        Reservation savedReservation = reservationRepository.save(reservation);

        return CreateReservationResponse.from(savedReservation);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

    private Performance getPerformance(
            Long performanceId
    ) {
        return performanceRepository
                .findById(performanceId)
                .orElseThrow(() -> new IllegalArgumentException("공연을 찾을 수 없습니다."));
    }

    private void validatePerformanceReservable(Performance performance, LocalDateTime now) {
        if (!performance.isReservable(now)) {
            throw new IllegalStateException("현재 예약할 수 없는 공연입니다.");
        }
    }

    private List<Long> validateAndSortSeatIds(
            List<Long> requestedSeatIds
    ) {
        if (requestedSeatIds == null
                || requestedSeatIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "예약할 좌석을 선택해 주세요."
            );
        }

        if (requestedSeatIds.size() > MAX_SEAT_COUNT) {
            throw new IllegalArgumentException(
                    "한 예약당 최대 "
                            + MAX_SEAT_COUNT
                            + "개 좌석까지 예약할 수 있습니다."
            );
        }

        if (requestedSeatIds.stream()
                .anyMatch(id -> id == null)) {
            throw new IllegalArgumentException(
                    "좌석 ID는 null일 수 없습니다."
            );
        }

        Set<Long> uniqueSeatIds =
                new HashSet<>(requestedSeatIds);

        if (uniqueSeatIds.size()
                != requestedSeatIds.size()) {
            throw new IllegalArgumentException(
                    "중복된 좌석이 포함되어 있습니다."
            );
        }

        return requestedSeatIds.stream()
                .sorted()
                .toList();
    }

    private void validateSeatCount(
            List<Long> requestedSeatIds,
            List<PerformanceSeat> performanceSeats
    ) {
        if (performanceSeats.size()
                != requestedSeatIds.size()) {
            throw new IllegalArgumentException(
                    "존재하지 않는 공연 좌석이 포함되어 있습니다."
            );
        }
    }

    private void validateSeatsBelongToPerformance(
            Long performanceId,
            List<PerformanceSeat> performanceSeats
    ) {
        boolean containsOtherPerformance =
                performanceSeats.stream()
                        .anyMatch(performanceSeat ->
                                !performanceId.equals(
                                        performanceSeat
                                                .getPerformance()
                                                .getId()
                                )
                        );

        if (containsOtherPerformance) {
            throw new IllegalArgumentException(
                    "동일한 공연의 좌석만 예약할 수 있습니다."
            );
        }
    }

    private void validateAllSeatsAvailable(
            List<PerformanceSeat> performanceSeats
    ) {
        boolean containsUnavailableSeat =
                performanceSeats.stream()
                        .anyMatch(
                                performanceSeat ->
                                        !performanceSeat
                                                .isAvailable()
                        );

        if (containsUnavailableSeat) {
            throw new IllegalStateException(
                    "이미 예약되었거나 예약할 수 없는 좌석이 포함되어 있습니다."
            );
        }
    }
}

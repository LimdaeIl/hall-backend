package com.hall.backend.reservation.application;

import com.hall.backend.auth.infrastructure.security.MemberPrincipal;
import com.hall.backend.member.domain.Member;
import com.hall.backend.member.exception.MemberErrorCode;
import com.hall.backend.member.exception.MemberException;
import com.hall.backend.member.infrastructure.jpa.MemberRepository;
import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceSeat;
import com.hall.backend.performance.exception.PerformanceErrorCode;
import com.hall.backend.performance.exception.PerformanceException;
import com.hall.backend.performance.infrastructure.PerformanceRepository;
import com.hall.backend.performance.infrastructure.PerformanceSeatRepository;
import com.hall.backend.reservation.domain.Reservation;
import com.hall.backend.reservation.domain.ReservationSeat;
import com.hall.backend.reservation.domain.ReservationStatus;
import com.hall.backend.reservation.exception.ReservationErrorCode;
import com.hall.backend.reservation.exception.ReservationException;
import com.hall.backend.reservation.infrastructure.ReservationRepository;
import com.hall.backend.reservation.presentation.dto.request.CreateReservationRequest;
import com.hall.backend.reservation.presentation.dto.response.CreateReservationResponse;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateReservationService {

    private static final int MAX_SEAT_COUNT = 4;
    private static final long PAYMENT_TIMEOUT_MINUTES = 10;

    private static final List<ReservationStatus> COUNTED_STATUSES =
            List.of(
                    ReservationStatus.PENDING_PAYMENT,
                    ReservationStatus.COMPLETED
            );

    private final MemberRepository memberRepository;
    private final PerformanceRepository performanceRepository;
    private final PerformanceSeatRepository performanceSeatRepository;
    private final ReservationRepository reservationRepository;
    private final Clock clock;

    @Transactional
    public CreateReservationResponse create(
            Long performanceId,
            MemberPrincipal principal,
            CreateReservationRequest request
    ) {
        List<Long> performanceSeatIds =
                validateAndSortSeatIds(
                        request.performanceSeatIds()
                );

        Member member = memberRepository.findByIdForUpdate(
                        principal.memberId()
                )
                .orElseThrow(() ->
                        new MemberException(
                                MemberErrorCode.MEMBER_NOT_FOUND
                        )
                );

        Performance performance =
                performanceRepository.findById(performanceId)
                        .orElseThrow(() ->
                                new PerformanceException(
                                        PerformanceErrorCode
                                                .PERFORMANCE_NOT_FOUND
                                )
                        );

        LocalDateTime now = LocalDateTime.now(clock);

        performance.validateReservable(now);

        validateReservationLimit(
                performance,
                member.getId(),
                performanceSeatIds.size()
        );

        List<PerformanceSeat> performanceSeats =
                performanceSeatRepository
                        .findAllByIdInForUpdate(
                                performanceSeatIds
                        );

        validatePerformanceSeats(
                performanceId,
                performanceSeatIds,
                performanceSeats
        );

        LocalDateTime expiresAt =
                now.plusMinutes(PAYMENT_TIMEOUT_MINUTES);

        Reservation reservation = Reservation.create(
                member,
                performance,
                expiresAt
        );

        performanceSeats.forEach(performanceSeat -> {
            performanceSeat.hold();

            ReservationSeat reservationSeat =
                    ReservationSeat.create(
                            reservation,
                            performanceSeat
                    );

            reservation.addSeat(reservationSeat);
        });

        Reservation savedReservation =
                reservationRepository.save(reservation);

        return CreateReservationResponse.from(savedReservation);
    }

    private List<Long> validateAndSortSeatIds(
            List<Long> performanceSeatIds
    ) {
        if (performanceSeatIds == null
                || performanceSeatIds.isEmpty()) {
            throw new ReservationException(
                    ReservationErrorCode.SEAT_REQUIRED
            );
        }

        if (performanceSeatIds.size() > MAX_SEAT_COUNT) {
            throw new ReservationException(
                    ReservationErrorCode.RESERVATION_LIMIT_EXCEEDED
            );
        }

        if (performanceSeatIds.stream()
                .anyMatch(id -> id == null || id <= 0)) {
            throw new ReservationException(
                    ReservationErrorCode.INVALID_SEAT_ID
            );
        }

        if (new HashSet<>(performanceSeatIds).size()
                != performanceSeatIds.size()) {
            throw new ReservationException(
                    ReservationErrorCode.DUPLICATE_SEAT
            );
        }

        return performanceSeatIds.stream()
                .sorted()
                .toList();
    }

    private void validateSeatLimit(
            Long memberId,
            Long performanceId,
            int requestedSeatCount
    ) {
        long existingSeatCount =
                reservationRepository
                        .countSeatsByMemberAndPerformance(
                                memberId,
                                performanceId,
                                COUNTED_STATUSES
                        );

        if (existingSeatCount + requestedSeatCount
                > MAX_SEAT_COUNT) {
            throw new ReservationException(
                    ReservationErrorCode
                            .RESERVATION_LIMIT_EXCEEDED
            );
        }
    }

    private void validateReservationLimit(
            Performance performance,
            Long memberId,
            int requestedTicketCount
    ) {
        long existingTicketCount =
                reservationRepository
                        .countSeatsByMemberAndPerformance(
                                memberId,
                                performance.getId(),
                                COUNTED_STATUSES
                        );

        performance.validateReservationLimit(
                existingTicketCount,
                requestedTicketCount
        );
    }


    private void validatePerformanceSeats(
            Long performanceId,
            List<Long> requestedIds,
            List<PerformanceSeat> performanceSeats
    ) {
        if (performanceSeats.size() != requestedIds.size()) {
            throw new ReservationException(
                    ReservationErrorCode.SEAT_NOT_FOUND
            );
        }

        boolean differentPerformance =
                performanceSeats.stream()
                        .anyMatch(performanceSeat ->
                                !performanceSeat
                                        .getPerformance()
                                        .getId()
                                        .equals(performanceId)
                        );

        if (differentPerformance) {
            throw new ReservationException(
                    ReservationErrorCode
                            .INVALID_PERFORMANCE_SEAT
            );
        }

        boolean unavailable =
                performanceSeats.stream()
                        .anyMatch(performanceSeat ->
                                !performanceSeat.isAvailable()
                        );

        if (unavailable) {
            throw new ReservationException(
                    ReservationErrorCode.SEAT_NOT_AVAILABLE
            );
        }
    }
}

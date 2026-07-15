package com.hall.backend.reservation.application;

import com.hall.backend.common.response.PageResponse;
import com.hall.backend.payment.domain.PaymentStatus;
import com.hall.backend.reservation.domain.ReservationStatus;
import com.hall.backend.reservation.exception.ReservationErrorCode;
import com.hall.backend.reservation.exception.ReservationException;
import com.hall.backend.reservation.infrastructure.ReservationRepository;
import com.hall.backend.reservation.infrastructure.projection.AdminReservationProjection;
import com.hall.backend.reservation.presentation.dto.request.AdminReservationSortType;
import com.hall.backend.reservation.presentation.dto.response.GetAdminReservationsResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetAdminReservationsService {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private static final ZoneId SERVICE_ZONE =
            ZoneId.of("Asia/Seoul");

    private final ReservationRepository
            reservationRepository;

    @Transactional(readOnly = true)
    public PageResponse<GetAdminReservationsResponse> getReservations(
            Long memberId,
            String memberEmail,
            Long concertId,
            Long performanceId,
            ReservationStatus reservationStatus,
            PaymentStatus paymentStatus,
            LocalDate createdFrom,
            LocalDate createdTo,
            Integer page,
            Integer size,
            AdminReservationSortType sortType
    ) {
        validateIds(
                memberId,
                concertId,
                performanceId
        );

        String normalizedMemberEmail =
                normalizeSearchKeyword(memberEmail);

        Instant resolvedCreatedFrom =
                resolveCreatedFrom(createdFrom);

        Instant resolvedCreatedTo =
                resolveCreatedTo(createdTo);

        validateDateRange(
                resolvedCreatedFrom,
                resolvedCreatedTo
        );

        int resolvedPage = resolvePage(page);
        int resolvedSize = resolveSize(size);

        AdminReservationSortType resolvedSortType =
                resolveSortType(sortType);

        PageRequest pageRequest = PageRequest.of(
                resolvedPage,
                resolvedSize,
                resolvedSortType.toSort()
        );

        Page<AdminReservationProjection> reservations =
                reservationRepository.searchForAdmin(
                        memberId,
                        normalizedMemberEmail,
                        concertId,
                        performanceId,
                        reservationStatus,
                        paymentStatus,
                        resolvedCreatedFrom,
                        resolvedCreatedTo,
                        pageRequest
                );

        return PageResponse.from(
                reservations,
                GetAdminReservationsResponse::from
        );
    }

    private String normalizeSearchKeyword(
            String keyword
    ) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return keyword.trim();
    }

    private Instant resolveCreatedFrom(
            LocalDate createdFrom
    ) {
        if (createdFrom == null) {
            return null;
        }

        return createdFrom
                .atStartOfDay(SERVICE_ZONE)
                .toInstant();
    }

    private Instant resolveCreatedTo(
            LocalDate createdTo
    ) {
        if (createdTo == null) {
            return null;
        }

        return createdTo
                .plusDays(1)
                .atStartOfDay(SERVICE_ZONE)
                .toInstant();
    }

    private void validateDateRange(
            Instant createdFrom,
            Instant createdTo
    ) {
        if (createdFrom != null
                && createdTo != null
                && !createdFrom.isBefore(createdTo)) {
            throw new ReservationException(
                    ReservationErrorCode
                            .INVALID_RESERVATION_DATE_RANGE
            );
        }
    }

    private void validateIds(
            Long memberId,
            Long concertId,
            Long performanceId
    ) {
        if (memberId != null && memberId <= 0) {
            throw new ReservationException(
                    ReservationErrorCode
                            .INVALID_SEARCH_ID
            );
        }

        if (concertId != null && concertId <= 0) {
            throw new ReservationException(
                    ReservationErrorCode
                            .INVALID_SEARCH_ID
            );
        }

        if (performanceId != null
                && performanceId <= 0) {
            throw new ReservationException(
                    ReservationErrorCode
                            .INVALID_SEARCH_ID
            );
        }
    }

    private int resolvePage(Integer page) {
        if (page == null) {
            return 0;
        }

        if (page < 0) {
            throw new ReservationException(
                    ReservationErrorCode
                            .INVALID_PAGE_NUMBER
            );
        }

        return page;
    }

    private int resolveSize(Integer size) {
        if (size == null) {
            return DEFAULT_PAGE_SIZE;
        }

        if (size <= 0 || size > MAX_PAGE_SIZE) {
            throw new ReservationException(
                    ReservationErrorCode
                            .INVALID_PAGE_SIZE
            );
        }

        return size;
    }

    private AdminReservationSortType resolveSortType(
            AdminReservationSortType sortType
    ) {
        return sortType == null
                ? AdminReservationSortType.LATEST
                : sortType;
    }
}

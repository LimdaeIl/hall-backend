package com.hall.backend.reservation.application;

import com.hall.backend.auth.infrastructure.security.MemberPrincipal;
import com.hall.backend.common.response.PageResponse;
import com.hall.backend.reservation.domain.ReservationStatus;
import com.hall.backend.reservation.exception.ReservationErrorCode;
import com.hall.backend.reservation.exception.ReservationException;
import com.hall.backend.reservation.infrastructure.ReservationRepository;
import com.hall.backend.reservation.infrastructure.projection.MemberReservationProjection;
import com.hall.backend.reservation.presentation.dto.request.ReservationSortType;
import com.hall.backend.reservation.presentation.dto.response.GetMyReservationsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetMyReservationsService {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private final ReservationRepository
            reservationRepository;

    @Transactional(readOnly = true)
    public PageResponse<GetMyReservationsResponse> getReservations(
            MemberPrincipal principal,
            ReservationStatus status,
            Integer page,
            Integer size,
            ReservationSortType sortType
    ) {
        validatePrincipal(principal);

        int resolvedPage = resolvePage(page);
        int resolvedSize = resolveSize(size);

        ReservationSortType resolvedSortType =
                resolveSortType(sortType);

        PageRequest pageRequest = PageRequest.of(
                resolvedPage,
                resolvedSize,
                resolvedSortType.toSort()
        );

        Page<MemberReservationProjection> reservations =
                reservationRepository.findMyReservations(
                        principal.memberId(),
                        status,
                        pageRequest
                );

        return PageResponse.from(
                reservations,
                GetMyReservationsResponse::from
        );
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

    private ReservationSortType resolveSortType(
            ReservationSortType sortType
    ) {
        return sortType == null
                ? ReservationSortType.LATEST
                : sortType;
    }

    private void validatePrincipal(
            MemberPrincipal principal
    ) {
        if (principal == null
                || principal.memberId() == null
                || principal.memberId() <= 0) {
            throw new ReservationException(
                    ReservationErrorCode
                            .RESERVATION_NOT_FOUND
            );
        }
    }
}

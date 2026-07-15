package com.hall.backend.performance.application;

import com.hall.backend.common.response.PageResponse;
import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceStatus;
import com.hall.backend.performance.infrastructure.PerformanceRepository;
import com.hall.backend.performance.presentation.dto.request.PerformanceSortType;
import com.hall.backend.performance.presentation.dto.response.GetAdminPerformancesResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetAdminPerformancesService {

    private static final int MAX_PAGE_SIZE = 100;

    private final PerformanceRepository
            performanceRepository;

    @Transactional(readOnly = true)
    public PageResponse<GetAdminPerformancesResponse>
    getPerformances(
            Long concertId,
            String concertTitle,
            PerformanceStatus status,
            LocalDateTime startsAtFrom,
            LocalDateTime startsAtTo,
            LocalDateTime reservationOpensAtFrom,
            LocalDateTime reservationOpensAtTo,
            LocalDateTime reservationClosesAtFrom,
            LocalDateTime reservationClosesAtTo,
            Integer page,
            Integer size,
            PerformanceSortType sort
    ) {
        int resolvedPage =
                page == null || page < 0
                        ? 0
                        : page;

        int resolvedSize =
                size == null || size <= 0
                        ? 20
                        : Math.min(size, MAX_PAGE_SIZE);

        PerformanceSortType resolvedSort =
                sort == null
                        ? PerformanceSortType.LATEST
                        : sort;

        Pageable pageable =
                PageRequest.of(
                        resolvedPage,
                        resolvedSize,
                        resolvedSort.toSort()
                );

        Page<Performance> performances =
                performanceRepository.searchForAdmin(
                        normalizeId(concertId),
                        normalizeText(concertTitle),
                        status,
                        startsAtFrom,
                        startsAtTo,
                        reservationOpensAtFrom,
                        reservationOpensAtTo,
                        reservationClosesAtFrom,
                        reservationClosesAtTo,
                        pageable
                );

        return PageResponse.from(
                performances,
                GetAdminPerformancesResponse::from
        );
    }

    private Long normalizeId(Long id) {
        if (id == null || id <= 0) {
            return null;
        }

        return id;
    }

    private String normalizeText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}

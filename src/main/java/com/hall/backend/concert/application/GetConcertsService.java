package com.hall.backend.concert.application;

import com.hall.backend.common.response.PageResponse;
import com.hall.backend.concert.domain.Concert;
import com.hall.backend.concert.domain.ConcertStatus;
import com.hall.backend.concert.infrastructure.ConcertRepository;
import com.hall.backend.concert.presentation.dto.request.ConcertSortType;
import com.hall.backend.concert.presentation.dto.response.GetConcertsResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetConcertsService {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private static final List<ConcertStatus> MEMBER_VISIBLE_STATUSES =
            List.of(
                    ConcertStatus.OPEN,
                    ConcertStatus.CLOSED
            );

    private final ConcertRepository concertRepository;

    @Transactional(readOnly = true)
    public PageResponse<GetConcertsResponse> getConcerts(String title, String artist, Integer page,
            Integer size, ConcertSortType sortType) {
        String normalizedTitle = normalizeSearchKeyword(title);
        String normalizedArtist = normalizeSearchKeyword(artist);

        int resolvedPage = resolvePage(page);
        int resolvedSize = resolveSize(size);
        ConcertSortType resolvedSortType = resolveSortType(sortType);

        PageRequest pageRequest = PageRequest.of(resolvedPage, resolvedSize, resolvedSortType.toSort());

        Page<Concert> concerts = concertRepository.searchForMember(
                normalizedTitle,
                normalizedArtist,
                MEMBER_VISIBLE_STATUSES,
                pageRequest
        );

        return PageResponse.from(concerts, GetConcertsResponse::from);
    }

    private String normalizeSearchKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return keyword.trim();
    }

    private int resolvePage(Integer page) {
        if (page == null) {
            return 0;
        }

        if (page < 0) {
            throw new IllegalArgumentException("page는 0 이상이어야 합니다.");
        }

        return page;
    }

    private int resolveSize(Integer size) {
        if (size == null) {
            return DEFAULT_PAGE_SIZE;
        }

        if (size <= 0 || size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("size는 1 이상 100 이하여야 합니다.");
        }

        return size;
    }

    private ConcertSortType resolveSortType(ConcertSortType sortType) {
        return sortType == null
                ? ConcertSortType.LATEST
                : sortType;
    }
}

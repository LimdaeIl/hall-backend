package com.hall.backend.concert.presentation.dto.request;

import org.springframework.data.domain.Sort;

public enum ConcertSortType {

    LATEST(
            Sort.by(
                    Sort.Order.desc("createdAt"),
                    Sort.Order.desc("id")
            )
    ),

    OLDEST(
            Sort.by(
                    Sort.Order.asc("createdAt"),
                    Sort.Order.asc("id")
            )
    );

    private final Sort sort;

    ConcertSortType(Sort sort) {
        this.sort = sort;
    }

    public Sort toSort() {
        return sort;
    }
}

package com.hall.backend.performance.presentation.dto.request;

import org.springframework.data.domain.Sort;

public enum PerformanceSortType {

    LATEST(
            Sort.by(
                    Sort.Order.desc("startsAt"),
                    Sort.Order.desc("id")
            )
    ),

    OLDEST(
            Sort.by(
                    Sort.Order.asc("startsAt"),
                    Sort.Order.asc("id")
            )
    ),

    RESERVATION_OPEN_ASC(
            Sort.by(
                    Sort.Order.asc("reservationOpensAt"),
                    Sort.Order.asc("id")
            )
    ),

    RESERVATION_OPEN_DESC(
            Sort.by(
                    Sort.Order.desc("reservationOpensAt"),
                    Sort.Order.desc("id")
            )
    );

    private final Sort sort;

    PerformanceSortType(Sort sort) {
        this.sort = sort;
    }

    public Sort toSort() {
        return sort;
    }
}

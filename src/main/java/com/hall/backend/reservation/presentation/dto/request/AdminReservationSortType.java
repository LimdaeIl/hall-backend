package com.hall.backend.reservation.presentation.dto.request;

import org.springframework.data.domain.Sort;

public enum AdminReservationSortType {

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
    ),

    PERFORMANCE_ASC(
            Sort.by(
                    Sort.Order.asc("performance.startsAt"),
                    Sort.Order.asc("id")
            )
    ),

    PERFORMANCE_DESC(
            Sort.by(
                    Sort.Order.desc("performance.startsAt"),
                    Sort.Order.desc("id")
            )
    );

    private final Sort sort;

    AdminReservationSortType(Sort sort) {
        this.sort = sort;
    }

    public Sort toSort() {
        return sort;
    }
}

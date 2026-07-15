package com.hall.backend.reservation.presentation.dto.request;

import org.springframework.data.domain.Sort;

public enum ReservationSortType {

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

    ReservationSortType(Sort sort) {
        this.sort = sort;
    }

    public Sort toSort() {
        return sort;
    }
}

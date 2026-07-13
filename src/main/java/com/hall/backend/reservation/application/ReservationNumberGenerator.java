package com.hall.backend.reservation.application;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ReservationNumberGenerator {

    private static final String PREFIX = "RSV-";
    private static final int RANDOM_LENGTH = 20;

    public String generate() {
        String randomValue = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, RANDOM_LENGTH)
                .toUpperCase();

        return PREFIX + randomValue;
    }
}

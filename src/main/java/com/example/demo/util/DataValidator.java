package com.example.demo.util;

import java.time.LocalDateTime;

public final class DateValidator {

    private DateValidator() {
        // prevent instantiation
    }

    /**
     * Validates that the given date-time is not null
     * and not in the future.
     *
     * @param dateTime LocalDateTime to validate
     * @throws IllegalArgumentException if invalid
     */
    public static void validateNotFuture(LocalDateTime dateTime) {

        if (dateTime == null) {
            throw new IllegalArgumentException("Date time cannot be null");
        }

        if (dateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Date time cannot be in the future");
        }
    }
}

package com.example.demo.util;

public final class CoordinateValidator {

    private CoordinateValidator() {
        // prevent instantiation
    }

    /**
     * Validates latitude value.
     * Range: -90 to +90
     */
    public static void validateLatitude(Double latitude) {

        if (latitude == null || latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Invalid latitude value");
        }
    }

    /**
     * Validates longitude value.
     * Range: -180 to +180
     */
    public static void validateLongitude(Double longitude) {

        if (longitude == null || longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Invalid longitude value");
        }
    }

    /**
     * Validates both latitude and longitude together.
     */
    public static void validateCoordinates(Double latitude, Double longitude) {
        validateLatitude(latitude);
        validateLongitude(longitude);
    }
}

package com.example.demo.util;

public class CoordinateValidator {

    private CoordinateValidator() {
        // utility class
    }

    public static void validateLatitude(Double latitude) {
        if (latitude == null || latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Invalid latitude value");
        }
    }

    public static void validateLongitude(Double longitude) {
        if (longitude == null || longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Invalid longitude value");
        }
    }

    public static void validateLatLong(Double latitude, Double longitude) {
        validateLatitude(latitude);
        validateLongitude(longitude);
    }
}

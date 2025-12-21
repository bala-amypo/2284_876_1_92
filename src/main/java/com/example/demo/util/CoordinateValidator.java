package com.example.demo.util;

import org.springframework.stereotype.Component;

@Component
public class CoordinateValidator {
    
    public boolean isValidLatitude(Double lat) {
        return lat != null && lat >= -90 && lat <= 90;
    }
    
    public boolean isValidLongitude(Double lon) {
        return lon != null && lon >= -180 && lon <= 180;
    }
    
    public boolean isValidCoordinates(Double lat, Double lon) {
        return isValidLatitude(lat) && isValidLongitude(lon);
    }
}
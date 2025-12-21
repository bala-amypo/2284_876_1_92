package com.example.demo.util;

import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DateValidator {
    
    public boolean isNotFuture(LocalDateTime dt) {
        return dt != null && !dt.isAfter(LocalDateTime.now());
    }
    
    public boolean isValidDateRange(LocalDate start, LocalDate end) {
        return start != null && end != null && !start.isAfter(end);
    }
}
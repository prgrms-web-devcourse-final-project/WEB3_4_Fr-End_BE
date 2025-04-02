package com.frend.planit.domain.calendar.exception;

public class CalendarNotFoundException extends RuntimeException {
    public CalendarNotFoundException(String message) {
        super(message);
    }
}
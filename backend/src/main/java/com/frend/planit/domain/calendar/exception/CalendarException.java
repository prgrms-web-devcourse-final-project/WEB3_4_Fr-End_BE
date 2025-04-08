package com.frend.planit.domain.calendar.exception;

import com.frend.planit.global.response.ErrorType;
import lombok.Getter;

@Getter
public class CalendarException extends RuntimeException {

    private final ErrorType errorType;

    public CalendarException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}

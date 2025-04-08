package com.frend.planit.domain.calendar.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CalendarRequestDto(
        @NotBlank @Size(max = 20) String calendarTitle,
        @NotNull @FutureOrPresent LocalDateTime startDate,
        @NotNull @FutureOrPresent LocalDateTime endDate,
        LocalDateTime alertTime,
        @Size(max = 200) String note
) {}
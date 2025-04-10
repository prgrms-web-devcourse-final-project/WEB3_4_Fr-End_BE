package com.frend.planit.domain.calendar.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CalendarRequestDto(

        @Schema(description = "캘린더 제목")
        @NotBlank @Size(max = 20)
        String calendarTitle,

        @Schema(description = "시작 날짜 및 시간")
        @NotNull @FutureOrPresent
        LocalDateTime startDate,

        @Schema(description = "종료 날짜 및 시간")
        @NotNull @FutureOrPresent
        LocalDateTime endDate,

        @Schema(description = "알림 시간 (선택)")
        LocalDateTime alertTime,

        @Schema(description = "캘린더 색상 코드 (기본값은 #3b82f6)")
        String labelColor,

        @Schema(description = "캘린더 설명 또는 메모")
        @Size(max = 200)
        String note

) {}
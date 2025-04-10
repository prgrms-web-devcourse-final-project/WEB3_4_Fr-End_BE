package com.frend.planit.domain.calendar.schedule.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode // 테스트 코드를 위해 추가
public class ScheduleRequest {

    @NotBlank
    @Size(max = 20)
    private String scheduleTitle;

    @NotNull
    @FutureOrPresent // 날짜가 현재 또는 미래여야 함
    private LocalDate startDate;

    @NotNull
    @FutureOrPresent
    private LocalDate endDate;

    @Size(max = 200)
    private String note;

    private LocalTime alertTime;
}

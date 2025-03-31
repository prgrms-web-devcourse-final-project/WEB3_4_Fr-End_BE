package com.frend.planit.domain.calendar.schedule.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ScheduleRequest {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScheduleRequestData {

        @Size(min = 3, message = "일정 제목은 최소 3자 이상이어야 합니다.")
        private String title;

        @NotNull(message = "시작 날짜는 필수 입력 값입니다.")
        private LocalDate startDate;

        @NotNull(message = "종료 날짜는 필수 입력 값입니다.")
        private LocalDate endDate;

        private LocalTime alertTime;

        private String note;

        @Valid
        @NotNull(message = "여행지는 필수 입력 값입니다.")
        @Size(min = 1, message = "여행지는 최소 1개 이상이어야 합니다.")
        private List<@Valid Travel> travelList;
    }

}

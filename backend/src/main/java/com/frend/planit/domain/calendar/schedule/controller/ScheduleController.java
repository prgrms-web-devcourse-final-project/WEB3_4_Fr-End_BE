package com.frend.planit.domain.calendar.schedule.controller;

import com.frend.planit.domain.calendar.schedule.dto.request.ScheduleRequest;
import com.frend.planit.domain.calendar.schedule.dto.response.ScheduleResponse;
import com.frend.planit.domain.calendar.schedule.service.ScheduleService;
import com.frend.planit.global.response.ApiResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Schedule Controller", description = "여행 일정 컨트롤러")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/api/v1/schedule/{scheduleId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "여행 일정 조회")
    public ResponseEntity<ScheduleResponse> getSchedule(@PathVariable Long scheduleId) {
        ScheduleResponse scheduleResponse = scheduleService.getSchedule(scheduleId);
        return ApiResponseHelper.success(HttpStatus.OK, scheduleResponse);
    }

    @PostMapping("/api/v1/calendar/{calendarId}/schedule")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "여행 일정 생성")
    public ResponseEntity<ScheduleResponse> createSchedule(
            @PathVariable Long calendarId,
            @Valid @RequestBody ScheduleRequest scheduleRequest
    ) {
        ScheduleResponse scheduleResponse = scheduleService.createSchedule(calendarId,
                scheduleRequest);
        return ApiResponseHelper.success(HttpStatus.CREATED, scheduleResponse);

    }

    @PatchMapping("/api/v1/schedule/{scheduleId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "여행 일정 수정")
    public ResponseEntity<Void> updateSchedule(
            @PathVariable Long scheduleId,
            @Valid @RequestBody ScheduleRequest scheduleRequest
    ) {
        scheduleService.updateSchedule(scheduleId, scheduleRequest);
        return ApiResponseHelper.success(HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/schedule/{scheduleId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "여행 일정 삭제")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ApiResponseHelper.success(HttpStatus.OK);
    }
}

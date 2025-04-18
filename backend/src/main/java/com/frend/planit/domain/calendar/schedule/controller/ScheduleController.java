package com.frend.planit.domain.calendar.schedule.controller;

import com.frend.planit.domain.calendar.schedule.dto.request.ScheduleRequest;
import com.frend.planit.domain.calendar.schedule.dto.response.ScheduleResponse;
import com.frend.planit.domain.calendar.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Schedule Controller", description = "여행 일정 컨트롤러")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/calendars/{calendarId}/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    @Operation(summary = "전체 여행 일정 조회")
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleResponse> getAllSchedules(
            @PathVariable Long calendarId,
            @AuthenticationPrincipal Long userId
    ) {
        return scheduleService.getAllSchedules(calendarId, userId);
    }

    @GetMapping("/{scheduleId}")
    @Operation(summary = "단일 여행 일정 조회")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleResponse getSchedule(
            @PathVariable Long calendarId,
            @PathVariable Long scheduleId,
            @AuthenticationPrincipal Long userId
    ) {
        return scheduleService.getSchedule(calendarId, scheduleId, userId);
    }

    @PostMapping
    @Operation(summary = "여행 일정 생성")
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleResponse createSchedule(
            @PathVariable Long calendarId,
            @Valid @RequestBody ScheduleRequest scheduleRequest,
            @AuthenticationPrincipal Long userId
    ) {
        return scheduleService.createSchedule(calendarId, scheduleRequest, userId);
    }

    @PatchMapping("/{scheduleId}")
    @Operation(summary = "여행 일정 수정")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleResponse modifySchedule(
            @PathVariable Long calendarId,
            @PathVariable Long scheduleId,
            @Valid @RequestBody ScheduleRequest scheduleRequest,
            @AuthenticationPrincipal Long userId
    ) {
        return scheduleService.modifySchedule(calendarId, scheduleId, scheduleRequest, userId);
    }

    @DeleteMapping("/{scheduleId}")
    @Operation(summary = "여행 일정 삭제")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleResponse deleteSchedule(
            @PathVariable Long calendarId,
            @PathVariable Long scheduleId,
            @AuthenticationPrincipal Long userId
    ) {
        return scheduleService.deleteSchedule(calendarId, scheduleId, userId);
    }
}
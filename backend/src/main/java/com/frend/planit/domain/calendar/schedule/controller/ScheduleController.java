package com.frend.planit.domain.calendar.schedule.controller;

import com.frend.planit.global.response.ApiResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedule/{scheduleId}")
@Tag(name = "Schedule Controller", description = "여행 일정 컨트롤러")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "여행 일정 조회")
    public ResponseEntity<SchdeuleResponse> getSchedule(@PathVariable Long scheduleId) {
        ScheduleResponse scheduleResponse = scheduleService.getSchedule(scheduleId);
        return ApiResponseHelper.success(HttpStatus.OK, scheduleResponse);
    }

    @PatchMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "여행 일정 수정")
    public ResponseEntity<void> updateSchedule(
            @PathVariable Long scheduleId,
            @RequestBody ScheduleRequest scheduleRequest
    ) {
        scheduleService.updateSchedule(scheduleId, scheduleRequest);
        return ApiResponseHelper.success(HttpStatus.OK);
    }

    @DeleteMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "여행 일정 삭제")
    public ResponseEntity<void> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ApiResponseHelper.success(HttpStatus.OK);
    }
}

package com.frend.planit.domain.calendar.schedule.travel.controller;

import com.frend.planit.domain.calendar.schedule.travel.dto.response.TravelResponse;
import com.frend.planit.domain.calendar.schedule.travel.service.TravelService;
import com.frend.planit.global.response.ApiResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules/{scheduleId}/travels")
@Tag(name = "Travel Controller", description = "행선지 컨트롤러")
public class TravelController {

    private final TravelService travelService;

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "전체 행선지 조회")
    public ResponseEntity<List<TravelResponse>> getAllTravels(@PathVariable Long scheduleId) {

        List<TravelResponse> travelResponses = travelService.getAllTravels(scheduleId);

        return ApiResponseHelper.success(HttpStatus.OK, travelResponses);
    }

}

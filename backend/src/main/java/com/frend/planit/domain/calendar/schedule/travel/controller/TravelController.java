package com.frend.planit.domain.calendar.schedule.travel.controller;

import com.frend.planit.domain.calendar.schedule.travel.dto.request.TravelRequest;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.DailyTravelResponse;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.TravelResponse;
import com.frend.planit.domain.calendar.schedule.travel.service.TravelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@Tag(name = "Travel Controller", description = "행선지 컨트롤러")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/schedules/{scheduleId}/travels")
public class TravelController {

    private final TravelService travelService;

    @GetMapping
    @Operation(summary = "행선지 조회")
    @ResponseStatus(HttpStatus.OK)
    public List<DailyTravelResponse> getAllTravels(@PathVariable Long scheduleId) {
        return travelService.getAllTravels(scheduleId);
    }

    @PostMapping
    @Operation(summary = "행선지 생성")
    @ResponseStatus(HttpStatus.CREATED)
    public TravelResponse createTravel(
            @PathVariable Long scheduleId,
            @Valid @RequestBody TravelRequest travelRequest
    ) {
        return travelService.createTravel(scheduleId, travelRequest);
    }

    @DeleteMapping("/{travelId}")
    @Operation(summary = "행선지 삭제")
    @ResponseStatus(HttpStatus.OK)
    public TravelResponse deleteTravel(
            @PathVariable Long scheduleId,
            @PathVariable Long travelId
    ) {
        return travelService.deleteTravel(scheduleId, travelId);
    }

    @PatchMapping("/{travelId}")
    @Operation(summary = "행선지 수정")
    @ResponseStatus(HttpStatus.OK)
    public TravelResponse modifyTravel(
            @PathVariable Long scheduleId,
            @PathVariable Long travelId,
            @Valid @RequestBody TravelRequest travelRequest
    ) {
        return travelService.modifyTravel(scheduleId, travelId, travelRequest);
    }
}
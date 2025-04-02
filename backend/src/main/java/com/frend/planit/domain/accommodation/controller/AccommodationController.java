package com.frend.planit.domain.accommodation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accommodations")
@RequiredArgsConstructor
public class AccommodationController {
    private final AccommodationService accommodationService;

    @PostMapping("/sync")
    public void syncAccommodations(@RequestBody List<AccommodationRequestDto> accommodations) {
        accommodationService.saveOrUpdateAccommodations(accommodations);
    }
}

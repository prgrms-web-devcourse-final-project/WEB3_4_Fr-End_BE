package com.frend.planit.domain.accommodation.controller;

import com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto;
import com.frend.planit.domain.accommodation.dto.response.AccommodationResponseDto;
import com.frend.planit.domain.accommodation.service.AccommodationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService service;

    @PostMapping
    public AccommodationResponseDto create(@RequestBody @Valid AccommodationRequestDto dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<AccommodationResponseDto> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public AccommodationResponseDto getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public AccommodationResponseDto update(@PathVariable Long id, @RequestBody @Valid AccommodationRequestDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @RequestParam boolean isAdmin) {
        service.delete(id, isAdmin);
    }
}

package com.frend.planit.domain.accommodation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccommodationRequestDto(
        @NotBlank String name,
        @NotBlank String location,
        @NotNull @Min(0) Integer pricePerNight,
        @NotNull @Min(0) Integer availableRooms,
        String amenities
) {}
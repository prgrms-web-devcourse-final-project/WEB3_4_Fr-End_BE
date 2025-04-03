package com.frend.planit.domain.accommodation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AccommodationRequestDto(
        @NotBlank @Size(max = 50) String name,
        @NotBlank @Size(max = 255) String location,
        @NotBlank @Size(max = 255) String pricePerNight,
        @NotNull @Min(0) Integer availableRooms,
        String amenities,
        @NotBlank @Size(max = 2083) String mainImage
) {}
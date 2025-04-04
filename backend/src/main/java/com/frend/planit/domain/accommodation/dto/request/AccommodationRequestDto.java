package com.frend.planit.domain.accommodation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record AccommodationRequestDto(

        @NotBlank
        String name,

        @NotBlank
        String location,

        @NotNull
        @Positive
        BigDecimal pricePerNight,

        @NotNull
        @Positive
        Integer availableRooms,

        @NotBlank
        String mainImage,

        @Size(min = 1)
        List<@NotBlank String> amenities
) {}
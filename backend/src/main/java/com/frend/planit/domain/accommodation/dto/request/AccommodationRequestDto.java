package com.frend.planit.domain.accommodation.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record AccommodationRequestDto(

        @NotBlank
        String name,

        @NotBlank
        String location,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal pricePerNight,

        @NotNull
        @PositiveOrZero
        Integer availableRooms,

        @NotBlank
        String mainImage,

        @NotNull
        List<@NotBlank String> amenities,

        @NotNull
        @PositiveOrZero
        Integer areaCode,

        @NotBlank
        String cat3,

        @NotNull
        @DecimalMin(value = "-180.0")
        @DecimalMax(value = "180.0")
        Double mapX,

        @NotNull
        @DecimalMin(value = "-90.0")
        @DecimalMax(value = "90.0")
        Double mapY,

        @NotBlank
        @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$")
        String checkInTime,

        @NotBlank
        @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$")
        String checkOutTime

) {}
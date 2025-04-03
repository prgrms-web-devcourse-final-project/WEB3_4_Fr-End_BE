package com.frend.planit.domain.accommodation.dto.response;

import java.time.LocalDateTime;

public record AccommodationResponseDto(
        Long id,
        String name,
        String location,
        Integer pricePerNight,
        Integer availableRooms,
        String amenities,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
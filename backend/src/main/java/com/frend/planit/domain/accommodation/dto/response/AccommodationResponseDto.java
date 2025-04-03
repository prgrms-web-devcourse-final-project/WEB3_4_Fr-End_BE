package com.frend.planit.domain.accommodation.dto.response;

import java.time.LocalDateTime;

public record AccommodationResponseDto(
        Long id,
        String name,
        String location,
        String pricePerNight,
        Integer availableRooms,
        String amenities,
        String mainImage,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
package com.frend.planit.domain.accommodation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AccommodationResponseDto(
        Long id,
        String name,
        String location,
        BigDecimal pricePerNight,
        Integer availableRooms,
        String mainImage,
        List<String> amenities,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {}
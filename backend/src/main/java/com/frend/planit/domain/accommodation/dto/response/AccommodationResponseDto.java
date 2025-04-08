package com.frend.planit.domain.accommodation.dto.response;

import com.frend.planit.domain.accommodation.entity.AccommodationEntity;

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
        Integer areaCode,
        String cat3,
        Double mapX,
        Double mapY,
        String checkInTime,
        String checkOutTime,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    //AccommodationEntity 기반 생성자
    public AccommodationResponseDto(AccommodationEntity entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                entity.getPricePerNight(),
                entity.getAvailableRooms(),
                entity.getMainImage(),
                entity.getAmenities(),
                entity.getAreaCode(),
                entity.getCat3(),
                entity.getMapX(),
                entity.getMapY(),
                entity.getCheckInTime(),
                entity.getCheckOutTime(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }
}
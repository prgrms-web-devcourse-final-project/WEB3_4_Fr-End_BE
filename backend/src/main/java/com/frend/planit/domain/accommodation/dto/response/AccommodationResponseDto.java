package com.frend.planit.domain.accommodation.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccommodationResponseDto {
    private Long id;
    private String name;
    private String location;
    private BigDecimal pricePerNight;
    private int availableRooms;
    private String amenities;
}
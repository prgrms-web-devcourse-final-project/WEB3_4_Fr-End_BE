package com.frend.planit.domain.accommodation.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccommodationRequestDto {
    private String name;
    private String location;
    private BigDecimal pricePerNight;
    private int availableRooms;
    private String amenities;
}
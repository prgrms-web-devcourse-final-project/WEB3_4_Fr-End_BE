package com.frend.planit.domain.accommodation.entity;

import com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto;
import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "accommodations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AccommodationEntity extends BaseTime {

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String location;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(nullable = false)
    private Integer availableRooms;

    @Column(length = 2083, nullable = false)
    private String mainImage;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "accommodation_amenities", joinColumns = @JoinColumn(name = "accommodation_id"))
    @Column(name = "amenity")
    private List<String> amenities;

    private Integer areaCode;

    private String cat3;

    private Double mapX;

    private Double mapY;

    @Column(length = 10)
    private String checkInTime;

    @Column(length = 10)
    private String checkOutTime;

    public void updateFrom(AccommodationRequestDto dto) {
        this.name = dto.name();
        this.location = dto.location();
        this.pricePerNight = dto.pricePerNight() != null ? dto.pricePerNight() : new BigDecimal("110000");
        this.availableRooms = dto.availableRooms();
        this.mainImage = dto.mainImage();
        this.amenities = dto.amenities();
        this.areaCode = dto.areaCode();
        this.cat3 = dto.cat3();
        this.mapX = dto.mapX();
        this.mapY = dto.mapY();
        this.checkInTime = dto.checkInTime() != null && !dto.checkInTime().isBlank() ? dto.checkInTime() : "15:00";
        this.checkOutTime = dto.checkOutTime() != null && !dto.checkOutTime().isBlank() ? dto.checkOutTime() : "11:00";
    }
}
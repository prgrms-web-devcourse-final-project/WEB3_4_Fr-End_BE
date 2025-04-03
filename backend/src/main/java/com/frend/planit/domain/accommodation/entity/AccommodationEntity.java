package com.frend.planit.domain.accommodation.entity;

import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "accommodations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AccommodationEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private Integer pricePerNight;
    private Integer availableRooms;
    private String amenities;

    private LocalDateTime updatedAt;

    public void updateFrom(com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto dto) {
        this.name = dto.name();
        this.location = dto.location();
        this.pricePerNight = dto.pricePerNight();
        this.availableRooms = dto.availableRooms();
        this.amenities = dto.amenities();
        this.updatedAt = LocalDateTime.now();
    }
}
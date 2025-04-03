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

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String location;

    @Column(length = 255, nullable = false)
    private String pricePerNight;

    @Column(nullable = false)
    private Integer availableRooms;

    @Column(length = 2083, nullable = false)
    private String mainImage;

    private String amenities;

    private LocalDateTime updatedAt;

    public void updateFrom(com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto dto) {
        this.name = dto.name();
        this.location = dto.location();
        this.pricePerNight = dto.pricePerNight();
        this.availableRooms = dto.availableRooms();
        this.amenities = dto.amenities();
        this.mainImage = dto.mainImage();
        this.updatedAt = LocalDateTime.now();
    }
}
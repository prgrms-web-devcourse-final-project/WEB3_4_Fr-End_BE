package com.frend.planit.domain.accommodation.entity;

import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.*;
import lombok.*;

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
}
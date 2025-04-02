package com.frend.planit.domain.accommodation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AccommodationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 숙소 ID (PK, AUTO_INCREMENT)

    @Column(nullable = false, length = 255)
    private String name; // 숙소명

    @Column(nullable = false, length = 255)
    private String location; // 주소

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight; // 1박 가격

    @Column(nullable = false)
    private int availableRooms; // 남은 객실 수

}
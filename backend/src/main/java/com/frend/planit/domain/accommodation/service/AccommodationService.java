package com.frend.planit.domain.accommodation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccommodationService {
    private final AccommodationRepository accommodationRepository;

    @Transactional
    public void saveOrUpdateAccommodations(List<AccommodationRequestDto> accommodations) {
        for (AccommodationRequestDto dto : accommodations) {
            if (accommodationRepository.existsByNameAndLocation(dto.getName(), dto.getLocation())) {
                // 업데이트 로직 (추후 추가 가능)
                continue;
            }
            Accommodation newAccommodation = Accommodation.builder()
                    .name(dto.getName())
                    .location(dto.getLocation())
                    .pricePerNight(dto.getPricePerNight())
                    .availableRooms(dto.getAvailableRooms())
                    .amenities(dto.getAmenities())
                    .build();
            accommodationRepository.save(newAccommodation);
        }
    }
}
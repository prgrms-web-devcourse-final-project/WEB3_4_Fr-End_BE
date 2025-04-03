package com.frend.planit.domain.accommodation.service;

import com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto;
import com.frend.planit.domain.accommodation.dto.response.AccommodationResponseDto;
import com.frend.planit.domain.accommodation.entity.AccommodationEntity;
import com.frend.planit.domain.accommodation.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository repository;

    @Transactional
    public void syncFromTourApi(List<AccommodationRequestDto> externalData) {
        for (AccommodationRequestDto dto : externalData) {
            repository.findByNameAndLocation(dto.name(), dto.location())
                    .ifPresentOrElse(
                            existing -> existing.updateFrom(dto),
                            () -> repository.save(AccommodationEntity.builder()
                                    .name(dto.name())
                                    .location(dto.location())
                                    .pricePerNight(dto.pricePerNight())
                                    .availableRooms(dto.availableRooms())
                                    .amenities(dto.amenities())
                                    .build())
                    );
        }
    }

    public List<AccommodationResponseDto> findAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public AccommodationResponseDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElseThrow();
    }

    @Transactional
    public AccommodationResponseDto create(AccommodationRequestDto dto) {
        AccommodationEntity entity = AccommodationEntity.builder()
                .name(dto.name())
                .location(dto.location())
                .pricePerNight(dto.pricePerNight())
                .availableRooms(dto.availableRooms())
                .amenities(dto.amenities())
                .build();
        return toDto(repository.save(entity));
    }

    @Transactional
    public AccommodationResponseDto update(Long id, AccommodationRequestDto dto) {
        AccommodationEntity entity = repository.findById(id).orElseThrow();
        entity.updateFrom(dto);
        return toDto(entity);
    }

    @Transactional
    public void delete(Long id, boolean isAdmin) {
        if (!isAdmin) throw new RuntimeException("관리자만 삭제할 수 있습니다.");
        repository.deleteById(id);
    }

    private AccommodationResponseDto toDto(AccommodationEntity entity) {
        return new AccommodationResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                entity.getPricePerNight(),
                entity.getAvailableRooms(),
                entity.getAmenities(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
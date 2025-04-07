package com.frend.planit.domain.accommodation.service;

import com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto;
import com.frend.planit.domain.accommodation.dto.response.AccommodationResponseDto;
import com.frend.planit.domain.accommodation.entity.AccommodationEntity;
import com.frend.planit.domain.accommodation.repository.AccommodationRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository repository;

    @Transactional
    public void syncFromTourApi(List<AccommodationRequestDto> externalData) {
        for (AccommodationRequestDto dto : externalData) {
            if (isInvalid(dto)) {
                continue;
            }

            repository.findByNameAndLocation(dto.name(), dto.location())
                    .ifPresentOrElse(
                            existing -> updateEntity(existing, dto),
                            () -> repository.save(toEntity(dto))
                    );
        }
    }

    private boolean isInvalid(AccommodationRequestDto dto) {
        return dto.name() == null || dto.name().isBlank()
                || dto.location() == null || dto.location().isBlank()
                || dto.mainImage() == null || dto.mainImage().isBlank()
                || dto.pricePerNight() == null
                || dto.availableRooms() == null;
    }

    @Transactional(readOnly = true)
    public AccommodationResponseDto findById(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ServiceException(ErrorType.ACCOMMODATION_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<AccommodationResponseDto> findAllPaged(String sortBy, String direction, int page, int size) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAll(pageable).map(this::toDto);
    }

    @Transactional
    public AccommodationResponseDto create(AccommodationRequestDto dto) {
        AccommodationEntity saved = repository.save(toEntity(dto));
        return toDto(saved);
    }

    @Transactional
    public AccommodationResponseDto update(Long id, AccommodationRequestDto dto) {
        AccommodationEntity entity = repository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorType.ACCOMMODATION_NOT_FOUND));
        updateEntity(entity, dto);
        return toDto(entity);
    }

    @Transactional
    public void delete(Long id, boolean isAdmin) {
        if (!isAdmin) {
            throw new ServiceException(ErrorType.ACCOMMODATION_DELETE_UNAUTHORIZED);
        }
        repository.deleteById(id);
    }

    private AccommodationEntity toEntity(AccommodationRequestDto dto) {
        return AccommodationEntity.builder()
                .name(dto.name())
                .location(dto.location())
                .pricePerNight(dto.pricePerNight())
                .availableRooms(dto.availableRooms())
                .mainImage(dto.mainImage())
                .amenities(dto.amenities())
                .build();
    }

    private AccommodationResponseDto toDto(AccommodationEntity entity) {
        return new AccommodationResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                entity.getPricePerNight(),
                entity.getAvailableRooms(),
                entity.getMainImage(),
                entity.getAmenities(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

    private void updateEntity(AccommodationEntity entity, AccommodationRequestDto dto) {
        entity.updateFrom(dto);
    }
}
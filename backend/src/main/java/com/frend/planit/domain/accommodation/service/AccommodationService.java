package com.frend.planit.domain.accommodation.service;

import com.frend.planit.domain.accommodation.client.TourApiClient;
import com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto;
import com.frend.planit.domain.accommodation.dto.response.AccommodationResponseDto;
import com.frend.planit.domain.accommodation.entity.AccommodationEntity;
import com.frend.planit.domain.accommodation.repository.AccommodationRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccommodationService {

    private final AccommodationRepository repository;
    private final TourApiClient tourApiClient;

    @Transactional(readOnly = true)
    public AccommodationResponseDto findById(Long id) {
        AccommodationEntity entity = repository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorType.ACCOMMODATION_NOT_FOUND));
        return new AccommodationResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public Page<AccommodationResponseDto> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return repository.findAll(pageable)
                .map(AccommodationResponseDto::new);
    }

    @Transactional(readOnly = true)
    public Page<AccommodationResponseDto> findAllPaged(String sortBy, String direction, int page, int size) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAll(pageable)
                .map(AccommodationResponseDto::new);
    }

    @Transactional
    public AccommodationResponseDto create(AccommodationRequestDto dto) {
        AccommodationEntity entity = AccommodationEntity.builder()
                .name(dto.name())
                .location(dto.location())
                .pricePerNight(dto.pricePerNight())
                .availableRooms(dto.availableRooms())
                .mainImage(dto.mainImage())
                .amenities(dto.amenities())
                .areaCode(dto.areaCode())
                .cat3(dto.cat3())
                .mapX(dto.mapX())
                .mapY(dto.mapY())
                .checkInTime(dto.checkInTime())
                .checkOutTime(dto.checkOutTime())
                .build();

        repository.save(entity);
        return new AccommodationResponseDto(entity);
    }

    @Transactional
    public AccommodationResponseDto update(Long id, AccommodationRequestDto dto) {
        AccommodationEntity entity = repository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorType.ACCOMMODATION_NOT_FOUND));

        entity.updateFrom(dto);
        return new AccommodationResponseDto(entity);
    }

    @Transactional
    public void delete(Long id, boolean isAdmin) {
        if (!isAdmin) {
            throw new ServiceException(ErrorType.ACCOMMODATION_DELETE_UNAUTHORIZED);
        }
        AccommodationEntity entity = repository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorType.ACCOMMODATION_NOT_FOUND));
        repository.delete(entity);
    }

    @Transactional
    public void save(AccommodationRequestDto dto) {
        AccommodationEntity entity = AccommodationEntity.builder()
                .name(dto.name())
                .location(dto.location())
                .pricePerNight(dto.pricePerNight())
                .availableRooms(dto.availableRooms())
                .mainImage(dto.mainImage())
                .amenities(dto.amenities())
                .areaCode(dto.areaCode())
                .cat3(dto.cat3())
                .mapX(dto.mapX())
                .mapY(dto.mapY())
                .checkInTime(dto.checkInTime())
                .checkOutTime(dto.checkOutTime())
                .build();

        repository.save(entity);
    }

    @Transactional(readOnly = true)
    public boolean isEmpty() {
        return repository.count() == 0;
    }

    @Transactional
    public void syncFromTourApi() {
        log.info("TourAPI: 숙소 동기화 시작");
        List<AccommodationRequestDto> dtos = tourApiClient.fetchAccommodations();
        int savedCount = 0;

        for (AccommodationRequestDto dto : dtos) {
            boolean exists = repository.findByNameAndLocation(dto.name(), dto.location()).isPresent();
            if (!exists) {
                AccommodationEntity entity = AccommodationEntity.builder()
                        .name(dto.name())
                        .location(dto.location())
                        .pricePerNight(dto.pricePerNight())
                        .availableRooms(dto.availableRooms())
                        .mainImage(dto.mainImage())
                        .amenities(dto.amenities())
                        .areaCode(dto.areaCode())
                        .cat3(dto.cat3())
                        .mapX(dto.mapX())
                        .mapY(dto.mapY())
                        .checkInTime(dto.checkInTime())
                        .checkOutTime(dto.checkOutTime())
                        .build();

                repository.save(entity);
                savedCount++;
            }
        }

        log.info("TourAPI: 숙소 동기화 완료 - 신규 저장 {}건", savedCount);
    }
}
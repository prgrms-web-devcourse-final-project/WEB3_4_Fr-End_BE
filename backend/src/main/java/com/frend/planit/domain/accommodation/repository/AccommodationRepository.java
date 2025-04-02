package com.frend.planit.domain.accommodation.repository;

import com.frend.planit.domain.accommodation.entity.AccommodationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<AccommodationEntity, Long> {
    boolean existsByNameAndLocation(String name, String location);
}
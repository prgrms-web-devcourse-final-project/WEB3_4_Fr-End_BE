package com.frend.planit.domain.accommodation.repository;

import com.frend.planit.domain.accommodation.entity.AccommodationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccommodationRepository extends JpaRepository<AccommodationEntity, Long> {
    Optional<AccommodationEntity> findByNameAndLocation(String name, String location);
}
package com.frend.planit.domain.accommodation.repository;

import com.frend.planit.domain.accommodation.entity.AccommodationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccommodationRepository extends JpaRepository<AccommodationEntity, Long> {

    Optional<AccommodationEntity> findByNameAndLocation(String name, String location);

    @Query("""
        SELECT a FROM AccommodationEntity a
        WHERE (:areaCode IS NULL OR a.areaCode = :areaCode)
          AND (:title IS NULL OR a.name LIKE %:title%)
          AND (:cat3 IS NULL OR a.cat3 = :cat3)
    """)
    Page<AccommodationEntity> findByFilters(@Param("areaCode") String areaCode,
                                            @Param("title") String title,
                                            @Param("cat3") String cat3,
                                            Pageable pageable);
}
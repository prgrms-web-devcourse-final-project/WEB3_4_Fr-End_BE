package com.frend.planit.domain.image.repository;

import com.frend.planit.domain.image.entity.ImageTestEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Profile("test")
@Repository
public interface ImageTestRepository extends JpaRepository<ImageTestEntity, Long> {

}
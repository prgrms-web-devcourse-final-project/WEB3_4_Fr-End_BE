package com.frend.planit.domain.image.repository;

import com.frend.planit.domain.image.entity.Image;
import com.frend.planit.domain.image.type.HolderType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Modifying
    @Query("UPDATE Image i SET i.holderType = :holderType, i.holderId = :holderId WHERE i.id = :imageId")
    int updateHolderForImage(
            @Param("holderType") HolderType holderType,
            @Param("holderId") Long holderId,
            @Param("imageId") long imageId
    );

    @Modifying
    @Query("UPDATE Image i SET i.holderType = :holderType, i.holderId = :holderId WHERE i.id IN :imageIds")
    int updateHolderForImages(
            @Param("holderType") HolderType holderType,
            @Param("holderId") Long holderId,
            @Param("imageIds") List<Long> imageIds
    );

    Optional<Image> findFirstByHolderTypeAndHolderIdOrderByIdAsc(
            HolderType holderType, Long holderId);

    List<Image> findAllByHolderTypeAndHolderIdOrderByIdAsc(HolderType holderType, Long holderId);
}
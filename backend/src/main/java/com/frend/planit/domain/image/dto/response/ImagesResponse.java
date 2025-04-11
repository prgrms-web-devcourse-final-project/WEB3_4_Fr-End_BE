package com.frend.planit.domain.image.dto.response;

import com.frend.planit.domain.image.entity.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record ImagesResponse(
        long count,
        List<Long> imageIds,
        List<String> imageUrls
) {

    public static ImagesResponse of(Optional<Image> image) {
        if (image.isPresent()) {
            return new ImagesResponse(1, List.of(image.get().getId()),
                    List.of(image.get().getUrl()));
        } else {
            return new ImagesResponse(0, null, null);
        }
    }

    public static ImagesResponse of(List<Image> images) {
        List<Long> imageIds = new ArrayList<>();
        List<String> imageUrls = new ArrayList<>();

        for (Image image : images) {
            imageIds.add(image.getId());
            imageUrls.add(image.getUrl());
        }

        return new ImagesResponse(images.size(), imageIds, imageUrls);
    }
}
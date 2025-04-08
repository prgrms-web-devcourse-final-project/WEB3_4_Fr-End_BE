package com.frend.planit.domain.image.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.frend.planit.domain.image.entity.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ImageResponse(
        long count,
        List<Long> imageIds,
        List<String> imageUrls
) {

    public static ImageResponse of(Optional<Image> image) {
        if (image.isPresent()) {
            return new ImageResponse(1, List.of(image.get().getId()),
                    List.of(image.get().getUrl()));
        } else {
            return new ImageResponse(0, null, null);
        }
    }

    public static ImageResponse of(List<Image> images) {
        List<Long> imageIds = new ArrayList<>();
        List<String> imageUrls = new ArrayList<>();

        for (Image image : images) {
            imageIds.add(image.getId());
            imageUrls.add(image.getUrl());
        }

        return new ImageResponse(images.size(), imageIds, imageUrls);
    }
}
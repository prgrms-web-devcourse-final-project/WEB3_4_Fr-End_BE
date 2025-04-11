package com.frend.planit.domain.image.dto.response;

import com.frend.planit.domain.image.entity.Image;
import java.util.Optional;

public record ImageResponse(
        boolean hasImage,
        Long imageId,
        String imageUrl
) {

    public static ImageResponse of(Optional<Image> image) {
        if (image.isPresent()) {
            return new ImageResponse(true, image.get().getId(), image.get().getUrl());
        } else {
            return new ImageResponse(false, null, null);
        }
    }
}
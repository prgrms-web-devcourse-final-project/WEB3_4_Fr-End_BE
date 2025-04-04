package com.frend.planit.domain.image.dto.response;

import com.frend.planit.domain.image.entity.Image;

public record ImageResponse(
        String url
) {

    public static ImageResponse of(Image image) {
        return new ImageResponse(image.getUrl());
    }
}
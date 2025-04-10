package com.frend.planit.domain.image.dto.response;

import org.springframework.context.annotation.Profile;

@Profile("test")
public record ImageTestResponse(
        String name,
        int age,
        String email,
        ImageResponse imageData
) {

}
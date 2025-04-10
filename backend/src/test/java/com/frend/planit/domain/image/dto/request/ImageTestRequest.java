package com.frend.planit.domain.image.dto.request;

import java.util.List;
import org.springframework.context.annotation.Profile;

@Profile("test")
public record ImageTestRequest(
        String name,
        int age,
        String email,
        List<Long> imageIds
) {

}
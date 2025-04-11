package com.frend.planit.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateProfileImageRequest {

    @Positive
    private long imageId;

    @NotBlank
    private String imageUrl;
}
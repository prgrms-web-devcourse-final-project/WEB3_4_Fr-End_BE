package com.frend.planit.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateProfileImageRequest {

    private String profileImageUrl;
}

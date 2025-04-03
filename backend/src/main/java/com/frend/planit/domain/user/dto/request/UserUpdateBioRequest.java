package com.frend.planit.domain.user.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdateBioRequest {

    @Size(max = 255, message = "자기소개는 255자 이내여야 합니다.")
    private String bio;
}

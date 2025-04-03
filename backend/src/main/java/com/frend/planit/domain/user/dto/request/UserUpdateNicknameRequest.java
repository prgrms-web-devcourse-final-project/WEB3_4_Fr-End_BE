package com.frend.planit.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserUpdateNicknameRequest {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;
}

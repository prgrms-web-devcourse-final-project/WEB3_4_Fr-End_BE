package com.frend.planit.domain.auth.dto.response;

import com.frend.planit.domain.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocialLoginResponse {

    private String accessToken;
    private String refreshToken;
    private UserStatus status; // ACTIVE or UNREGISTERED
}

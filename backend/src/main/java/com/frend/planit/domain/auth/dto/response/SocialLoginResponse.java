package com.frend.planit.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocialLoginResponse {

    private String accessToken;
    private String refreshToken;
    private boolean needAdditionalInfo;
}

package com.frend.planit.domain.auth.dto.request;

import com.frend.planit.domain.user.enums.SocialType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginRequest {

    private SocialType socialType;
    private String code; // 프론트에서 전달한 Google 인가 코드
}

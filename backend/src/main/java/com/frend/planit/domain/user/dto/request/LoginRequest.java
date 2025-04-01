package com.frend.planit.domain.user.dto.request;

import lombok.Getter;

@Getter
public class LoginRequest {

    private String code; // 프론트에서 전달한 Google 인가 코드
}

package com.frend.planit.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocalRegisterResponse {

    private Long userId;
    private String loginId;
    private String nickname;
    private String email;
}


package com.frend.planit.domain.auth.dto.response;

import lombok.Data;

@Data
public class GoogleUserInfoResponse {

    private String sub;        // 구글 고유 사용자 ID
    private String email;
    private String name;
    private String picture;
}

package com.frend.planit.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleUserInfoResponse {

    @JsonProperty("id")
    private String socialId;        // 구글 고유 사용자 ID
    private String email;
    private String name;
    private String picture;
}

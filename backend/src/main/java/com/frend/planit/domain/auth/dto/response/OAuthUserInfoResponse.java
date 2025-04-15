package com.frend.planit.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.frend.planit.domain.user.enums.SocialType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OAuthUserInfoResponse {

    @JsonProperty("id")
    private String socialId;
    private SocialType socialType;
}

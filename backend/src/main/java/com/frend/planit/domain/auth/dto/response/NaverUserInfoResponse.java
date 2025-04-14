package com.frend.planit.domain.auth.dto.response;

import lombok.Data;

@Data
public class NaverUserInfoResponse {

    private NaverResponse response;

    @Data
    public static class NaverResponse {

        private String id;
        private String name;
    }

    public OAuthUserInfoResponse toOAuthUserInfo() {
        return OAuthUserInfoResponse.builder()
                .socialId(response.getId())
                .build();
    }
}

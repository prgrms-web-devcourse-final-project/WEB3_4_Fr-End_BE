package com.frend.planit.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoUserInfoResponse {

    private String id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Data
    public static class KakaoAccount {

        private Profile profile;

        @Data
        public static class Profile {

            private String nickname;

            @JsonProperty("profile_image_url")
            private String profileImageUrl;
        }
    }

    public OAuthUserInfoResponse toOAuthUserInfo() {
        return OAuthUserInfoResponse.builder()
                .socialId(this.id)
                .email(null)
                .nickname(kakaoAccount.getProfile().getNickname())
                .profileImage(kakaoAccount.getProfile().getProfileImageUrl())
                .build();
    }
}

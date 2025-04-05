package com.frend.planit.domain.auth.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.frend.planit.domain.auth.dto.response.OAuthTokenResponse;
import com.frend.planit.domain.auth.dto.response.OAuthUserInfoResponse;
import com.frend.planit.domain.user.enums.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoOAuthClient implements OAuthClient {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public OAuthTokenResponse getAccessToken(String code) {

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        return restTemplate.postForEntity(
                tokenUri,  // 🔥 하드코딩 대신 설정된 값 사용
                body,
                OAuthTokenResponse.class
        ).getBody();
    }

    @Override
    public OAuthUserInfoResponse getUserInfo(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                entity,
                JsonNode.class
        );

        JsonNode attributes = response.getBody();
        JsonNode kakaoAccount = attributes.get("kakao_account");
        JsonNode profile = kakaoAccount.get("profile");

        return OAuthUserInfoResponse.builder()
                .socialId(attributes.get("id").asText())
                .email(null)
                .name(profile.has("nickname") ? profile.get("nickname").asText() : null)
                .profileImage(
                        profile.has("profile_image_url") ? profile.get("profile_image_url").asText()
                                : null)
                .build();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }
}



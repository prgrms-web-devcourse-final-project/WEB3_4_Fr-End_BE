package com.frend.planit.domain.auth.client;

import com.frend.planit.domain.auth.dto.response.KakaoUserInfoResponse;
import com.frend.planit.domain.auth.dto.response.OAuthTokenResponse;
import com.frend.planit.domain.auth.dto.response.OAuthUserInfoResponse;
import com.frend.planit.domain.user.enums.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoOAuthClient implements OAuthClient {

    private final WebClient webClient;

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

    @Override
    public OAuthTokenResponse getAccessToken(String code) {
        log.info(">>> [Kakao] Token 요청 URI: {}", tokenUri);
        log.info(">>> [Kakao] 요청 CODE: {}", code);

        return webClient.post()
                .uri(tokenUri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters
                        .fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("redirect_uri", redirectUri)
                        .with("code", code))
                .retrieve()
                .bodyToMono(OAuthTokenResponse.class)
                .block();
    }

    @Override
    public OAuthUserInfoResponse getUserInfo(String accessToken) {
        log.info(">>> [Kakao] 사용자 정보 요청");

        return webClient.get()
                .uri(userInfoUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfoResponse.class)
                .map(KakaoUserInfoResponse::toOAuthUserInfo)
                .block();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }
    
    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public String getRedirectUri() {
        return redirectUri;
    }
}

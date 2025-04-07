package com.frend.planit.domain.auth.client;

import com.frend.planit.domain.auth.dto.response.NaverUserInfoResponse;
import com.frend.planit.domain.auth.dto.response.OAuthTokenResponse;
import com.frend.planit.domain.auth.dto.response.OAuthUserInfoResponse;
import com.frend.planit.domain.user.enums.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NaverOAuthClient implements OAuthClient {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        return restTemplate.postForEntity(
                tokenUri,
                request,
                OAuthTokenResponse.class
        ).getBody();


    }

    @Override
    public OAuthUserInfoResponse getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<NaverUserInfoResponse> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                entity,
                NaverUserInfoResponse.class
        );

        return response.getBody().toOAuthUserInfo();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.NAVER;
    }
}

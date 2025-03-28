package com.frend.planit.domain.user.client;

import com.frend.planit.domain.user.dto.response.GoogleTokenResponse;
import com.frend.planit.domain.user.dto.response.GoogleUserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleOauthClient {

    @Value("${oauth2.google.client-id}")
    private String clientId;

    @Value("${oauth2.google.client-secret}")
    private String clientSecret;

    @Value("${oauth2.google.redirect-uri}")
    private String redirectUri;

    @Value("${oauth2.google.token-uri}")
    private String tokenUri;

    @Value("${oauth2.google.user-info-uri}")
    private String userInfoUri;

    private final RestTemplate restTemplate = new RestTemplate();

    // 인가 코드로 access_token 요청
    public GoogleTokenResponse getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpEntity<?> request = new HttpEntity<>(body, headers);

        ResponseEntity<GoogleTokenResponse> response = restTemplate.postForEntity(
                tokenUri,
                request,
                GoogleTokenResponse.class
        );

        return response.getBody();
    }

    // access_token으로 사용자 정보 조회
    public GoogleUserInfoResponse getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<GoogleUserInfoResponse> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                entity,
                GoogleUserInfoResponse.class
        );

        return response.getBody();
    }
}

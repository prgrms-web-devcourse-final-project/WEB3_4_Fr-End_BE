package com.frend.planit.domain.booking.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class IamportTokenService {

    @Value("${iamport.api.key}")
    private String apiKey;

    @Value("${iamport.api.secret}")
    private String apiSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 아임포트 accessToken 발급 메서드
     */
    public String getAccessToken() {
        String url = "https://api.iamport.kr/users/getToken";

        // 요청 바디 구성
        Map<String, String> requestBody = Map.of(
                "imp_key", apiKey,
                "imp_secret", apiSecret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            Map responseBody = (Map) response.getBody().get("response");
            String token = (String) responseBody.get("access_token");

            log.info("💬 발급받은 accessToken = {}", token);
            return token;

        } catch (Exception e) {
            log.error("❌ 아임포트 토큰 발급 실패", e);
            throw new RuntimeException("아임포트 토큰 발급 실패");
        }
    }
}

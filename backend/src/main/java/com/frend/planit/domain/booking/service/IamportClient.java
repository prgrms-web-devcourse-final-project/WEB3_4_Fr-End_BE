package com.frend.planit.domain.booking.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class IamportClient {

    private final RestTemplate restTemplate = new RestTemplate();

    // 아임포트 결제 취소 API 호출 메서드
    public void cancelPayment(String impUid, String accessToken) {
        String url = "https://api.iamport.kr/payments/cancel";

        // 요청 바디 생성
        Map<String, Object> requestBody = Map.of(
                "imp_uid", impUid,
                "reason", "고객 취소"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken); // 아임포트 인증 토큰

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            log.info("아임포트 결제 취소 응답 = {}", response.getBody());
        } catch (Exception e) {
            log.error("아임포트 결제 취소 실패", e);
            throw new RuntimeException("결제 취소 요청 실패");
        }
    }
}
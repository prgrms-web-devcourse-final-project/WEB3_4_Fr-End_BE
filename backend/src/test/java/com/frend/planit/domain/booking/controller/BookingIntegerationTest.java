package com.frend.planit.domain.booking.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.booking.dto.request.PaymentCompleteRequest;
import com.frend.planit.domain.booking.dto.request.PaymentCompleteRequest.CustomData;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "spring.ai.openai.api-key=dummy"
})
@AutoConfigureMockMvc
class BookingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "9", roles = "USER")
    void shouldReturnBookingInMypage_whenBookingIsSaved() throws Exception {
        // given: 예약 저장 요청 데이터 구성
        PaymentCompleteRequest request = new PaymentCompleteRequest(
                "imp_uid_test",
                "merchant_uid_test",
                "kakaopay",
                "kakaopay",
                "pg_tid_test",
                10000,
                "KRW",
                "PAID",
                1712880000L,
                "https://receipt.link",
                new CustomData(
                        9L,
                        1880L,
                        "속초 힐링 펜션",
                        "강원도 속초시 바닷가길 123",
                        "https://image.link/sample.jpg",
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        LocalTime.of(15, 0),
                        2,
                        BigDecimal.valueOf(10000)
                )
        );

        String content = objectMapper.writeValueAsString(request);

        // when: 예약 완료 API 호출
        mockMvc.perform(post("/api/v1/bookings/complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());

        // then: 마이페이지 조회 API 호출
        mockMvc.perform(get("/api/v1/bookings/mypage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.upcoming").isArray());
    }
}
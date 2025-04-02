package com.frend.planit.domain.travel.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.TravelResponse;
import com.frend.planit.domain.calendar.schedule.travel.service.TravelService;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // MockMvc 시큐리티 필터 비활성화
@ActiveProfiles("test")
@Transactional
public class TravelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //  Java 객체와 JSON 간 변환을 도와주는 Jackson의 핵심 도구
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TravelService travelService;

    private List<TravelResponse> travelResponse;

    private Long scheduleId;

    @BeforeEach
    void setUp() {
        // 테스트에 필요한 TravelResponse 객체를 생성
        scheduleId = 1L;

        TravelResponse travel1 = TravelResponse.builder()
                .lat(37.5665)
                .lng(126.9780)
                .plan(LocalDateTime.of(2025, 4, 2, 10, 0))
                .title("서울 타워")
                .content("서울의 랜드마크")
                .build();

        TravelResponse travel2 = TravelResponse.builder()
                .lat(35.1796)
                .lng(129.0756)
                .plan(LocalDateTime.of(2025, 4, 3, 15, 0))
                .title("해운대")
                .content("부산의 명소")
                .build();

        travelResponse = Arrays.asList(travel1, travel2);
    }

    @Test
    @DisplayName("전체 행선지 조회 - 성공")
    void getAllTravelsSuccess() throws Exception {
        // given
        given(travelService.getAllTravels(scheduleId)).willReturn(
                travelResponse); // 서비스 메소드가 호출될 때 반환할 값 설정

        // when & then
        mockMvc.perform(get("/api/v1/schedules/{scheduleId}/travels", scheduleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // 첫 번째 TravelResponse 검증
                .andExpect(jsonPath("$[0].title").value("서울 타워"))
                .andExpect(jsonPath("$[0].lat").value(37.5665))
                .andExpect(jsonPath("$[0].lng").value(126.9780))
                .andExpect(jsonPath("$[0].plan").value("2025-04-02T10:00:00"))
                .andExpect(jsonPath("$[0].content").value("서울의 랜드마크"))

                // 두 번째 TravelResponse 검증
                .andExpect(jsonPath("$[1].title").value("해운대"))
                .andExpect(jsonPath("$[1].lat").value(35.1796))
                .andExpect(jsonPath("$[1].lng").value(129.0756))
                .andExpect(jsonPath("$[1].plan").value("2025-04-03T15:00:00"))
                .andExpect(jsonPath("$[1].content").value("부산의 명소"));

        // 서비스 메소드가 정확히 한 번 호출되었는지 검증
        verify(travelService, times(1)).getAllTravels(scheduleId);
    }

    @Test
    @DisplayName("전체 행선지 조회 - 실패 (존재하지 않는 일정 ID)")
    void getAllTravelsFail() throws Exception {
        // given
        Long scheduleId = 999L; // 존재하지 않는 일정 ID

        given(travelService.getAllTravels(scheduleId))
                .willThrow(new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/api/v1/schedules/{scheduleId}/travels", scheduleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 스케줄이 존재하지 않습니다."));

        // 서비스 메소드가 정확히 한 번 호출되었는지 검증
        verify(travelService, times(1)).getAllTravels(scheduleId);
    }
}
package com.frend.planit.domain.travel.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.DailyTravelResponse;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.TravelResponse;
import com.frend.planit.domain.calendar.schedule.travel.service.TravelService;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import java.time.LocalDate;
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

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false) // MockMvc 시큐리티 필터 비활성화
public class TravelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //  Java 객체와 JSON 간 변환을 도와주는 Jackson의 핵심 도구
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TravelService travelService;

    private List<DailyTravelResponse> dailyTravelResponses;

    private Long scheduleId;

    @BeforeEach
    void setUp() {
        // 테스트에 필요한 TravelResponse 객체를 생성
        scheduleId = 1L;

        TravelResponse travel1 = TravelResponse.builder()
                .location("서울 타워")
                .category("랜드마크")
                .lat(37.5665)
                .lng(126.9780)
                .hour(10)
                .minute(0)
                .build();

        TravelResponse travel2 = TravelResponse.builder()
                .location("해운대")
                .category("명소")
                .lat(35.1796)
                .lng(129.0756)
                .hour(15)
                .minute(30)
                .build();

        DailyTravelResponse day1 = new DailyTravelResponse(
                LocalDate.of(2025, 4, 2),
                List.of(travel1)
        );

        DailyTravelResponse day2 = new DailyTravelResponse(
                LocalDate.of(2025, 4, 3),
                List.of(travel2)
        );

        dailyTravelResponses = Arrays.asList(day1, day2);

    }

    @Test
    @DisplayName("행선지 조회 - 성공")
    void getAllTravelsSuccess() throws Exception {
        // given
        given(travelService.getAllTravels(scheduleId)).willReturn(
                dailyTravelResponses); // 서비스 메소드가 호출될 때 반환할 값 설정

        // when & then
        mockMvc.perform(get("/api/v1/schedules/{scheduleId}/travels", scheduleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // 첫 번째 응답 검증
                .andExpect(jsonPath("$[0].date").value("2025-04-02"))
                .andExpect(jsonPath("$[0].travels[0].location").value("서울 타워"))
                .andExpect(jsonPath("$[0].travels[0].category").value("랜드마크"))
                .andExpect(jsonPath("$[0].travels[0].lat").value(37.5665))
                .andExpect(jsonPath("$[0].travels[0].lng").value(126.9780))
                .andExpect(jsonPath("$[0].travels[0].hour").value(10))
                .andExpect(jsonPath("$[0].travels[0].minute").value(0))

                // 두 번째 응답 검증
                .andExpect(jsonPath("$[1].date").value("2025-04-03"))
                .andExpect(jsonPath("$[1].travels[0].location").value("해운대"))
                .andExpect(jsonPath("$[1].travels[0].category").value("명소"))
                .andExpect(jsonPath("$[1].travels[0].lat").value(35.1796))
                .andExpect(jsonPath("$[1].travels[0].lng").value(129.0756))
                .andExpect(jsonPath("$[1].travels[0].hour").value(15))
                .andExpect(jsonPath("$[1].travels[0].minute").value(30));

        // 서비스 메소드가 정확히 한 번 호출되었는지 검증
        verify(travelService, times(1)).getAllTravels(scheduleId);
    }

    @Test
    @DisplayName("행선지 조회 - 실패 (존재하지 않는 일정 ID)")
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
package com.frend.planit.domain.calendar.schedule.travel.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.accommodation.loader.AccommodationInitialDataLoader;
import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.schedule.dto.request.ScheduleRequest;
import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import com.frend.planit.domain.calendar.schedule.travel.dto.request.TravelRequest;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.AllTravelsResponse;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.DailyTravelResponse;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.TravelResponse;
import com.frend.planit.domain.calendar.schedule.travel.service.TravelService;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import com.frend.planit.global.security.JwtTokenProvider;
import java.time.LocalDate;
import java.time.LocalTime;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false) // MockMvc 시큐리티 필터 비활성화
public class TravelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //  Java 객체와 JSON 간 변환을 도와주는 Jackson의 핵심 도구
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean // TravelService Bean 대신 Mock 객체를 주입
    private TravelService travelService;

    @MockitoBean // Accommodation DB 초기 데이터 로딩 지연 문제 방지용 Mock 처리
    AccommodationInitialDataLoader loader;

    private List<DailyTravelResponse> dailyTravelResponses;

    private Long scheduleId;

    private Long travelId;

    private TravelResponse travelResponse;

    private TravelRequest travelRequest;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private WebApplicationContext context;

    private Long userId = 1L;

    private String token;

    private AllTravelsResponse allTravelsResponse;

    private ScheduleEntity mockSchedule;

    private List<Long> scheduleDayIds;

    @BeforeEach
    void setUp() {

        // JWT 토큰 발급 (mock 으로 처리하거나 실제 로직 사용 가능)
        token = "test.jwt.token";

        given(jwtTokenProvider.validateToken(token)).willReturn(true);
        given(jwtTokenProvider.getUserIdFromToken(token)).willReturn(userId);

        // 테스트에 필요한 TravelResponse 객체를 생성
        scheduleId = 1L;
        travelId = 100L;

        travelRequest = TravelRequest.builder()
                .scheduleDayId(10L)
                .kakaomapId("kakao_123")
                .location("서울타워")
                .category("명소")
                .lat(37.5665)
                .lng(126.9780)
                .hour("14")
                .minute("30")
                .build();

        travelResponse = TravelResponse.builder()
                .kakaomapId("kakao_123")
                .location("서울타워")
                .category("명소")
                .lat(37.5665)
                .lng(126.9780)
                .hour(14)
                .minute(30)
                .build();

        TravelResponse travel1 = TravelResponse.builder()
                .kakaomapId("kakao_123")
                .location("서울타워")
                .category("랜드마크")
                .lat(37.5665)
                .lng(126.9780)
                .hour(10)
                .minute(0)
                .build();

        TravelResponse travel2 = TravelResponse.builder()
                .kakaomapId("kakao_456")
                .location("해운대")
                .category("명소")
                .lat(35.1796)
                .lng(129.0756)
                .hour(15)
                .minute(30)
                .build();

        DailyTravelResponse day1 = new DailyTravelResponse(
                10L,
                LocalDate.of(2025, 4, 2),
                List.of(travel1)
        );

        DailyTravelResponse day2 = new DailyTravelResponse(
                11L,
                LocalDate.of(2025, 4, 3),
                List.of(travel2)
        );

        dailyTravelResponses = Arrays.asList(day1, day2);

        CalendarEntity mockCalendar = CalendarEntity.builder()
                .calendarTitle("테스트 캘린더")
                .build();
        ReflectionTestUtils.setField(mockCalendar, "id", 999L); // 테스트용 ID

        scheduleDayIds = List.of(10L, 11L);

        ScheduleRequest mockScheduleRequest = ScheduleRequest.builder()
                .scheduleTitle("봄 여행")
                .startDate(LocalDate.of(2025, 4, 1))
                .endDate(LocalDate.of(2025, 4, 5))
                .blockColor("#00c3ff")
                .note("벚꽃 구경")
                .alertTime(LocalTime.of(9, 0))
                .build();

        mockSchedule = ScheduleEntity.of(mockCalendar, mockScheduleRequest);
        ReflectionTestUtils.setField(mockSchedule, "id", scheduleId); // 테스트 ID 세팅

        allTravelsResponse = AllTravelsResponse.of(
                mockSchedule,
                scheduleDayIds,
                dailyTravelResponses
        );
    }

    @Test
    @DisplayName("행선지 조회 - 성공")
    void getAllTravelsSuccess() throws Exception {
        // given
        given(travelService.getAllTravels(scheduleId, userId)).willReturn(
                allTravelsResponse); // 서비스 메소드가 호출될 때 반환할 값 설정

        // when & then
        mockMvc.perform(get("/api/v1/schedules/{scheduleId}/travels", scheduleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // 첫 번째 응답 검증
                .andExpect(jsonPath("$[0].date").value("2025-04-02"))
                .andExpect(jsonPath("$[0].travels[0].id").value("kakao_123"))
                .andExpect(jsonPath("$[0].travels[0].place_name").value("서울타워"))
                .andExpect(jsonPath("$[0].travels[0].category_group_name").value("랜드마크"))
                .andExpect(jsonPath("$[0].travels[0].x").value(37.5665))
                .andExpect(jsonPath("$[0].travels[0].y").value(126.9780))
                .andExpect(jsonPath("$[0].travels[0].hour").value(10))
                .andExpect(jsonPath("$[0].travels[0].minute").value(0))

                // 두 번째 응답 검증
                .andExpect(jsonPath("$[1].date").value("2025-04-03"))
                .andExpect(jsonPath("$[1].travels[0].id").value("kakao_456"))
                .andExpect(jsonPath("$[1].travels[0].place_name").value("해운대"))
                .andExpect(jsonPath("$[1].travels[0].category_group_name").value("명소"))
                .andExpect(jsonPath("$[1].travels[0].x").value(35.1796))
                .andExpect(jsonPath("$[1].travels[0].y").value(129.0756))
                .andExpect(jsonPath("$[1].travels[0].hour").value(15))
                .andExpect(jsonPath("$[1].travels[0].minute").value(30));

        // 서비스 메소드가 정확히 한 번 호출되었는지 검증
        verify(travelService, times(1)).getAllTravels(scheduleId, userId);
    }

    @Test
    @DisplayName("행선지 조회 - 실패 (존재하지 않는 일정 ID)")
    void getAllTravelsFail() throws Exception {
        // given
        Long wrongScheduleId = 999L; // 존재하지 않는 일정 ID

        given(travelService.getAllTravels(wrongScheduleId, userId))
                .willThrow(new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/api/v1/schedules/{scheduleId}/travels", wrongScheduleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 스케줄이 존재하지 않습니다."));

        // 서비스 메소드가 정확히 한 번 호출되었는지 검증
        verify(travelService, times(1)).getAllTravels(wrongScheduleId, userId);
    }

    @Test
    @DisplayName("행선지 생성 - 성공")
    void createTravelSuccess() throws Exception {
        // given
        given(travelService.createTravel(scheduleId, userId, travelRequest)).willReturn(
                travelResponse);

        // when & then
        mockMvc.perform(post("/api/v1/schedules/{scheduleId}/travels", scheduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(travelRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("kakao_123"))
                .andExpect(jsonPath("$.place_name").value("서울타워"))
                .andExpect(jsonPath("$.category_group_name").value("명소"))
                .andExpect(jsonPath("$.x").value(37.5665))
                .andExpect(jsonPath("$.y").value(126.9780))
                .andExpect(jsonPath("$.hour").value(14))
                .andExpect(jsonPath("$.minute").value(30));

        verify(travelService, times(1)).createTravel(scheduleId, userId, travelRequest);
    }

    @Test
    @DisplayName("행선지 삭제 - 성공")
    void deleteTravelSuccess() throws Exception {
        // when & then
        mockMvc.perform(
                        delete("/api/v1/schedules/{scheduleId}/travels/{travelId}", scheduleId, travelId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(travelService, times(1))
                .deleteTravel(eq(scheduleId), eq(userId), eq(travelId));

    }

    @Test
    @DisplayName("행선지 수정 - 성공")
    void modifyTravelSuccess() throws Exception {
        // given
        TravelRequest modifiedRequest = TravelRequest.builder()
                .scheduleDayId(10L)
                .kakaomapId("kakao_123")
                .location("남산타워") // 변경됨
                .category("랜드마크") // 변경됨
                .lat(37.5512)
                .lng(126.9882)
                .hour("16")
                .minute("45")
                .build();

        TravelResponse modifiedResponse = TravelResponse.builder()
                .kakaomapId("kakao_123")
                .location("남산타워")
                .category("랜드마크")
                .lat(37.5512)
                .lng(126.9882)
                .hour(16)
                .minute(45)
                .build();

        given(travelService.modifyTravel(scheduleId, userId, travelId, modifiedRequest))
                .willReturn(modifiedResponse);

        // when & then
        mockMvc.perform(
                        patch("/api/v1/schedules/{scheduleId}/travels/{travelId}", scheduleId, travelId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(modifiedRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.place_name").value("남산타워"))
                .andExpect(jsonPath("$.category_group_name").value("랜드마크"))
                .andExpect(jsonPath("$.x").value(37.5512))
                .andExpect(jsonPath("$.y").value(126.9882))
                .andExpect(jsonPath("$.hour").value(16))
                .andExpect(jsonPath("$.minute").value(45));

        verify(travelService, times(1)).modifyTravel(scheduleId, travelId, userId,
                modifiedRequest);
    }

    @Test
    @DisplayName("행선지 수정 - 실패 (존재하지 않는 행선지)")
    void modifyTravelFail() throws Exception {
        given(travelService.modifyTravel(scheduleId, travelId, userId, travelRequest))
                .willThrow(new ServiceException(ErrorType.TRAVEL_NOT_FOUND));

        mockMvc.perform(
                        patch("/api/v1/schedules/{scheduleId}/travels/{travelId}", scheduleId, travelId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(travelRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 행선지가 존재하지 않습니다."));
    }

}
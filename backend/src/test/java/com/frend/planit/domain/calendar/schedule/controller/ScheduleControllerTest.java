package com.frend.planit.domain.calendar.schedule.controller;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.accommodation.loader.AccommodationInitialDataLoader;
import com.frend.planit.domain.calendar.schedule.dto.request.ScheduleRequest;
import com.frend.planit.domain.calendar.schedule.dto.response.ScheduleResponse;
import com.frend.planit.domain.calendar.schedule.service.ScheduleService;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import com.frend.planit.global.security.JwtTokenProvider;
import java.time.LocalDate;
import java.time.LocalTime;
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
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ScheduleService scheduleService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean // Accommodation DB 초기 데이터 로딩 지연 문제 방지용 Mock 처리
    AccommodationInitialDataLoader loader;

    private Long calendarId = 1L;

    private Long scheduleId = 10L;

    private Long userId = 1L;

    private String token;

    private List<ScheduleResponse> scheduleResponseList;

    private ScheduleResponse scheduleResponse;

    private ScheduleRequest scheduleRequest;

    @BeforeEach
    void setUp() {
        // JWT 토큰 발급 (mock 으로 처리하거나 실제 로직 사용 가능)
        token = "test.jwt.token";

        given(jwtTokenProvider.validateToken(token)).willReturn(true);
        given(jwtTokenProvider.getUserIdFromToken(token)).willReturn(userId);

        scheduleRequest = ScheduleRequest.builder()
                .scheduleTitle("후쿠오카 여행")
                .startDate(LocalDate.of(2025, 6, 1))
                .endDate(LocalDate.of(2025, 6, 3))
                .alertTime(LocalTime.of(8, 0))
                .note("여행은 역시 먹방")
                .build();

        ScheduleRequest request1 = ScheduleRequest.builder()
                .scheduleTitle("후쿠오카 여행")
                .startDate(LocalDate.of(2025, 6, 1))
                .endDate(LocalDate.of(2025, 6, 3))
                .blockColor("#3b82f6")
                .alertTime(LocalTime.of(8, 0))
                .note("여행은 역시 먹방")
                .build();

        ScheduleRequest request2 = ScheduleRequest.builder()
                .scheduleTitle("제주도 여행")
                .startDate(LocalDate.of(2025, 5, 9))
                .endDate(LocalDate.of(2025, 5, 11))
                .blockColor("#ffcc00")
                .alertTime(LocalTime.of(8, 0))
                .note("폭삭속았수다")
                .build();

        // 전체 조회용 응답
        scheduleResponseList = List.of(
                ScheduleResponse.builder()
                        .id(1L)
                        .scheduleTitle(request1.getScheduleTitle())
                        .startDate(request1.getStartDate())
                        .endDate(request1.getEndDate())
                        .alertTime(request1.getAlertTime())
                        .note(request1.getNote())
                        .blockColor("#3b82f6")
                        .build(),
                ScheduleResponse.builder()
                        .id(2L)
                        .scheduleTitle(request2.getScheduleTitle())
                        .startDate(request2.getStartDate())
                        .endDate(request2.getEndDate())
                        .alertTime(request2.getAlertTime())
                        .note(request2.getNote())
                        .blockColor("#ffcc00")
                        .build());

        // 단일 조회용 응답
        scheduleResponse = ScheduleResponse.builder()
                .id(scheduleId)
                .scheduleTitle("후쿠오카 여행")
                .startDate(LocalDate.of(2025, 6, 1))
                .endDate(LocalDate.of(2025, 6, 3))
                .blockColor("#3b82f6")
                .alertTime(LocalTime.of(8, 0))
                .note("여행은 역시 먹방")
                .build();
    }


    @Test
    @DisplayName("전체 여행 일정 조회 - 성공")
    void getAllSchedulesSuccess() throws Exception {
        // given
        given(scheduleService.getAllSchedules(calendarId, userId))
                .willReturn(scheduleResponseList);

        // when & then
        mockMvc.perform(get("/api/v1/calendars/{calendarId}/schedules", calendarId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].scheduleTitle").value("후쿠오카 여행"))
                .andExpect(jsonPath("$[0].startDate").value("2025-06-01"))
                .andExpect(jsonPath("$[0].endDate").value("2025-06-03"))
                .andExpect(jsonPath("$[0].alertTime").value("08:00:00"))
                .andExpect(jsonPath("$[0].note").value("여행은 역시 먹방"))
                .andExpect(jsonPath("$[1].scheduleTitle").value("제주도 여행"))
                .andExpect(jsonPath("$[1].startDate").value("2025-05-09"))
                .andExpect(jsonPath("$[1].endDate").value("2025-05-11"))
                .andExpect(jsonPath("$[1].alertTime").value("08:00:00"))
                .andExpect(jsonPath("$[1].note").value("폭삭속았수다"));

        verify(scheduleService, times(1)).getAllSchedules(calendarId, userId);
    }

    @Test
    @DisplayName("여행 일정 조회 - 성공")
    void getScheduleSuccess() throws Exception {
        // given
        given(scheduleService.getSchedule(calendarId, scheduleId, userId))
                .willReturn(scheduleResponse);

        // when & then
        mockMvc.perform(
                        get("/api/v1/calendars/{calendarId}/schedules/{scheduleId}", calendarId, scheduleId)
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scheduleTitle").value("후쿠오카 여행"))
                .andExpect(jsonPath("$.startDate").value("2025-06-01"))
                .andExpect(jsonPath("$.endDate").value("2025-06-03"))
                .andExpect(jsonPath("$.blockColor").value("#3b82f6"))
                .andExpect(jsonPath("$.alertTime").value("08:00:00"))
                .andExpect(jsonPath("$.note").value("여행은 역시 먹방"));

        verify(scheduleService, times(1)).getSchedule(calendarId, scheduleId, userId);
    }

    @Test
    @DisplayName("여행 일정 조회 - 실패 (스케줄이 존재하지 않음)")
    void getSchedulesFail() throws Exception {
        // given
        given(scheduleService.getSchedule(calendarId, scheduleId, userId))
                .willThrow(new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // when & then
        mockMvc.perform(
                        get("/api/v1/calendars/{calendarId}/schedules/{scheduleId}", calendarId, scheduleId)
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 스케줄이 존재하지 않습니다."));

        verify(scheduleService, times(1)).getSchedule(calendarId, scheduleId, userId);
    }

    @Test
    @DisplayName("여행 일정 생성 - 성공")
    void createScheduleSuccess() throws Exception {
        // given
        given(scheduleService.createSchedule(calendarId, scheduleRequest, userId))
                .willReturn(scheduleResponse);

        // when & then
        mockMvc.perform(post("/api/v1/calendars/{calendarId}/schedules", calendarId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.scheduleTitle").value("후쿠오카 여행"))
                .andExpect(jsonPath("$.startDate").value("2025-06-01"))
                .andExpect(jsonPath("$.endDate").value("2025-06-03"))
                .andExpect(jsonPath("$.alertTime").value("08:00:00"))
                .andExpect(jsonPath("$.note").value("여행은 역시 먹방"));

        verify(scheduleService, times(1)).createSchedule(calendarId,
                scheduleRequest, userId);
    }

    @Test
    @DisplayName("여행 일정 생성 - 실패 (존재하지 않는 캘린더)")
    void createScheduleFail_invalidCalendar() throws Exception {
        // given
        Long wrongCalendarId = 888L;

        given(scheduleService.createSchedule(wrongCalendarId,
                scheduleRequest, userId))
                .willThrow(new ServiceException(ErrorType.CALENDAR_NOT_FOUND));

        // when & then
        mockMvc.perform(post("/api/v1/calendars/{calendarId}/schedules", wrongCalendarId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 캘린더가 존재하지 않습니다."));

        verify(scheduleService, times(1)).createSchedule(wrongCalendarId,
                scheduleRequest, userId);
    }

    @Test
    @DisplayName("여행 일정 생성 - 실패 (유효성 검증 오류)")
    void createSchedule_Fail_ValidationError() throws Exception {
        // given
        ScheduleRequest invalidRequest = ScheduleRequest.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .note("제목 없음 테스트")
                .build();

        // when & then
        mockMvc.perform(post("/api/v1/calendars/{calendarId}/schedules", calendarId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("요청 형식이 잘못되었습니다."));
    }

    @Test
    @DisplayName("여행 일정 수정 - 성공")
    void modifyScheduleSuccess() throws Exception {
        // given
        ScheduleResponse updatedResponse = ScheduleResponse.builder()
                .scheduleTitle("수정된 여행 제목")
                .startDate(LocalDate.of(2025, 6, 2))
                .endDate(LocalDate.of(2025, 6, 4))
                .alertTime(LocalTime.of(9, 0))
                .note("수정된 노트")
                .build();

        given(scheduleService.modifySchedule(calendarId, scheduleId, scheduleRequest,
                userId))
                .willReturn(updatedResponse);

        // when & then
        mockMvc.perform(patch("/api/v1/calendars/{calendarId}/schedules/{scheduleId}", calendarId,
                        scheduleId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scheduleTitle").value("수정된 여행 제목"))
                .andExpect(jsonPath("$.startDate").value("2025-06-02"))
                .andExpect(jsonPath("$.endDate").value("2025-06-04"))
                .andExpect(jsonPath("$.alertTime").value("09:00:00"))
                .andExpect(jsonPath("$.note").value("수정된 노트"));

        verify(scheduleService, times(1)).modifySchedule(calendarId, scheduleId,
                scheduleRequest, userId);
    }

    @Test
    @DisplayName("여행 일정 수정 - 실패 (존재하지 않는 스케줄)")
    void modifyScheduleFail_scheduleNotFound() throws Exception {
        // given
        given(scheduleService.modifySchedule(calendarId, scheduleId, scheduleRequest,
                userId))
                .willThrow(new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // when & then
        mockMvc.perform(patch("/api/v1/calendars/{calendarId}/schedules/{scheduleId}", calendarId,
                        scheduleId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 스케줄이 존재하지 않습니다."));

        verify(scheduleService, times(1)).modifySchedule(calendarId, scheduleId,
                scheduleRequest, userId);
    }

    @Test
    @DisplayName("여행 일정 수정 - 실패 (캘린더 ID 불일치)")
    void modifyScheduleFail_calendarMismatch() throws Exception {
        // given
        given(scheduleService.modifySchedule(calendarId, scheduleId, scheduleRequest,
                userId))
                .willThrow(new ServiceException(ErrorType.CALENDAR_NOT_FOUND));

        // when & then
        mockMvc.perform(patch("/api/v1/calendars/{calendarId}/schedules/{scheduleId}", calendarId,
                        scheduleId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 캘린더가 존재하지 않습니다."));

        verify(scheduleService, times(1)).modifySchedule(calendarId, scheduleId,
                scheduleRequest, userId);
    }

    @Test
    @DisplayName("여행 일정 수정 - 실패 (유효성 검증 오류)")
    void modifyScheduleFail_validationError() throws Exception {
        // given: 제목이 빠진 잘못된 요청
        ScheduleRequest invalidRequest = ScheduleRequest.builder()
                .startDate(LocalDate.of(2025, 6, 1))
                .endDate(LocalDate.of(2025, 6, 3))
                .note("제목 없음 테스트")
                .build();

        // when & then
        mockMvc.perform(patch("/api/v1/calendars/{calendarId}/schedules/{scheduleId}", calendarId,
                        scheduleId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("요청 형식이 잘못되었습니다."));
    }

}

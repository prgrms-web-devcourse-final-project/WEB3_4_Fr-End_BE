package com.frend.planit.domain.calendar.schedule.controller;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.calendar.schedule.dto.request.ScheduleRequest;
import com.frend.planit.domain.calendar.schedule.dto.response.ScheduleResponse;
import com.frend.planit.domain.calendar.schedule.service.ScheduleService;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import java.time.LocalDate;
import java.time.LocalTime;
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
public class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ScheduleService scheduleService;

    private Long calendarId;

    private Long scheduleId;

    private ScheduleResponse scheduleResponse;

    private ScheduleRequest scheduleRequest;

    @BeforeEach
    void setUp() {
        calendarId = 1L;
        scheduleId = 10L;

        scheduleRequest = ScheduleRequest.builder()
                .scheduleTitle("후쿠오카 여행")
                .startDate(LocalDate.of(2025, 6, 1))
                .endDate(LocalDate.of(2025, 6, 3))
                .alertTime(LocalTime.of(8, 0))
                .note("여행은 역시 먹방")
                .build();

        scheduleResponse = ScheduleResponse.builder()
                .scheduleTitle(scheduleRequest.getScheduleTitle())
                .startDate(scheduleRequest.getStartDate())
                .endDate(scheduleRequest.getEndDate())
                .alertTime(scheduleRequest.getAlertTime())
                .note(scheduleRequest.getNote())
                .build();
    }

    @Test
    @DisplayName("여행 일정 조회 - 성공")
    void getScheduleDetailsSuccess() throws Exception {
        // given
        given(scheduleService.getScheduleDetails(calendarId, scheduleId))
                .willReturn(scheduleResponse);

        // when & then
        mockMvc.perform(
                        get("/api/v1/calendars/{calendarId}/schedule/{scheduleId}", calendarId, scheduleId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scheduleTitle").value("후쿠오카 여행"))
                .andExpect(jsonPath("$.startDate").value("2025-06-01"))
                .andExpect(jsonPath("$.endDate").value("2025-06-03"))
                .andExpect(jsonPath("$.alertTime").value("08:00:00"))
                .andExpect(jsonPath("$.note").value("여행은 역시 먹방"));

        verify(scheduleService, times(1)).getScheduleDetails(calendarId, scheduleId);
    }

    @Test
    @DisplayName("여행 일정 조회 - 실패 (스케줄이 존재하지 않음)")
    void getScheduleDetailsFail() throws Exception {
        // given
        given(scheduleService.getScheduleDetails(calendarId, scheduleId))
                .willThrow(new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // when & then
        mockMvc.perform(
                        get("/api/v1/calendars/{calendarId}/schedule/{scheduleId}", calendarId, scheduleId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 스케줄이 존재하지 않습니다."));

        verify(scheduleService, times(1)).getScheduleDetails(calendarId, scheduleId);
    }

    @Test
    @DisplayName("여행 일정 생성 - 성공")
    void createScheduleSuccess() throws Exception {
        // given
        given(scheduleService.createScheduleDetails(calendarId, scheduleId, scheduleRequest))
                .willReturn(scheduleResponse);

        // when & then
        mockMvc.perform(
                        post("/api/v1/calendars/{calendarId}/schedule/{scheduleId}", calendarId, scheduleId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(scheduleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.scheduleTitle").value("후쿠오카 여행"))
                .andExpect(jsonPath("$.startDate").value("2025-06-01"))
                .andExpect(jsonPath("$.endDate").value("2025-06-03"))
                .andExpect(jsonPath("$.alertTime").value("08:00:00"))
                .andExpect(jsonPath("$.note").value("여행은 역시 먹방"));

        verify(scheduleService, times(1)).createScheduleDetails(calendarId, scheduleId,
                scheduleRequest);
    }

    @Test
    @DisplayName("여행 일정 생성 - 실패 (존재하지 않는 캘린더)")
    void createScheduleFail_invalidCalendar() throws Exception {
        // given
        Long wrongCalendarId = 888L;
        Long wrongScheduleId = 999L;

        given(scheduleService.createScheduleDetails(wrongCalendarId, wrongScheduleId,
                scheduleRequest))
                .willThrow(new ServiceException(ErrorType.CALENDAR_NOT_FOUND));

        // when & then
        mockMvc.perform(
                        post("/api/v1/calendars/{calendarId}/schedule/{scheduleId}", wrongCalendarId,
                                wrongScheduleId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(scheduleRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 캘린더가 존재하지 않습니다."));

        verify(scheduleService, times(1)).createScheduleDetails(wrongCalendarId, wrongScheduleId,
                scheduleRequest);
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
        mockMvc.perform(
                        post("/api/v1/calendars/{calendarId}/schedule/{scheduleId}", calendarId, scheduleId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("요청 형식이 잘못되었습니다."));
    }
}

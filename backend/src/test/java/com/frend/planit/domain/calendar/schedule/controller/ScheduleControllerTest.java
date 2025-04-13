package com.frend.planit.domain.calendar.schedule.controller;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.calendar.schedule.dto.request.ScheduleRequest;
import com.frend.planit.domain.calendar.schedule.dto.response.ScheduleResponse;
import com.frend.planit.domain.calendar.schedule.service.ScheduleService;
import com.frend.planit.domain.user.entity.User;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

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

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    private Long calendarId;

    private Long scheduleId;

    private Long userId;

    private List<ScheduleResponse> scheduleResponseList;

    private ScheduleResponse scheduleResponse;

    private ScheduleRequest scheduleRequest;

    @Autowired
    private WebApplicationContext context;

    private User mockUser;


    @BeforeEach
    void setUp() {
        calendarId = 1L;
        scheduleId = 10L;

        ScheduleRequest request1 = ScheduleRequest.builder()
                .scheduleTitle("후쿠오카 여행")
                .startDate(LocalDate.of(2025, 6, 1))
                .endDate(LocalDate.of(2025, 6, 3))
                .alertTime(LocalTime.of(8, 0))
                .note("여행은 역시 먹방")
                .build();

        ScheduleRequest request2 = ScheduleRequest.builder()
                .scheduleTitle("제주도 여행")
                .startDate(LocalDate.of(2025, 5, 9))
                .endDate(LocalDate.of(2025, 5, 11))
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
                .alertTime(LocalTime.of(8, 0))
                .note("여행은 역시 먹방")
                .build();

        // Mock 유저 및 SecurityContext 설정
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("utf-8", true))
                .apply(springSecurity())
                .build();

        // Mock 유저 및 SecurityContext 설정
        mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(1L);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(mockUser.getId(), null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }


    @Test
    @DisplayName("전체 여행 일정 조회 - 성공")
    void getAllSchedulesSuccess() throws Exception {
        // given
        given(scheduleService.getAllSchedules(calendarId, mockUser.getId()))
                .willReturn(scheduleResponseList);

        // when & then
        mockMvc.perform(
                        get("/api/v1/calendars/{calendarId}/schedules", calendarId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
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

        verify(scheduleService, times(1)).getAllSchedules(calendarId, mockUser.getId());
    }

    @Test
    @DisplayName("여행 일정 조회 - 성공")
    void getScheduleSuccess() throws Exception {
        // given
        given(scheduleService.getSchedule(calendarId, scheduleId, mockUser.getId()))
                .willReturn(scheduleResponse);

        // when & then
        mockMvc.perform(
                        get("/api/v1/calendars/{calendarId}/schedules/{scheduleId}", calendarId, scheduleId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.scheduleTitle").value("후쿠오카 여행"))
                .andExpect(jsonPath("$.startDate").value("2025-06-01"))
                .andExpect(jsonPath("$.endDate").value("2025-06-03"))
                .andExpect(jsonPath("$.alertTime").value("08:00:00"))
                .andExpect(jsonPath("$.note").value("여행은 역시 먹방"))
                .andExpect(jsonPath("$.label_color").value("#3b82f6"));

        verify(scheduleService, times(1)).getSchedule(calendarId, scheduleId, mockUser.getId());
    }

    @Test
    @DisplayName("여행 일정 조회 - 실패 (스케줄이 존재하지 않음)")
    void getSchedulesFail() throws Exception {
        // given
        given(scheduleService.getSchedule(calendarId, scheduleId, mockUser.getId()))
                .willThrow(new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // when & then
        mockMvc.perform(
                        get("/api/v1/calendars/{calendarId}/schedules/{scheduleId}", calendarId, scheduleId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.message").value("해당 스케줄이 존재하지 않습니다."));

        verify(scheduleService, times(1)).getSchedule(calendarId, scheduleId, mockUser.getId());
    }

    @Test
    @DisplayName("여행 일정 생성 - 성공")
    void createScheduleSuccess() throws Exception {
        // given
        given(scheduleService.createSchedule(calendarId, scheduleRequest, mockUser.getId()))
                .willReturn(scheduleResponse);

        // when & then
        mockMvc.perform(
                        post("/api/v1/calendars/{calendarId}/schedules", calendarId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(scheduleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.scheduleTitle").value("후쿠오카 여행"))
                .andExpect(jsonPath("$.startDate").value("2025-06-01"))
                .andExpect(jsonPath("$.endDate").value("2025-06-03"))
                .andExpect(jsonPath("$.alertTime").value("08:00:00"))
                .andExpect(jsonPath("$.note").value("여행은 역시 먹방"));

        verify(scheduleService, times(1)).createSchedule(calendarId,
                scheduleRequest, mockUser.getId());
    }

    @Test
    @DisplayName("여행 일정 생성 - 실패 (존재하지 않는 캘린더)")
    void createScheduleFail_invalidCalendar() throws Exception {
        // given
        Long wrongCalendarId = 888L;

        given(scheduleService.createSchedule(wrongCalendarId,
                scheduleRequest, mockUser.getId()))
                .willThrow(new ServiceException(ErrorType.CALENDAR_NOT_FOUND));

        // when & then
        mockMvc.perform(
                        post("/api/v1/calendars/{calendarId}/schedules", wrongCalendarId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(scheduleRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.message").value("해당 캘린더가 존재하지 않습니다."));

        verify(scheduleService, times(1)).createSchedule(wrongCalendarId,
                scheduleRequest, mockUser.getId());
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
                        post("/api/v1/calendars/{calendarId}/schedules", calendarId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.userId").value(1L))
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
                mockUser.getId()))
                .willReturn(updatedResponse);

        // when & then
        mockMvc.perform(
                        patch("/api/v1/calendars/{calendarId}/schedule/{scheduleId}", calendarId,
                                scheduleId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(scheduleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.scheduleTitle").value("수정된 여행 제목"))
                .andExpect(jsonPath("$.startDate").value("2025-06-02"))
                .andExpect(jsonPath("$.endDate").value("2025-06-04"))
                .andExpect(jsonPath("$.alertTime").value("09:00:00"))
                .andExpect(jsonPath("$.note").value("수정된 노트"));

        verify(scheduleService, times(1)).modifySchedule(calendarId, scheduleId,
                scheduleRequest, mockUser.getId());
    }

    @Test
    @DisplayName("여행 일정 수정 - 실패 (존재하지 않는 스케줄)")
    void modifyScheduleFail_scheduleNotFound() throws Exception {
        // given
        given(scheduleService.modifySchedule(calendarId, scheduleId, scheduleRequest,
                mockUser.getId()))
                .willThrow(new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // when & then
        mockMvc.perform(
                        patch("/api/v1/calendars/{calendarId}/schedule/{scheduleId}", calendarId,
                                scheduleId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(scheduleRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.message").value("해당 스케줄이 존재하지 않습니다."));

        verify(scheduleService, times(1)).modifySchedule(calendarId, scheduleId,
                scheduleRequest, mockUser.getId());
    }

    @Test
    @DisplayName("여행 일정 수정 - 실패 (캘린더 ID 불일치)")
    void modifyScheduleFail_calendarMismatch() throws Exception {
        // given
        given(scheduleService.modifySchedule(calendarId, scheduleId, scheduleRequest,
                mockUser.getId()))
                .willThrow(new ServiceException(ErrorType.CALENDAR_NOT_FOUND));

        // when & then
        mockMvc.perform(
                        patch("/api/v1/calendars/{calendarId}/schedule/{scheduleId}", calendarId,
                                scheduleId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(scheduleRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.message").value("해당 캘린더가 존재하지 않습니다."));

        verify(scheduleService, times(1)).modifySchedule(calendarId, scheduleId,
                scheduleRequest, mockUser.getId());
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
        mockMvc.perform(
                        patch("/api/v1/calendars/{calendarId}/schedule/{scheduleId}", calendarId,
                                scheduleId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.message").value("요청 형식이 잘못되었습니다."));
    }

}

package com.frend.planit.domain.calendar;

import com.frend.planit.domain.calendar.controller.CalendarController;
import com.frend.planit.domain.calendar.dto.request.CalendarRequestDto;
import com.frend.planit.domain.calendar.dto.response.CalendarResponseDto;
import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.service.CalendarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CalendarControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CalendarService calendarService;

    @InjectMocks
    private CalendarController calendarController;

    private CalendarRequestDto calendarRequestDto;
    private CalendarResponseDto calendarResponseDto;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);


        mockMvc = MockMvcBuilders.standaloneSetup(calendarController).build();


        calendarRequestDto = new CalendarRequestDto();
        calendarRequestDto.setCalendarTitle("Test Calendar");
        calendarRequestDto.setStartDate(LocalDateTime.now());
        calendarRequestDto.setEndDate(LocalDateTime.now().plusHours(1));
        calendarRequestDto.setTime(LocalDateTime.now().plusMinutes(30));
        calendarRequestDto.setAlertTime(LocalDateTime.now().plusMinutes(15));
        calendarRequestDto.setNote("Test Note");


        calendarResponseDto = new CalendarResponseDto(
                new CalendarEntity(
                        calendarRequestDto.getCalendarTitle(),
                        calendarRequestDto.getStartDate(),
                        calendarRequestDto.getEndDate(),
                        calendarRequestDto.getTime(),
                        calendarRequestDto.getAlertTime(),
                        calendarRequestDto.getNote()
                )
        );
    }

    // 캘린터 생성 테스트
    @Test
    void createCalendar() throws Exception {
        when(calendarService.createCalendar(Mockito.any(CalendarRequestDto.class))).thenReturn(calendarResponseDto);

        mockMvc.perform(post("/api/calendar")
                        .contentType("application/json")
                        .content("{\"calendarTitle\":\"Test Calendar\",\"startDate\":\"" + calendarRequestDto.getStartDate() + "\", \"endDate\":\"" + calendarRequestDto.getEndDate() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.calendarTitle").value("Test Calendar"));
    }

    // 특정 캘린더 조회 테스트 (모든 캘린더중에 사용자가 찾고자하는 특정 캘린더)
    @Test
    void getCalendar() throws Exception {
        when(calendarService.getCalendar(Mockito.anyLong())).thenReturn(calendarResponseDto);

        mockMvc.perform(get("/api/calendar/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.calendarTitle").value("Test Calendar"));
    }

    // 전체 캘린더 조회 테스트
    @Test
    void getAllCalendars() throws Exception {
        when(calendarService.getAllCalendars()).thenReturn(Collections.singletonList(calendarResponseDto));

        mockMvc.perform(get("/api/calendar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].calendarTitle").value("Test Calendar"));
    }

    // 캘린더 수정 테스트
    @Test
    void updateCalendar() throws Exception {

        CalendarResponseDto updatedResponseDto = new CalendarResponseDto(
                new CalendarEntity(
                        "Updated Calendar",  // 임시로 제목 수정
                        calendarRequestDto.getStartDate(),
                        calendarRequestDto.getEndDate(),
                        calendarRequestDto.getTime(),
                        calendarRequestDto.getAlertTime(),
                        calendarRequestDto.getNote()
                )
        );


        when(calendarService.updateCalendar(Mockito.anyLong(), Mockito.any(CalendarRequestDto.class)))
                .thenReturn(updatedResponseDto);


        mockMvc.perform(put("/api/calendar/{id}", 1L)
                        .contentType("application/json")
                        .content("{\"calendarTitle\":\"Updated Calendar\",\"startDate\":\"" + calendarRequestDto.getStartDate() + "\", \"endDate\":\"" + calendarRequestDto.getEndDate() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.calendarTitle").value("Updated Calendar"));  // 업데이트된 타이틀 검증
    }

    // 캘린더 삭제 테스트
    @Test
    void deleteCalendar() throws Exception {
        mockMvc.perform(delete("/api/calendar/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
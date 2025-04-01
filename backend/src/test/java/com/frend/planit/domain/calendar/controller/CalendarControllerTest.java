package com.frend.planit.domain.calendar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.frend.planit.domain.calendar.dto.request.CalendarRequestDto;
import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.repository.CalendarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class CalendarControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CalendarRepository calendarRepository;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private Long savedCalendarId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        // 기본 캘린더 데이터 추가 (수정 & 삭제 테스트에 사용)
        CalendarEntity calendar = CalendarEntity.builder()
                .calendarTitle("테스트 캘린더")
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(2))
                .time(LocalDateTime.now().plusHours(3))
                .alertTime(null)
                .note("테스트 메모")
                .build();
        savedCalendarId = calendarRepository.save(calendar).getId(); // ID 저장
    }

    @Test
    void testCreateCalendar() throws Exception {
        CalendarRequestDto requestDto = new CalendarRequestDto(
                "회의",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusHours(5),
                null,
                "중요한 회의"
        );

        mockMvc.perform(post("/api/calendar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.calendarTitle").value("회의"));
    }

    @Test
    void testGetAllCalendars() throws Exception {
        mockMvc.perform(get("/api/calendar"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateCalendar() throws Exception {
        CalendarRequestDto requestDto = new CalendarRequestDto(
                "업데이트된 회의",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusHours(6),
                null,
                "업데이트된 메모"
        );

        mockMvc.perform(put("/api/calendar/" + savedCalendarId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.calendarTitle").value("업데이트된 회의"));
    }

    @Test
    void testDeleteCalendar() throws Exception {
        mockMvc.perform(delete("/api/calendar/" + savedCalendarId))
                .andExpect(status().isNoContent());
    }
}
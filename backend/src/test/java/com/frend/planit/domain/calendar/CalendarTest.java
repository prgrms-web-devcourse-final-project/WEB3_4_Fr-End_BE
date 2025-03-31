package com.frend.planit.domain.calendar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.calendar.dto.request.CalendarRequestDto;
import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.repository.CalendarRepository;
import com.frend.planit.domain.calendar.service.CalendarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CalendarTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private CalendarService calendarService;

    private CalendarEntity savedCalendar;

    @BeforeEach
    void setup() {
        savedCalendar = calendarRepository.save(new CalendarEntity(
                "Test Calendar",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now(),
                LocalDateTime.now().minusHours(1),
                "Test Note"
        ));
    }

  // 캘린더 생성 테스트
    @Test
    void createCalendarTest() throws Exception {
        CalendarRequestDto requestDto = new CalendarRequestDto();
        requestDto.setCalendarTitle("New Calendar");
        requestDto.setStartDate(LocalDateTime.now());
        requestDto.setEndDate(LocalDateTime.now().plusDays(1));
        requestDto.setTime(LocalDateTime.now());
        requestDto.setAlertTime(LocalDateTime.now().minusHours(1));
        requestDto.setNote("New Note");

        mockMvc.perform(post("/api/calendar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.calendarTitle").value("New Calendar"));
    }

// 특정 캘린더 조회 테스트
    @Test
    void getCalendarTest() throws Exception {
        mockMvc.perform(get("/api/calendar/" + savedCalendar.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.calendarTitle").value("Test Calendar"));
    }

// 전체 캘린더 조회 테스트
    @Test
    void getAllCalendarsTest() throws Exception {
        mockMvc.perform(get("/api/calendar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNotEmpty())
                .andExpect(jsonPath("$[0].calendarTitle").value("Test Calendar"));
    }

 // 캘린더 수정 테스트
    @Test
    void updateCalendarTest() throws Exception {
        CalendarRequestDto requestDto = new CalendarRequestDto();
        requestDto.setCalendarTitle("Updated Calendar");
        requestDto.setStartDate(savedCalendar.getStartDate());
        requestDto.setEndDate(savedCalendar.getEndDate());
        requestDto.setTime(savedCalendar.getTime());
        requestDto.setAlertTime(savedCalendar.getAlertTime());
        requestDto.setNote("Updated Note");

        mockMvc.perform(put("/api/calendar/" + savedCalendar.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.calendarTitle").value("Updated Calendar"));
    }

// 캘린더 삭제 테스트
    @Test
    void deleteCalendarTest() throws Exception {
        mockMvc.perform(delete("/api/calendar/" + savedCalendar.getId()))
                .andExpect(status().isNoContent());

        Optional<CalendarEntity> deletedCalendar = calendarRepository.findById(savedCalendar.getId());
        assertThat(deletedCalendar).isEmpty();
    }
}

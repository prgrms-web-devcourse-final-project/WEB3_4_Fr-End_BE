package com.frend.planit.domain.calendar.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.frend.planit.domain.accommodation.loader.AccommodationInitialDataLoader;
import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.repository.CalendarRepository;
import com.frend.planit.domain.calendar.schedule.dto.request.ScheduleRequest;
import com.frend.planit.domain.calendar.schedule.dto.response.ScheduleResponse;
import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import com.frend.planit.domain.calendar.schedule.repository.ScheduleRepository;
import com.frend.planit.domain.calendar.schedule.service.ScheduleService;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.exception.ServiceException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ScheduleServiceTest {

    @InjectMocks
    private ScheduleService scheduleService;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private CalendarRepository calendarRepository;

    @Mock
    private UserRepository userRepository;

    @MockitoBean // Accommodation DB 초기 데이터 로딩 지연 문제 방지용 Mock 처리
    AccommodationInitialDataLoader loader;

    private Long userId = 1L;

    private Long calendarId = 10L;

    private Long scheduleId = 100L;

    private CalendarEntity calendarEntity;

    private ScheduleEntity scheduleEntity1;

    private ScheduleEntity scheduleEntity2;

    private ScheduleRequest schedule1;

    private ScheduleRequest schedule2;

    private List<ScheduleResponse> expectedResponses;

    private User user;

    @BeforeEach
    void setUp() {

        // 유저 ID 설정
        user = User.builder().build();
        ReflectionTestUtils.setField(user, "id", userId);

        // 캘린더 생성
        calendarEntity = CalendarEntity.builder()
                .calendarTitle("테스트 캘린더")
                .build();
        ReflectionTestUtils.setField(calendarEntity, "id", calendarId);

        // 첫번째 스케줄 생성
        schedule1 = ScheduleRequest.builder()
                .scheduleTitle("도쿄 여행")
                .startDate(LocalDate.of(2025, 5, 10))
                .endDate(LocalDate.of(2025, 5, 12))
                .blockColor("#3b82f6")
                .note("벚꽃 시즌")
                .alertTime(LocalTime.of(9, 0))
                .build();

        scheduleEntity1 = ScheduleEntity.of(calendarEntity, schedule1);
        ReflectionTestUtils.setField(scheduleEntity1, "id", scheduleId);

        // 두번째 스케줄 생성
        schedule2 = ScheduleRequest.builder()
                .scheduleTitle("제주도 여행")
                .startDate(LocalDate.of(2025, 6, 1))
                .endDate(LocalDate.of(2025, 6, 3))
                .blockColor("#ffcc00")
                .note("돌담길 산책")
                .alertTime(LocalTime.of(10, 0))
                .build();

        scheduleEntity2 = ScheduleEntity.of(calendarEntity, schedule2);
        ReflectionTestUtils.setField(scheduleEntity2, "id", scheduleId + 1);

        // 스케줄 응답 객체 생성
        expectedResponses = List.of(
                ScheduleResponse.from(scheduleEntity1),
                ScheduleResponse.from(scheduleEntity2)
        );
    }

    @Test
    @DisplayName("전체 여행 일정 조회 - 성공")
    void getAllSchedulesSuccess() {
        // given

        // 로그인 사용자 확인
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // 여행 일정 존재여부 확인
        when(scheduleRepository.findAllByCalendarId(calendarId))
                .thenReturn(List.of(scheduleEntity1, scheduleEntity2));

        // when
        List<ScheduleResponse> actual = scheduleService.getAllSchedules(calendarId, userId);

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponses);
    }

    @Test
    @DisplayName("전체 여행 일정 조회 - 실패 (인증되지 않은 사용자")
    void getAllSchedulesFail_UnauthorizedUser() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ServiceException.class, () -> {
            scheduleService.getAllSchedules(calendarId, userId);
        });
    }

    @Test
    @DisplayName("전체 여행 일정 조회 - 실패 (존재하지 않는 캘린더 ID)")
    void getAllSchedulesFail_InvalidCalendarId() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(scheduleRepository.findAllByCalendarId(calendarId)).thenReturn(List.of());

        // when & then
        assertThrows(ServiceException.class, () -> {
            scheduleService.getAllSchedules(calendarId, userId);
        });
    }

    @Test
    @DisplayName("단일 여행 일정 조회 - 성공")
    void getScheduleSuccess() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(scheduleRepository.findByIdAndCalendarId(scheduleId, calendarId))
                .thenReturn(Optional.of(scheduleEntity1));

        // when
        ScheduleResponse actual = scheduleService.getSchedule(calendarId, scheduleId, userId);

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(ScheduleResponse.from(scheduleEntity1));
    }
}

package com.frend.planit.domain.calendar.schedule.travel.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.frend.planit.domain.accommodation.loader.AccommodationInitialDataLoader;
import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.schedule.day.entity.ScheduleDayEntity;
import com.frend.planit.domain.calendar.schedule.day.repository.ScheduleDayRepository;
import com.frend.planit.domain.calendar.schedule.dto.request.ScheduleRequest;
import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import com.frend.planit.domain.calendar.schedule.repository.ScheduleRepository;
import com.frend.planit.domain.calendar.schedule.travel.dto.request.TravelRequest;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.AllTravelsResponse;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.DailyTravelResponse;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.TravelResponse;
import com.frend.planit.domain.calendar.schedule.travel.entity.TravelEntity;
import com.frend.planit.domain.calendar.schedule.travel.repository.TravelRepository;
import com.frend.planit.domain.calendar.schedule.travel.travelUtils.TravelGroupingUtils;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
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
public class TravelServiceTest {

    @InjectMocks
    private TravelService travelService;

    @Mock
    private TravelRepository travelRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ScheduleDayRepository scheduleDayRepository;

    @Mock
    private UserRepository userRepository;

    @MockitoBean // Accommodation DB 초기 데이터 로딩 지연 문제 방지용 Mock 처리
    AccommodationInitialDataLoader loader;

    private ScheduleEntity schedule;

    private ScheduleDayEntity scheduleDay;

    private TravelEntity travel;

    private TravelRequest request;

    private CalendarEntity calendar;

    private Long userId = 1L;

    private Long calendarId = 2L;

    private Long scheduleId = 3L;

    private Long scheduleDayId = 4L;

    private Long travelId = 5L;

    private User user;

    private CalendarEntity calendarEntity;

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

        ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                .scheduleTitle("오사카 여행")
                .startDate(LocalDate.of(2025, 4, 6))
                .endDate(LocalDate.of(2025, 4, 8))
                .note("오사카의 모든 것")
                .alertTime(LocalTime.of(9, 0))
                .build();

        // 스케줄 생성 (3일짜리)
        schedule = ScheduleEntity.of(calendar, scheduleRequest);
        ReflectionTestUtils.setField(schedule, "id", scheduleId);

        // 4월 6일에 해당하는 ScheduleDay 찾기
        scheduleDay = schedule.getScheduleDayList().stream()
                .filter(day -> day.getDate().equals(LocalDate.of(2025, 4, 6)))
                .findFirst()
                .orElseThrow();
        ReflectionTestUtils.setField(scheduleDay, "id", scheduleDayId);

        // 요청 DTO
        request = TravelRequest.builder()
                .scheduleDayId(scheduleDay.getId())
                .kakaomapId("kakao_123")
                .location("서울타워")
                .category("명소")
                .lat(37.5665)
                .lng(126.9780)
                .hour("14")
                .minute("30")
                .build();

        // 여행지 생성 (scheduleDay 필요함)
        travel = TravelEntity.of(request, scheduleDay);
        ReflectionTestUtils.setField(travel, "id", travelId);
    }

    @Test
    @DisplayName("행선지 조회 - 성공")
    void getAllTravelsSuccess() {
        // given

        // 로그인 사용자 확인
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // 스케줄 존재 여부 확인
        when(scheduleRepository.findById(schedule.getId())).thenReturn(Optional.of(schedule));

        // 스케줄에 속한 행선지 조회
        when(travelRepository.findAllByScheduleId(schedule.getId())).thenReturn(List.of(travel));

        // 스케줄에 속한 날짜 조회
        when(scheduleDayRepository.findAllByScheduleId(schedule.getId())).thenReturn(
                List.of(scheduleDay));

        // 그룹핑 유틸리티는 실제 로직과 상관없이 mock 처리
        List<DailyTravelResponse> dummyDailyTravels = List.of(
                DailyTravelResponse.of(scheduleDay.getId(), scheduleDay.getDate(),
                        List.of(travel))
        );
        mockStatic(TravelGroupingUtils.class).when(() ->
                TravelGroupingUtils.groupTravelsByDate(anyList())
        ).thenReturn(dummyDailyTravels);

        // when
        AllTravelsResponse result = travelService.getAllTravels(schedule.getId(), user.getId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.scheduleTitle()).isEqualTo("오사카 여행");
        assertThat(result.dailyTravels()).isNotEmpty();
        assertThat(result.dailyTravels().get(0).getDate()).isEqualTo(scheduleDay.getDate());
    }

    @Test
    @DisplayName("행선지 조회 - 실패 (스케줄이 존재하지 않음)")
    void getAllTravelsScheduleNotFound() {
        //given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(scheduleRepository.findById(schedule.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> travelService.getAllTravels(schedule.getId(), user.getId()))
                .isInstanceOf(ServiceException.class)
                .hasMessage(ErrorType.SCHEDULE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("행선지 조회 - 실패 (인증되지 않은 사용자)")
    void getAllTravelsTravelNotFound() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(scheduleRepository.findById(schedule.getId())).thenReturn(Optional.of(schedule));
        when(travelRepository.findAllByScheduleId(schedule.getId())).thenReturn(List.of(travel));

        // when & then
        assertThrows(ServiceException.class, () -> {
            travelService.getAllTravels(schedule.getId(), user.getId());
        });


    }


    @Test
    @DisplayName("행선지 생성 - 성공")
    void createTravelSuccess() {
        // given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(scheduleRepository.findById(schedule.getId())).thenReturn(Optional.of(schedule));
        when(scheduleDayRepository.findById(scheduleDay.getId())).thenReturn(
                Optional.of(scheduleDay));
        when(travelRepository.save(any(TravelEntity.class))).thenReturn(travel);

        // when
        TravelResponse response = travelService.createTravel(schedule.getId(), user.getId(),
                request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getLocation()).isEqualTo("서울타워");
        verify(travelRepository, times(1)).save(any(TravelEntity.class));
    }

    @Test
    @DisplayName("행선지 생성 - 실패 (스케줄이 존재하지 않음)")
    void createTravelSuccessFail() {
        // given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(scheduleRepository.findById(schedule.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(
                () -> travelService.createTravel(schedule.getId(), user.getId(), request))
                .isInstanceOf(ServiceException.class)
                .hasMessage(ErrorType.SCHEDULE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("행선지 삭제 - 성공")
    void deleteTravelSuccess() {
        // given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(scheduleRepository.findById(schedule.getId())).thenReturn(Optional.of(schedule));
        when(travelRepository.findById(travel.getId())).thenReturn(Optional.of(travel));

        // when
        travelService.deleteTravel(schedule.getId(), travel.getId(), user.getId());

        // then
        verify(travelRepository, times(1)).delete(travel);
    }

    @Test
    @DisplayName("행선지 삭제 - 실패 (날짜 소속이 다름)")
    void deleteTravelFail() {
        // given
        CalendarEntity wrongCalendar = CalendarEntity.builder()
                .id(2L)
                .calendarTitle("잘못된 캘린더")
                .build();

        ScheduleRequest wrongScheduleRequest = ScheduleRequest.builder()
                .scheduleTitle("다른 일정")
                .startDate(LocalDate.of(2025, 5, 1))
                .endDate(LocalDate.of(2025, 5, 2))
                .build();

        ScheduleEntity wrongSchedule = ScheduleEntity.of(wrongCalendar, wrongScheduleRequest);
        ReflectionTestUtils.setField(wrongSchedule, "id", 2L);

        scheduleDay.setSchedule(wrongSchedule);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(scheduleRepository.findById(schedule.getId())).thenReturn(Optional.of(schedule));
        when(travelRepository.findById(travel.getId())).thenReturn(Optional.of(travel));

        // when & then
        assertThatThrownBy(
                () -> travelService.deleteTravel(schedule.getId(), travel.getId(), user.getId()))
                .isInstanceOf(ServiceException.class)
                .hasMessage(ErrorType.SCHEDULE_DAY_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("행선지 수정 - 성공")
    void modifyTravelSuccess() {
        // given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(scheduleRepository.findById(schedule.getId())).thenReturn(Optional.of(schedule));
        when(travelRepository.findById(travel.getId())).thenReturn(Optional.of(travel));
        when(scheduleDayRepository.findById(scheduleDay.getId())).thenReturn(
                Optional.of(scheduleDay));
        when(travelRepository.save(any(TravelEntity.class))).thenReturn(travel);

        // when
        TravelResponse response = travelService.modifyTravel(schedule.getId(), travel.getId(),
                user.getId(),
                request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getLocation()).isEqualTo("서울타워");
    }

    @Test
    @DisplayName("행선지 수정 - 실패 (스케줄 날짜가 존재하지 않음)")
    void modifyTravelFail() {
        // given
        CalendarEntity wrongCalendar = CalendarEntity.builder()
                .id(2L)
                .calendarTitle("잘못된 캘린더")
                .build();

        ScheduleRequest wrongScheduleRequest = ScheduleRequest.builder()
                .scheduleTitle("다른 일정")
                .startDate(LocalDate.of(2025, 5, 1))
                .endDate(LocalDate.of(2025, 5, 2))
                .build();

        ScheduleEntity wrongSchedule = ScheduleEntity.of(wrongCalendar, wrongScheduleRequest);
        ReflectionTestUtils.setField(wrongSchedule, "id", 2L);

        // 기존 scheduleDay는 여전히 잘못된 스케줄에 연결됨
        scheduleDay.setSchedule(wrongSchedule);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(scheduleRepository.findById(schedule.getId())).thenReturn(Optional.of(schedule));
        when(travelRepository.findById(travel.getId())).thenReturn(Optional.of(travel));
        when(scheduleDayRepository.findById(scheduleDay.getId())).thenReturn(
                Optional.of(scheduleDay));

        // when & then
        assertThatThrownBy(() ->
                travelService.modifyTravel(schedule.getId(), travel.getId(), user.getId(), request))
                .isInstanceOf(ServiceException.class)
                .hasMessage(ErrorType.SCHEDULE_DAY_NOT_FOUND.getMessage());
    }
}
package com.frend.planit.domain.calendar.service;

import static com.frend.planit.global.response.ErrorType.CALENDAR_NOT_FOUND;
import static com.frend.planit.global.response.ErrorType.NOT_AUTHORIZED;

import com.frend.planit.domain.calendar.dto.request.CalendarRequestDto;
import com.frend.planit.domain.calendar.dto.response.CalendarActivityResponseDto;
import com.frend.planit.domain.calendar.dto.response.CalendarResponseDto;
import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.repository.CalendarRepository;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.global.response.ErrorType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private static <T> T orThrow(Optional<T> optional, ErrorType errorType) {
        return optional.orElseThrow(() -> errorType.serviceException());
    }

    private final CalendarRepository calendarRepository;
    private final CalendarPermissionValidator calendarPermissionValidator;
    private final SharedCalendarService sharedCalendarService;

    //캘린더 생성
    @Transactional
    public CalendarResponseDto createCalendar(CalendarRequestDto requestDto, User user) {
        CalendarEntity calendar = CalendarEntity.fromDto(requestDto, user);
        CalendarEntity saved = calendarRepository.save(calendar);
        return new CalendarResponseDto(saved);
    }

    // 캘린더 단건 조회
    @Transactional(readOnly = true)
    public CalendarResponseDto getCalendar(Long id) {
        CalendarEntity calendar = orThrow(calendarRepository.findById(id), CALENDAR_NOT_FOUND);
        return new CalendarResponseDto(calendar);
    }

    // 전체 캘린더 조회
    @Transactional(readOnly = true)
    public Page<CalendarResponseDto> getCalendars(Pageable pageable) {
        return calendarRepository.findAll(pageable).map(CalendarResponseDto::new);
    }

    // 캘린더 수정 (생성자, 공유자 가능)
    @Transactional
    public CalendarResponseDto updateCalendar(Long id, CalendarRequestDto requestDto, User user) {
        CalendarEntity calendar = orThrow(calendarRepository.findById(id), CALENDAR_NOT_FOUND);
        if (!calendarPermissionValidator.hasModifyAccess(calendar, user)) {
            throw NOT_AUTHORIZED.serviceException();
        }

        calendar.updateCalendar(requestDto);
        return new CalendarResponseDto(calendar);
    }

    // 캘린더 삭제 (생성자만 가능)
    @Transactional
    public void deleteCalendar(Long id, User user) {
        CalendarEntity calendar = orThrow(calendarRepository.findById(id), CALENDAR_NOT_FOUND);
        if (!calendarPermissionValidator.isOwner(calendar, user)) {
            throw NOT_AUTHORIZED.serviceException();
        }

        calendarRepository.delete(calendar);
    }

    // 내 활동 내역 조회 (캘린더)
    @Transactional(readOnly = true)
    public List<CalendarActivityResponseDto> getUserCalendarActivity(User user) {
        // 내가 소유한 캘린더 조회
        List<CalendarEntity> myCalendars = calendarRepository.findAllByUser(user);

        // 내가 공유받은 캘린더 조회
        List<CalendarEntity> sharedCalendars = sharedCalendarService.getSharedCalendars(user);

        // 소유 + 공유 리스트 병합
        List<CalendarEntity> allCalendars = new ArrayList<>();
        allCalendars.addAll(myCalendars);
        allCalendars.addAll(sharedCalendars);

        // CalendarActivityResponseDto로 변환
        return allCalendars.stream()
                .map(calendar -> CalendarActivityResponseDto.builder()
                        .calendarId(calendar.getId())
                        .calendarTitle(calendar.getCalendarTitle())
                        .note(calendar.getNote())
                        .startDate(calendar.getStartDate())
                        .endDate(calendar.getEndDate())
                        .sharedUserNicknames(
                                calendar.getSchedules().stream()
                                        .flatMap(schedule -> schedule.getCalendar().getSchedules()
                                                .stream())
                                        .map(schedule -> schedule.getCalendar().getUser()
                                                .getNickname())
                                        .distinct()
                                        .toList()
                        )
                        .build())
                .toList();
    }
}
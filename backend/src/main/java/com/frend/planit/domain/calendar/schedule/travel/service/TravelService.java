package com.frend.planit.domain.calendar.schedule.travel.service;

import com.frend.planit.domain.calendar.schedule.day.entity.ScheduleDayEntity;
import com.frend.planit.domain.calendar.schedule.day.repository.ScheduleDayRepository;
import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import com.frend.planit.domain.calendar.schedule.repository.ScheduleRepository;
import com.frend.planit.domain.calendar.schedule.travel.dto.request.TravelRequest;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.AllTravelsResponse;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.DailyTravelResponse;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.TravelResponse;
import com.frend.planit.domain.calendar.schedule.travel.entity.TravelEntity;
import com.frend.planit.domain.calendar.schedule.travel.repository.TravelRepository;
import com.frend.planit.domain.calendar.schedule.travel.travelUtils.TravelGroupingUtils;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleDayRepository scheduleDayRepository;
    private final UserRepository userRepository;

    // 행선지 조회
    @Transactional(readOnly = true)
    public AllTravelsResponse getAllTravels(Long scheduleId, Long userId) {

        // 로그인 사용자 확인
        checkUser(userId);

        // 스케줄 존재 여부 확인
        ScheduleEntity schedule = checkSchedule(scheduleId);

        // 행선지 조회
        List<TravelEntity> travels = findTravelByScheduleId(scheduleId);

        // 일일 행선지 조회
        List<DailyTravelResponse> dailyTravels = TravelGroupingUtils.groupTravelsByDate(travels);

        // 일자 별 ID 조회
        List<Long> scheduleDayIds = scheduleDayRepository.findAllByScheduleId(scheduleId)
                .stream()
                .map(ScheduleDayEntity::getId)
                .toList();

        return AllTravelsResponse.of(schedule, scheduleDayIds, dailyTravels);
    }

    // 행선지 생성
    @Transactional
    public TravelResponse createTravel(Long scheduleId, Long userId, TravelRequest travelRequest) {
        // 로그인 사용자 확인
        checkUser(userId);

        // 스케줄 존재 여부 확인
        checkSchedule(scheduleId);

        // 특정 스케줄의 날짜 존재 여부 확인
        ScheduleDayEntity scheduleDay = findScheduleDayById(travelRequest);

        // TravelEntity 생성
        TravelEntity travel = TravelEntity.of(travelRequest, scheduleDay);

        TravelEntity createdTravel = travelRepository.save(travel);

        return TravelResponse.from(createdTravel);
    }

    // 행선지 삭제
    @Transactional
    public TravelResponse deleteTravel(Long scheduleId, Long travelId, Long userId) {
        // 로그인 사용자 확인
        checkUser(userId);

        // 스케줄 존재 여부 확인
        checkSchedule(scheduleId);

        // 행선지 존재 여부 확인
        TravelEntity travel = findTravelById(travelId);

        // travel에 연결된 scheduleDay가 해당 schedule 소속인지 검증
        checkScheduleDay(travel, scheduleId);

        // 행선지 삭제
        travelRepository.delete(travel);

        return TravelResponse.from(travel);
    }

    // 행선지 수정
    @Transactional
    public TravelResponse modifyTravel(Long scheduleId, Long travelId, Long userId,
            TravelRequest travelRequest) {
        // 로그인 사용자 확인
        checkUser(userId);

        // 스케줄 존재 여부 확인
        checkSchedule(scheduleId);

        // 행선지 존재 여부 확인
        TravelEntity travel = findTravelById(travelId);

        // 특정 스케줄의 날짜 존재 여부 확인
        ScheduleDayEntity scheduleDay = findScheduleDayById(travelRequest);

        // travel에 연결된 scheduleDay가 해당 schedule 소속인지 검증
        checkScheduleDay(travel, scheduleId);

        // TravelEntity 수정
        travel.updateTravel(travelRequest, scheduleDay);

        TravelEntity updatedTravel = travelRepository.save(travel);

        return TravelResponse.from(updatedTravel);
    }

    // 공통 메서드

    // 스케줄 존재 여부 확인
    public ScheduleEntity checkSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));
    }

    // 단일 행선지 조뢰
    public TravelEntity findTravelById(Long travelId) {
        // 행선지 존재 여부 확인
        return travelRepository.findById(travelId)
                .orElseThrow(() -> new ServiceException(ErrorType.TRAVEL_NOT_FOUND));
    }

    // 전체 행선지 조회
    public List<TravelEntity> findTravelByScheduleId(Long scheduleId) {
        // 행선지 조회
        List<TravelEntity> travels = travelRepository.findAllByScheduleId(scheduleId);

        return travels;
    }

    // 특정 스케줄의 날짜 존재 여부 확인
    public ScheduleDayEntity findScheduleDayById(TravelRequest travelRequest) {
        // 특정 스케줄의 날짜 존재 여부 확인
        ScheduleDayEntity scheduleDay = scheduleDayRepository.findById(
                        travelRequest.getScheduleDayId())
                .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_DAY_NOT_FOUND));

        return scheduleDay;

    }

    // tavel에 연결된 scheduleDay가 해당 schedule 소속인지 검증
    public void checkScheduleDay(TravelEntity travel, Long scheduleId) {
        if (!travel.getScheduleDay().getSchedule().getId().equals(scheduleId)) {
            throw new ServiceException(ErrorType.SCHEDULE_DAY_NOT_FOUND);
        }
    }

    // 인증된 사용자 여부 확인
    public void checkUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorType.USER_NOT_FOUND));
    }
}
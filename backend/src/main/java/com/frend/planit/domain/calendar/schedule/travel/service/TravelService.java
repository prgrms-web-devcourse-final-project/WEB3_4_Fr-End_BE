package com.frend.planit.domain.calendar.schedule.travel.service;

import com.frend.planit.domain.calendar.schedule.day.entity.ScheduleDayEntity;
import com.frend.planit.domain.calendar.schedule.day.repository.ScheduleDayRepository;
import com.frend.planit.domain.calendar.schedule.repository.ScheduleRepository;
import com.frend.planit.domain.calendar.schedule.travel.dto.request.TravelRequest;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.DailyTravelResponse;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.TravelResponse;
import com.frend.planit.domain.calendar.schedule.travel.entity.TravelEntity;
import com.frend.planit.domain.calendar.schedule.travel.repository.TravelRepository;
import com.frend.planit.domain.calendar.schedule.travel.travelUtils.TravelGroupingUtils;
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

    // 행선지 조회
    @Transactional(readOnly = true)
    public List<DailyTravelResponse> getAllTravels(Long scheduleId) {
        // 스케줄 존재 여부 확인
        scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // 행선지 조회
        List<TravelEntity> travels = travelRepository.findAllByScheduleId(scheduleId);
        // 스케줄에 속한 행선지가 없는 경우 예외 처리
        if (travels.isEmpty()) {
            throw new ServiceException(ErrorType.TRAVEL_NOT_FOUND);
        }

        return TravelGroupingUtils.groupTravelsByDate(travels);
    }

    // 행선지 생성
    @Transactional
    public TravelResponse createTravel(Long scheduleId, TravelRequest travelRequest) {
        // 스케줄 존재 여부 확인
        scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // 특정 스케줄의 날짜 존재 여부 확인
        ScheduleDayEntity scheduleDay = scheduleDayRepository.findById(
                        travelRequest.getScheduleDayId())
                .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_DAY_NOT_FOUND));

        // TravelEntity 생성
        TravelEntity travel = TravelEntity.of(travelRequest, scheduleDay);

        TravelEntity createdTravel = travelRepository.save(travel);

        return TravelResponse.from(createdTravel);
    }

    // 행선지 삭제
    public void deleteTravel(Long scheduleId, Long travelId) {
        // 스케줄 존재 여부 확인
        scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // 행선지 존재 여부 확인
        TravelEntity travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new ServiceException(ErrorType.TRAVEL_NOT_FOUND));

        // travel에 연결된 scheduleDay가 해당 schedule 소속인지 검증
        if (!travel.getScheduleDay().getSchedule().getId().equals(scheduleId)) {
            throw new ServiceException(ErrorType.SCHEDULE_DAY_NOT_FOUND);
        }

        // 행선지 삭제
        travelRepository.delete(travel);
    }

    // 행선지 수정
    public TravelResponse modifyTravel(Long scheduleId, Long travelId,
            TravelRequest travelRequest) {
        // 스케줄 존재 여부 확인
        scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // 행선지 존재 여부 확인
        TravelEntity travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new ServiceException(ErrorType.TRAVEL_NOT_FOUND));

        // 특정 스케줄의 날짜 존재 여부 확인
        ScheduleDayEntity scheduleDay = scheduleDayRepository.findById(
                        travelRequest.getScheduleDayId())
                .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_DAY_NOT_FOUND));

        // travel에 연결된 scheduleDay가 해당 schedule 소속인지 검증
        if (!travel.getScheduleDay().getSchedule().getId().equals(scheduleId)) {
            throw new ServiceException(ErrorType.SCHEDULE_DAY_NOT_FOUND);
        }

        // TravelEntity 수정
        travel.updateTravel(travelRequest, scheduleDay);

        TravelEntity updatedTravel = travelRepository.save(travel);

        return TravelResponse.from(updatedTravel);
    }
}

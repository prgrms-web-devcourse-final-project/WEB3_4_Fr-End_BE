package com.frend.planit.global.config;

import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.repository.CalendarRepository;
import com.frend.planit.domain.calendar.schedule.day.entity.ScheduleDayEntity;
import com.frend.planit.domain.calendar.schedule.dto.request.ScheduleRequest;
import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import com.frend.planit.domain.calendar.schedule.repository.ScheduleRepository;
import com.frend.planit.domain.calendar.schedule.travel.dto.request.TravelRequest;
import com.frend.planit.domain.calendar.schedule.travel.entity.TravelEntity;
import com.frend.planit.domain.calendar.schedule.travel.repository.TravelRepository;
import com.frend.planit.domain.mateboard.comment.entity.MateComment;
import com.frend.planit.domain.mateboard.comment.repository.MateCommentRepository;
import com.frend.planit.domain.mateboard.post.entity.Mate;
import com.frend.planit.domain.mateboard.post.entity.MateGender;
import com.frend.planit.domain.mateboard.post.entity.TravelRegion;
import com.frend.planit.domain.mateboard.post.repository.MateRepository;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev", "local"})
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final MateRepository mateRepository;
    private final MateCommentRepository mateCommentRepository;

    // 여행 일정
    private final ScheduleRepository scheduleRepository;
    private final TravelRepository travelRepository;

    @Override
    public void run(ApplicationArguments args) {
        Optional<User> optionalUser = userRepository.findById(1L);
        if (optionalUser.isEmpty()) return;

        User user = optionalUser.get();

        if (calendarRepository.count() == 0) {
            CalendarEntity calendar = CalendarEntity.builder()
                    .calendarTitle("테스트 캘린더1")
                    .startDate(LocalDateTime.now().minusDays(1))
                    .endDate(LocalDateTime.now().plusDays(1))
                    .note("테스트 노트1")
                    .user(user)
                    .build();

            calendarRepository.save(calendar);
        }

        if (mateRepository.count() == 0) {
            Mate mate = new Mate();
            mate.setWriter(user);
            mate.setTitle("테스트 메이트 모집글1");
            mate.setContent("테스트 메이트 모집글 내용1");
            mate.setTravelRegion(TravelRegion.BUSAN);
            mate.setTravelStartDate(LocalDate.now().minusDays(1));
            mate.setTravelEndDate(LocalDate.now().plusDays(1));
            mate.setMateGender(MateGender.MALE);
            mate.setRecruitCount(5);

            mateRepository.save(mate);
        }

        if (mateCommentRepository.count() == 0) {
            MateComment mateComment = new MateComment();
            mateComment.setMate(mateRepository.findById(1L).orElse(null));
            mateComment.setUser(user);
            mateComment.setContent("테스트 댓글 내용1");
        }

        // 스케줄 생성
        if (scheduleRepository.count() == 0) {
            // 캘린더 가져오기
            CalendarEntity calendar = calendarRepository.findById(1L)
                    .orElseThrow(() -> new ServiceException(ErrorType.CALENDAR_NOT_FOUND));
            // 스케줄 생성
            ScheduleRequest request = ScheduleRequest.builder()
                    .scheduleTitle("테스트 일정 제목")
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(2))
                    .note("테스트 일정 노트")
                    .alertTime(LocalTime.now())
                    .build();

            ScheduleEntity schedule = ScheduleEntity.of(calendar, request);

            scheduleRepository.save(schedule);
        }

        // 여행 일정 생성
        if (travelRepository.count() == 0) {
            // 스케줄 가져오기
            ScheduleEntity schedule = scheduleRepository.findById(1L)
                    .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

            // 여행 일정 생성
            for (ScheduleDayEntity day : schedule.getScheduleDayList()) {
                TravelRequest travelRequest = TravelRequest.builder()
                        .scheduleDayId(day.getId()) // 생성 시점에 ID 없을 수 있음
                        .kakaomapId("kakao-" + day.getDate())
                        .location("서울역 " + day.getDate())
                        .category("여행")
                        .lat(37.5)
                        .lng(126.97)
                        .hour("10")
                        .minute("00")
                        .build();

                TravelEntity travel = TravelEntity.of(travelRequest, day); // 연관관계 자동 설정

                travelRepository.save(travel);
            }
        }
    }
}
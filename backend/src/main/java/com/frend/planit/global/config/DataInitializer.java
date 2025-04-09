package com.frend.planit.global.config;

import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.repository.CalendarRepository;
import com.frend.planit.domain.mateboard.comment.entity.MateComment;
import com.frend.planit.domain.mateboard.comment.repository.MateCommentRepository;
import com.frend.planit.domain.mateboard.post.entity.Mate;
import com.frend.planit.domain.mateboard.post.entity.MateGender;
import com.frend.planit.domain.mateboard.post.entity.TravelRegion;
import com.frend.planit.domain.mateboard.post.repository.MateRepository;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.enums.Gender;
import com.frend.planit.domain.user.enums.LoginType;
import com.frend.planit.domain.user.enums.SocialType;
import com.frend.planit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Profile({"dev", "local"})
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final MateRepository mateRepository;
    private final MateCommentRepository mateCommentRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() == 0) {
            User user1 = User.builder()
                    .socialId("test1")
                    .socialType(SocialType.NAVER)
                    .loginType(LoginType.SOCIAL)
                    .build();

            user1.updateFirstInfo(
                    "test1@naver.com",
                    "테스트유저1",
                    "010-1234-5678",
                    LocalDate.now(),
                    Gender.MALE);

            userRepository.save(user1);
        }

        if (calendarRepository.count() == 0) {
            User user = userRepository.findById(1L).orElseThrow(); // 존재하는 테스트 유저

            CalendarEntity calendar = CalendarEntity.builder()
                    .calendarTitle("테스트 캘린더1")
                    .startDate(LocalDateTime.now().minusDays(1))
                    .endDate(LocalDateTime.now().plusDays(1))
                    .note("테스트 노트1")
                    .user(user) // 사용자 정보 추가
                    .build();

            calendarRepository.save(calendar);
        }
        if (mateRepository.count() == 0) {
            Mate mate = new Mate();
            mate.setWriter(userRepository.findById(1L).orElse(null));
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
            mateComment.setUserId(1L);
            mateComment.setNickname("테스트유저1");
            mateComment.setContent("테스트 댓글 내용1");
        }
    }
}
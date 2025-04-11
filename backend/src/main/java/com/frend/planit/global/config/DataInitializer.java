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
import com.frend.planit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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
    }
}
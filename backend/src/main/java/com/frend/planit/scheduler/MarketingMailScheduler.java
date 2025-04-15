package com.frend.planit.scheduler;

import com.frend.planit.domain.mail.service.MailService;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketingMailScheduler {

    private final MailService mailService;
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 9 * * MON") // 매주 월요일 오전 9시
    public void sendMarketingMail() {
        List<User> users = userRepository.findByMailingTypeTrue();
        log.info("{}명에게 메일 발송 시작", users.size());

        for (User user : users) {
            mailService.sendMarketingMail(
                    user.getEmail(),
                    "[Planit 주간 여행 뉴스레터] ✈️ 이번 주 인기 여행지는 어디일까요?",
                    "안녕하세요 Planit입니다!\n\n이번 주 추천 여행지는 제주도입니다. 지금 바로 메이트를 찾아보세요!"
            );
        }

        log.info("메일 발송 완료");
    }
}
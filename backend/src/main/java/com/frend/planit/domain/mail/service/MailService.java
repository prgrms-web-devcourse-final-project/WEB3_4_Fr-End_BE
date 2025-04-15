package com.frend.planit.domain.mail.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendMarketingMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    @PostConstruct
    public void testSendMail() {
        sendMarketingMail(
                "jjzzangjaei@gmail.com",        // 수신자
                "[Planit 주간 여행 뉴스레터] ✈ 이번 주 인기 여행지는 어디일까요?\"",
                "안녕하세요, Planit 여행자 여러분! \uD83C\uDF0D\n"
                        + "\n"
                        + "이번 주도 여행 계획하고 계신가요?\n"
                        + "Planit이 추천하는 ✨이번 주 인기 여행지✨는 바로… \uD83C\uDFDD\uFE0F **제주도**입니다!\n"
                        + "\n"
                        + "푸른 바다, 봄 향기 가득한 올레길, 그리고 유채꽃이 만개한 들판까지!  \n"
                        + "따뜻한 햇살과 함께 떠나는 제주 여행, 상상만 해도 설레지 않나요?\n"
                        + "\n"
                        + "\uD83C\uDFAF 추천 일정\n"
                        + "- 1일차: 협재 해수욕장 → 오설록 티뮤지엄 → 야시장\n"
                        + "- 2일차: 성산 일출봉 → 섭지코지 → 카페 투어\n"
                        + "\n"
                        + "\uD83D\uDCA1 지금 바로 [Planit]에서 동행 메이트를 모집하고,\n"
                        + "합리적인 숙소와 교통편도 함께 예약해 보세요!\n"
                        + "\n"
                        + "즐거운 한 주 되시고, 다음 뉴스레터에서 만나요 ☺\uFE0F\n"
                        + "\n"
                        + "— Planit 드림"
        );
    }
}

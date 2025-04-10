package com.frend.planit.domain.accommodation.scheduler;

import com.frend.planit.domain.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccommodationSyncScheduler {

    private final AccommodationService accommodationService;

    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Seoul") // 서울시간 기준으로 매일 06:00에 자동 동기화합니다!
    public void syncDaily() {
        try {
            log.info("TourAPI : 일일 자동 숙소 동기화 시작");

            accommodationService.syncFromTourApi();  //TourAPI 호출 및 저장

            log.info("TourAPI : 일일 동기화 완료.");
        } catch (Exception e) {
            log.error("TourAPI : 일일 동기화 실패", e);
        }
    }
}
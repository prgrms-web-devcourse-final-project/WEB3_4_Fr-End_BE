package com.frend.planit.domain.accommodation.scheduler;

import com.frend.planit.domain.accommodation.client.TourApiClient;
import com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto;
import com.frend.planit.domain.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccommodationSyncScheduler {

    private final TourApiClient tourApiClient;
    private final AccommodationService accommodationService;

    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Seoul") // 서울시간기준으로 매일 06:00에 자동 동기화합니다!
    public void syncDaily() {
        try {
            log.info("TourAPI : 일일 자동 숙소 동기화 시작");

            List<AccommodationRequestDto> dtos = tourApiClient.fetchAccommodations();  // TourAPI에서 숙소 데이터 가져오기
            accommodationService.syncFromTourApi(dtos);  // DB에 동기화

            log.info("TourAPI : 일일 동기화 완료. 동기화된 숙소 수: {}", dtos.size());
        } catch (Exception e) {
            log.error("TourAPI : 일일 동기화 실패", e);
        }
    }
}
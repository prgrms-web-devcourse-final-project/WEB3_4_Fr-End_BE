package com.frend.planit.domain.accommodation.scheduler;

import com.frend.planit.domain.accommodation.client.TourApiClient;
import com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto;
import com.frend.planit.domain.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TourApiScheduler {

    private final TourApiClient tourApiClient;
    private final AccommodationService accommodationService;

    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Seoul")
    public void fetchTourAccommodations() {
        log.info("TourAPI 숙소데이터 동기화중입니다.");

        List<AccommodationRequestDto> accommodations = tourApiClient.fetchAccommodations();

        accommodationService.syncFromTourApi(accommodations);

        log.info("TourAPI 숙소 동기화 완료. 총 {}건의 데이터를 불러왔습니다.", accommodations.size());
    }
}
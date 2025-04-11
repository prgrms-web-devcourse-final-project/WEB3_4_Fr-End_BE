package com.frend.planit.domain.accommodation.loader;

import com.frend.planit.domain.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccommodationInitialDataLoader implements ApplicationRunner {

    private final AccommodationService accommodationService;

    @Override
    public void run(ApplicationArguments args) {
        if (accommodationService.isEmpty()) {
            log.info("TourAPI : 숙소 DB 비어 있음 → 초기 동기화 수행 시작");
            accommodationService.syncFromTourApi();
            log.info("TourAPI : 초기 숙소 동기화 완료");
        } else {
            log.info("TourAPI : 초기 동기화 생략 - 기존 숙소 데이터 존재함");
        }
    }
}
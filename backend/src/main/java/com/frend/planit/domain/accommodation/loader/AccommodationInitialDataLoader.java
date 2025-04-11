package com.frend.planit.domain.accommodation.loader;

import com.frend.planit.domain.accommodation.service.AccommodationService;
<<<<<<< HEAD
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
=======
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
>>>>>>> e3400e7444c6dd649f362884cd5cd6fd9ac0844a
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
<<<<<<< HEAD
public class AccommodationInitialDataLoader implements ApplicationRunner {
=======
@Profile("!test")
public class AccommodationInitialDataLoader {
>>>>>>> e3400e7444c6dd649f362884cd5cd6fd9ac0844a

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
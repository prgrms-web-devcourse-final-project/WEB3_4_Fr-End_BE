package com.frend.planit.domain.accommodation.init;

import com.frend.planit.domain.accommodation.client.TourApiClient;
import com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto;
import com.frend.planit.domain.accommodation.repository.AccommodationRepository;
import com.frend.planit.domain.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 현재 매일 오전 06:00에 TourAPI에 업데이트된 새로운 내용을
 * Accommodation과 통신하여 DB에 저장한다.
 *
 * 그러나, 최초실행 시 DB가 비어있으므로
 * 수동으로 TourAPI의 데이터를 DB에 넣기 위한 코드.
 *
 * 이 작업은 매 실행마다 읽히지만, 최초 1회만 DB값을 받아오며
 * 그 후에는 이미 DB에 내용이 있다면 동작하지 않는다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccommodationInitialDataLoader implements CommandLineRunner {

    private final TourApiClient tourApiClient;
    private final AccommodationService accommodationService;
    private final AccommodationRepository repository;

    @Override
    public void run(String... args) {
        log.info("🏁 [InitialLoader] 숙소 데이터를 TourAPI로부터 받아 DB에 넣는중입니다.");

        // 안전장치: 기존 데이터가 이미 존재하면 실행하지 않음
        if (repository.count() > 0) {
            log.info("[InitialLoader] DB에 이미 숙소 데이터가 존재하므로 수동저장을 생략합니다.");
            return;
        }

        // TourAPI 호출 및 동기화
        List<AccommodationRequestDto> list = tourApiClient.fetchAccommodations();
        accommodationService.syncFromTourApi(list);

        log.info("[InitialLoader] 숙소 데이터 적재 완료. 총 {}건", list.size());
    }
}

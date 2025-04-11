package com.frend.planit.domain.accommodation.loader;

import com.frend.planit.domain.accommodation.client.TourApiClient;
import com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto;
import com.frend.planit.domain.accommodation.repository.AccommodationRepository;
import com.frend.planit.domain.accommodation.service.AccommodationService;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class AccommodationInitialDataLoader {

    private final TourApiClient tourApiClient;
    private final AccommodationService accommodationService;
    private final AccommodationRepository repository;

    @PostConstruct
    public void loadIfEmpty() {
        if (repository.count() == 0) {
            try {
                log.info("TourAPI : DB에 데이터가 없습니다. > 최초 동기화 시작");

                List<AccommodationRequestDto> dtos = tourApiClient.fetchAccommodations();
                accommodationService.syncFromTourApi(dtos);

                log.info("TourAPI : 최초 숙소 동기화 완료. 저장 건수: {}", dtos.size());
            } catch (Exception e) {
                log.error("TourAPI : 초기 데이터 로딩 실패", e);
            }
        } else {
            log.info("TourAPI : DB에 데이터 존재 > 초기 동기화를 진행하지 않겠습니다.");
        }
    }
}
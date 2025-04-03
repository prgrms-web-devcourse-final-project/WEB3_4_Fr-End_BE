package com.frend.planit.domain.accommodation.client;

import com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TourApiClient {

    public List<AccommodationRequestDto> fetchAccommodations() {
        // TODO: 실제 TourAPI 연동 로직 구현
        return List.of(
                new AccommodationRequestDto("호텔A", "서울", 100000, 10, "와이파이,조식"),
                new AccommodationRequestDto("모텔B", "부산", 50000, 5, "TV,에어컨")
        );
    }
}
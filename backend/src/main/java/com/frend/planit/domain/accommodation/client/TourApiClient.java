package com.frend.planit.domain.accommodation.client;

import com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TourApiClient { 

    @Value("${tourapi.base-url}")
    private String baseUrl;

    @Value("${tourapi.service-key}")
    private String serviceKey;

    private final RestClient restClient = RestClient.create();

    public List<AccommodationRequestDto> fetchAccommodations() {
        String listUrl = String.format(
                "%s?serviceKey=%s&MobileOS=ETC&MobileApp=AppTest&_type=json&numOfRows=100&pageNo=1",
                baseUrl, serviceKey);

        TourApiListResponse response = restClient.get()
                .uri(listUrl)
                .retrieve()
                .body(TourApiListResponse.class);

        List<AccommodationRequestDto> result = new ArrayList<>();

        if (response != null && response.response.body.items.item != null) {
            for (TourApiListResponse.Item item : response.response.body.items.item) {
                String description = fetchDescriptionByContentId(item.contentid);

                result.add(new AccommodationRequestDto(
                        item.title,
                        item.addr1,
                        "100000",
                        5,
                        "Wi-Fi,TV",
                        item.firstimage,
                        description
                ));
            }
        }
        return result;
    }

    private String fetchDescriptionByContentId(String contentId) {
        String detailUrl = String.format(
                "https://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=%s&MobileOS=ETC&MobileApp=AppTest&_type=json&contentId=%s&contentTypeId=32&overviewYN=Y",
                serviceKey, contentId);

        try {
            TourApiDetailResponse response = restClient.get()
                    .uri(detailUrl)
                    .retrieve()
                    .body(TourApiDetailResponse.class);

            if (response != null &&
                    response.response.body.items.item != null &&
                    !response.response.body.items.item.isEmpty()) {
                return response.response.body.items.item.get(0).overview;
            }
        } catch (Exception e) {
            System.err.println("[TourAPI] 상세 정보 호출 실패: " + e.getMessage());
        }
        return "";
    }

    // 내부 클래스: 목록 응답
    public static class TourApiListResponse {
        public Response response;

        public static class Response {
            public Body body;
        }

        public static class Body {
            public Items items;
        }

        public static class Items {
            public List<Item> item;
        }

        public static class Item {
            public String contentid;
            public String title;
            public String addr1;
            public String firstimage;
        }
    }

    // 내부 클래스: 상세 응답
    public static class TourApiDetailResponse {
        public Response response;

        public static class Response {
            public Body body;
        }

        public static class Body {
            public Items items;
        }

        public static class Items {
            public List<Item> item;
        }

        public static class Item {
            public String overview;
        }
    }
}
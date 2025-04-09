package com.frend.planit.domain.accommodation.client;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto;
import com.frend.planit.domain.accommodation.dto.tourapi.TourApiItem;
import com.frend.planit.domain.accommodation.dto.tourapi.TourApiResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TourApiClient {

    @Value("${tourapi.base-url}")
    private String baseUrl;

    @Value("${tourapi.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate;
    private final XmlMapper xmlMapper = new XmlMapper();

    @PostConstruct
    public void init() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.setMessageConverters(messageConverters);
    }

    public List<AccommodationRequestDto> fetchAccommodations() {
        List<AccommodationRequestDto> result = new ArrayList<>();

        List<Integer> areaCodes = List.of(1, 2, 3, 4, 5, 6, 7, 8, 31, 32, 33, 34, 35, 36, 37, 38, 39);

        log.info("TourAPI : 전국 숙소 정보 조회 시작합니다");

        for (int areaCode : areaCodes) {
            int page = 1;
            boolean hasMore = true;

            while (hasMore) {
                try {
                    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/areaBasedList1")
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("MobileOS", "ETC")
                            .queryParam("MobileApp", URLEncoder.encode("PlanIt", StandardCharsets.UTF_8))
                            .queryParam("numOfRows", 200)
                            .queryParam("pageNo", page)
                            .queryParam("contentTypeId", 32)
                            .queryParam("areaCode", areaCode)
                            .queryParam("_type", "xml")
                            .build(true)
                            .toUri();

                    String xml = restTemplate.getForObject(uri, String.class);

                    if (xml != null && xml.contains("SERVICE_KEY_IS_NOT_REGISTERED_ERROR")) {
                        log.error("TourAPI : 인증키 등록 오류 발생 > 포털에서 API 등록 상태 확인 필요");
                        return Collections.emptyList();
                    }

                    TourApiResponse response = xmlMapper.readValue(xml, TourApiResponse.class);
                    List<TourApiItem> items = response.getBody().getItems();

                    if (items == null || items.isEmpty()) {
                        hasMore = false;
                    } else {
                        List<AccommodationRequestDto> dtos = items.stream()
                                .map(TourApiItem::toDto)
                                .toList();

                        result.addAll(dtos);
                        page++;
                    }

                } catch (Exception e) {
                    log.error("TourAPI : 데이터 수신 실패 (지역코드: {}, 페이지: {}): {}", areaCode, page, e.getMessage());
                    hasMore = false;
                }
            }
        }

        log.info("TourAPI : 전국 숙소 데이터 조회 완료. 총 수신 건수: {}개", result.size());
        return result;
    }
}
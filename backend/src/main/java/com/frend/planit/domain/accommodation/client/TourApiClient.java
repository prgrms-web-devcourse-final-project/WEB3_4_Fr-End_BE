package com.frend.planit.domain.accommodation.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
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
        // 한글 깨짐 방지용 UTF-8 설정 추가
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.setMessageConverters(messageConverters);
    }

    public List<AccommodationRequestDto> fetchAccommodations() {
        List<AccommodationRequestDto> result = new ArrayList<>();

        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/areaBasedList1")
                    .queryParam("serviceKey", serviceKey)
                    .queryParam("MobileOS", "ETC")
                    .queryParam("MobileApp", URLEncoder.encode("PlanIt", StandardCharsets.UTF_8))
                    .queryParam("numOfRows", 200)
                    .queryParam("pageNo", 1)
                    .queryParam("contentTypeId", 32)
                    .queryParam("areaCode", 1)
                    .queryParam("_type", "xml")
                    .build(true)
                    .toUri();

            String xml = restTemplate.getForObject(uri, String.class);
            log.info("📦 [TourAPI] 응답 XML:\n{}", xml);

            if (xml != null && xml.contains("SERVICE_KEY_IS_NOT_REGISTERED_ERROR")) {
                log.error("[TourAPI] 인증키 등록 오류 발생 → 포털에서 API 등록 상태 확인 필요");
                return Collections.emptyList();
            }

            TourApiListResponse response = xmlMapper.readValue(xml, TourApiListResponse.class);

            if (response != null &&
                    response.body != null &&
                    response.body.items != null &&
                    response.body.items.item != null) {

                for (TourApiListResponse.Item item : response.body.items.item) {
                    result.add(new AccommodationRequestDto(
                            item.title,
                            item.addr1,
                            new BigDecimal("100000"),
                            5,
                            item.firstimage != null ? item.firstimage : "https://placehold.co/600x400?text=No+Image",
                            List.of("Wi-Fi", "TV")
                    ));
                }
            }

        } catch (Exception e) {
            log.error("[TourAPI] XML 파싱 또는 호출 실패", e);
        }

        return result;
    }

    // =================== 내부 DTO ===================

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TourApiListResponse {
        public Body body;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Body {
            public Items items;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Items {
            @JacksonXmlProperty(localName = "item")
            @JacksonXmlElementWrapper(useWrapping = false)
            public List<Item> item;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Item {
            public String contentid;
            public String title;
            public String addr1;
            public String firstimage;
        }
    }
}
package com.frend.planit.domain.accommodation.dto.request;

import java.math.BigDecimal;
import java.util.List;

public record AccommodationRequestDto(

        String name,
        String location,
        BigDecimal pricePerNight,
        Integer availableRooms,
        String mainImage,
        List<String> amenities,

        Integer areaCode,       // 지역 코드
        String cat3,            // 콘텐츠 소분류 코드
        Double mapX,            // 경도
        Double mapY,            // 위도
        String checkInTime,     // 체크인 시간
        String checkOutTime     // 체크아웃 시간

) {}
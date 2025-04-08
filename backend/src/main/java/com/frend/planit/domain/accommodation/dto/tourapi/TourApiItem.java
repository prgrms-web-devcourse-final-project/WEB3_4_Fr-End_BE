package com.frend.planit.domain.accommodation.dto.tourapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TourApiItem(

        @JacksonXmlProperty(localName = "title")
        String title,

        @JacksonXmlProperty(localName = "addr1")
        String addr1,

        @JacksonXmlProperty(localName = "firstimage")
        String firstImage,

        @JacksonXmlProperty(localName = "areacode")
        Integer areaCode,

        @JacksonXmlProperty(localName = "cat3")
        String cat3,

        @JacksonXmlProperty(localName = "mapx")
        Double mapX,

        @JacksonXmlProperty(localName = "mapy")
        Double mapY

) {
    public AccommodationRequestDto toDto() {
        BigDecimal randomPrice = BigDecimal.valueOf(50000 + new Random().nextInt(150000));
        List<String> amenities = List.of("WiFi", "세미나실", "에어컨", "헬스장");

        return new AccommodationRequestDto(
                title,
                addr1,
                randomPrice,
                5,
                firstImage != null ? firstImage : "https://placehold.co/600x400?text=No+Image",
                amenities,
                areaCode,
                cat3,
                mapX,
                mapY,
                "15:00",
                "11:00"
        );
    }
}
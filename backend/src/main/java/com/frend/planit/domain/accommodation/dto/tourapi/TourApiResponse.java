package com.frend.planit.domain.accommodation.dto.tourapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TourApiResponse(

        @JacksonXmlProperty(localName = "item")
        @JacksonXmlElementWrapper(useWrapping = false)
        List<TourApiItem> item

) {
        public List<AccommodationRequestDto> toAccommodationDtoList() {
                return item.stream().map(TourApiItem::toDto).toList();
        }
}
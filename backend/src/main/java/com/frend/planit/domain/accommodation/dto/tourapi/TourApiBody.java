package com.frend.planit.domain.accommodation.dto.tourapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TourApiBody(

        @JacksonXmlProperty(localName = "items")
        TourApiResponse items

) {}
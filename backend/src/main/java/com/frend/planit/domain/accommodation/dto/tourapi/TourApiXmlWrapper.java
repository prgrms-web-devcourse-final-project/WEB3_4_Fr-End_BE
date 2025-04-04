package com.frend.planit.domain.accommodation.dto.tourapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "response") // root 태그 명시
public record TourApiXmlWrapper(

        @JacksonXmlProperty(localName = "body")
        TourApiBody body

) {}
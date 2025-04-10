package com.frend.planit.domain.accommodation.dto.tourapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "response")
public class TourApiResponse {

        private final TourApiBody body;

        @JsonCreator
        public TourApiResponse(@JacksonXmlProperty(localName = "body") @JsonProperty("body") TourApiBody body) {
                this.body = body;
        }

        public TourApiBody getBody() {
                return body;
        }
}
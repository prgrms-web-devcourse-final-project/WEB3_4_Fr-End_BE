package com.frend.planit.domain.accommodation.dto.tourapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "response")
public class TourApiResponse {

        @JacksonXmlProperty(localName = "body")
        private TourApiBody body;

        public TourApiBody getBody() {
                return body;
        }

        public void setBody(TourApiBody body) {
                this.body = body;
        }
}
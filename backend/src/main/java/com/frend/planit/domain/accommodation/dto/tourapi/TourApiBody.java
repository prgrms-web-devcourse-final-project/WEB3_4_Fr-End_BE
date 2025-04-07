package com.frend.planit.domain.accommodation.dto.tourapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TourApiBody {

        private final List<TourApiItem> items;

        @JsonCreator
        public TourApiBody(
                @JacksonXmlElementWrapper(localName = "items")
                @JacksonXmlProperty(localName = "item")
                @JsonProperty("items")
                List<TourApiItem> items
        ) {
                this.items = items;
        }

        public List<TourApiItem> getItems() {
                return items;
        }
}
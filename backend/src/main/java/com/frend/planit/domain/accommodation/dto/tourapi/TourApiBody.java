package com.frend.planit.domain.accommodation.dto.tourapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TourApiBody {

        private final TourApiItems items;

        @JsonCreator
        public TourApiBody(@JsonProperty("items") TourApiItems items) {
                this.items = items;
        }

        public List<TourApiItem> getItems() {
                return items != null && items.item() != null
                        ? items.item()
                        : Collections.emptyList();
        }
}
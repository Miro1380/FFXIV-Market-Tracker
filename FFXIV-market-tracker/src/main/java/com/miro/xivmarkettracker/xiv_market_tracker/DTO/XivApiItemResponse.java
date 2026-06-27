package com.miro.xivmarkettracker.xiv_market_tracker.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class XivApiItemResponse {

    @JsonProperty("ID")
    private Long itemId;

    @JsonProperty("Name")
    private String itemName;

    @JsonProperty("IconHD")
    private String iconUrl;

    @JsonProperty("CanBeHq")
    private Boolean canBeHQ;

    @JsonProperty("StackSize")
    private Integer stackSize;

    @JsonProperty("PriceLow")
    private Integer vendor;
}

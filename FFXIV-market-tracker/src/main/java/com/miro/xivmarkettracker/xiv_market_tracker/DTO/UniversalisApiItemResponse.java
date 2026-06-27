package com.miro.xivmarkettracker.xiv_market_tracker.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UniversalisApiItemResponse {

    @JsonProperty("itemID")
    private Long id;

    @JsonProperty("worldName")
    private String world;

    @JsonProperty("currentAveragePrice")
    private BigDecimal avgPrice;

    @JsonProperty("minPrice")
    private BigDecimal minPrice;

    @JsonProperty("maxPrice")
    private BigDecimal maxPrice;

    @JsonProperty("listingsCount")
    private Integer listingCount;

    @JsonProperty("unitsSold")
    private Integer volumeSold;

    @JsonProperty("averagePriceNQ")
    private BigDecimal avgPriceNq;     // Universalis gives HQ/NQ averages separately

    @JsonProperty("averagePriceHQ")
    private BigDecimal avgPriceHq;

    @JsonProperty("lastUploadTime")
    private Long lastUploadTime;       // Unix epoch from Universalis — when data was last pushed


}

package com.miro.xivmarkettracker.xiv_market_tracker.DTO;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
public class PriceSnapshotResponseDTO {
    private Long id;
    private String itemName;

    private String world;
    private BigDecimal avgPrice;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer listingCount;
    private Integer volumeSold;

    private BigDecimal avgPriceNq;
    private BigDecimal avgPriceHq;
    private Long lastUploadTime;

    private LocalDateTime capturedAt;
}

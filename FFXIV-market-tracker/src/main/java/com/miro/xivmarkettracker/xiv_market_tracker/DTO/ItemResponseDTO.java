package com.miro.xivmarkettracker.xiv_market_tracker.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemResponseDTO {
    private Long itemId;
    private String itemName;
    private String iconUrl;
    private Boolean canBeHq;
    private Integer stackSize;
    private Integer vendorPrice;
}

package com.miro.xivmarkettracker.xiv_market_tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDTO {
    private Long itemId;
    private String itemName;
    private String iconUrl;
    private Boolean canBeHq;
    private Integer stackSize;
    private Integer vendorPrice;
}

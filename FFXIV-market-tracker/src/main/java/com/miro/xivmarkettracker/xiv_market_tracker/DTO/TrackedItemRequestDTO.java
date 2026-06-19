package com.miro.xivmarkettracker.xiv_market_tracker.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrackedItemRequestDTO {
    private Long userId;
    private Long itemId;
    private String world;
}

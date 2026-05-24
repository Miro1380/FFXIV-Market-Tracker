package com.miro.xivmarkettracker.xiv_market_tracker.DTO;

import lombok.Data;

@Data
public class TrackedItemRequestDTO {
    private Long userId;
    private Long itemId;
    private String world;
}

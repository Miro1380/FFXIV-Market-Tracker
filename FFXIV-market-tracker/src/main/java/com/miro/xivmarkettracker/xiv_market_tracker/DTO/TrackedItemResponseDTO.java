package com.miro.xivmarkettracker.xiv_market_tracker.DTO;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TrackedItemResponseDTO {
    private Long id;
    private Long userId;
    private Long itemId;
    private String world;
    private String itemName;
    private boolean isTracking;
    private LocalDateTime createdAt;
}

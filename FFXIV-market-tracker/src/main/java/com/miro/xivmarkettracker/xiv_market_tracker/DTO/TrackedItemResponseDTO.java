package com.miro.xivmarkettracker.xiv_market_tracker.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String iconUrl;
    @JsonProperty("isTracking")
    private boolean isTracking;
    private LocalDateTime createdAt;
}

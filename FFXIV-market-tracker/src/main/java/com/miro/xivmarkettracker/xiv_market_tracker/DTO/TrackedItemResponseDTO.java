package com.miro.xivmarkettracker.xiv_market_tracker.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackedItemResponseDTO {
    private Long id;
    private Long userId;
    private Long itemId;
    private String world;
    private String itemName;
    private String iconUrl;
    private Boolean canBeHq;
    @JsonProperty("isTracking")
    private boolean isTracking;
    private LocalDateTime createdAt;
}

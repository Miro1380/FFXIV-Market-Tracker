package com.miro.xivmarkettracker.xiv_market_tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackedItemRequestDTO {
    private Long userId;
    private Long itemId;
    private String world;
}

package com.miro.xivmarkettracker.xiv_market_tracker.DTO;

import com.miro.xivmarkettracker.xiv_market_tracker.entity.PriceAlertsEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class PriceAlertResponseDTO {
    private Long id;
    private Long itemId;
    private Long userId;
    private String itemName;
    private String world;
    private BigDecimal targetPrice;
    private PriceAlertsEntity.AlertCondition condition;
    private boolean isHq;
    private Integer triggerCount;
    private Boolean isActive;
    private LocalDateTime lastTriggerAt;
    private LocalDateTime createdAt;

}

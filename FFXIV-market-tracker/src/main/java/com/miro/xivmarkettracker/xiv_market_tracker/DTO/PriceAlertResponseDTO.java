package com.miro.xivmarkettracker.xiv_market_tracker.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.PriceAlertsEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceAlertResponseDTO {
    private Long id;
    private Long itemId;
    private Long userId;
    private String itemName;
    private String world;
    private BigDecimal targetPrice;
    private PriceAlertsEntity.AlertCondition alertCondition;

    @JsonProperty("isHq")
    private boolean isHq;
    private Integer triggerCount;
    private Boolean isActive;
    private LocalDateTime lastTriggerAt;
    private LocalDateTime createdAt;

}

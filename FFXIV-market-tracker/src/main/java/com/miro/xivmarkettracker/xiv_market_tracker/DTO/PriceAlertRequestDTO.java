package com.miro.xivmarkettracker.xiv_market_tracker.DTO;

import com.miro.xivmarkettracker.xiv_market_tracker.entity.PriceAlertsEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceAlertRequestDTO {

    private Long userId;
    private Long itemId;
    private String world;
    private BigDecimal targetPrice;
    private PriceAlertsEntity.AlertCondition condition;
    private Boolean isHq;
}

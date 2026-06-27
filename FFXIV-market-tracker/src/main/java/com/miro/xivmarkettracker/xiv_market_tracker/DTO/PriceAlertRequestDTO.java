package com.miro.xivmarkettracker.xiv_market_tracker.DTO;

import com.miro.xivmarkettracker.xiv_market_tracker.entity.PriceAlertsEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceAlertRequestDTO {

    private Long userId;
    private Long itemId;
    private String world;
    private BigDecimal targetPrice;
    private PriceAlertsEntity.AlertCondition alertCondition;
    private Boolean isHq;
}

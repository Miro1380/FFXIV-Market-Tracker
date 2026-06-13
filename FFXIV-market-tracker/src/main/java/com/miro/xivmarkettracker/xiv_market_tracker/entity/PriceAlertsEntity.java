package com.miro.xivmarkettracker.xiv_market_tracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="price_alerts")
public class PriceAlertsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id",nullable = false)
    private ItemEntity item;

    private String world;

    @Enumerated(EnumType.STRING)
    private AlertCondition alertCondition;

    private BigDecimal targetPrice;

    private Boolean isHq;                  // The type of item the alert is referencing. Hq or Nq? Needed to check correct field.
    private LocalDateTime lastTriggeredAt; // rename from triggeredAt — can fire multiple times

    @Builder.Default
    private Integer triggerCount = 0;         // nice-to-have for portfolio: shows alert history


    @Column(nullable = false,columnDefinition = "boolean default true")
    private boolean isActive;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum AlertCondition {
        BELOW, ABOVE
    }
}



package com.miro.xivmarkettracker.xiv_market_tracker.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="price_snapshots")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceSnapshotsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="item_id")
    private ItemEntity item;

    private String world;
    private BigDecimal avgPrice;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer listingCount;
    private Integer volumeSold;

    private BigDecimal avgPriceNq;     // Universalis gives HQ/NQ averages separately
    private BigDecimal avgPriceHq;
    private Long lastUploadTime;       // Unix epoch from Universalis — when data was last pushed


    @CreationTimestamp
    private LocalDateTime capturedAt;

}

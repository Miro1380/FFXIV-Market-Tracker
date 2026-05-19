package com.miro.xivmarkettracker.xiv_market_tracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemEntity {

    @Id
    @Column(name="item_id")
    private Long itemId;

    private String itemName;
    private String iconUrl;

    private Boolean canBeHq;
    private Integer stackSize;
    private Integer vendorPrice;

    @CreationTimestamp
    private LocalDateTime createdAt;
}

package com.miro.xivmarkettracker.xiv_market_tracker.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tracked_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "item_id", "world"}))
public class TrackedItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    private String world;
    private boolean isTracking;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity userId;

    @ManyToOne
    @JoinColumn(name="item_id",nullable = false)
    private ItemEntity itemId;

    private String world;
    private String condition;
    private BigDecimal targetPrice;
    @Column(nullable = false,columnDefinition = "boolean default true")
    private boolean isActive;
    private LocalDateTime triggeredAt;
    private LocalDateTime createdAt;

}

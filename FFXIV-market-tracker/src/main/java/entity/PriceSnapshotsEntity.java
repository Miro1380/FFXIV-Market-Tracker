package entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="price_snapshots")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceSnapshotsEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Integer itemId;

    private String world;
    private BigDecimal avgPrice;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer listingCount;
    private Integer volumeSold;
    private LocalDateTime capturedAt;

}

package entity;

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
    private Integer itemId;

    private String itemName;
    private String iconUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;
}

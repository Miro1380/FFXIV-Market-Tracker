package com.miro.xivmarkettracker.xiv_market_tracker.repository;

import com.miro.xivmarkettracker.xiv_market_tracker.entity.ItemEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.PriceSnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PriceSnapshotRepository extends JpaRepository<PriceSnapshotEntity, Long> {

    //All snapshots and info for an item on a world, chronologically
    List<PriceSnapshotEntity> findByItemAndWorldOrderByCapturedAtDesc(ItemEntity item, String world);

    Optional<PriceSnapshotEntity> findTopByItemAndWorldOrderByCapturedAtDesc(ItemEntity item, String world);

}

package com.miro.xivmarkettracker.xiv_market_tracker.repository;

import com.miro.xivmarkettracker.xiv_market_tracker.entity.ItemEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.PriceAlertsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriceAlertRepo extends JpaRepository<PriceAlertsEntity, Long> {

    List<PriceAlertsEntity> findByItemAndWorldAndIsActiveTrue(ItemEntity item, String world);

    List<PriceAlertsEntity> findByUserId(Long userId);
}

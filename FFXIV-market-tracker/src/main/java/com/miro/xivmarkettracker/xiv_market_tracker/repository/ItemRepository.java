package com.miro.xivmarkettracker.xiv_market_tracker.repository;

import com.miro.xivmarkettracker.xiv_market_tracker.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity,Long> {


}

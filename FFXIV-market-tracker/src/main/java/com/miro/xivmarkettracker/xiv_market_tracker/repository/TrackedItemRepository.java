package com.miro.xivmarkettracker.xiv_market_tracker.repository;

import com.miro.xivmarkettracker.xiv_market_tracker.entity.TrackedItemEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackedItemRepository extends JpaRepository<TrackedItemEntity,Long> {

    List<TrackedItemEntity> findByIsTrackingTrue();
    List<TrackedItemEntity> findByUser(UserEntity user);
    List<TrackedItemEntity> findByUserAndIsTrackingTrue(UserEntity user);
    //TrackedItemEntity findByUser();
}

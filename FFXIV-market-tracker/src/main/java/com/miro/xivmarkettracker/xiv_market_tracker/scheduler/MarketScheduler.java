package com.miro.xivmarkettracker.xiv_market_tracker.scheduler;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.PriceAlertResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.PriceSnapshotResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.TrackedItemEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.TrackedItemRepository;
import com.miro.xivmarkettracker.xiv_market_tracker.service.PriceAlertService;
import com.miro.xivmarkettracker.xiv_market_tracker.service.UniversalisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j

public class MarketScheduler {

    private final TrackedItemRepository trackedItemRepository;
    private final UniversalisService universalisService;
    private final PriceAlertService priceAlertService;

    //Testing every 15 minutes
    @Scheduled(fixedDelay = 900000)
    public void pollTrackedItems(){
        //Get all actively tracked items
        List<TrackedItemEntity> trackedItems = trackedItemRepository.findByIsTrackingTrue();

        //For each, fetch snapshot then check alerts
        for(TrackedItemEntity item : trackedItems){
            try{
                Long itemId = item.getItem().getItemId();
                String world = item.getWorld();

                //Get Live snapshot for current item, world
                PriceSnapshotResponseDTO priceSnapshot = universalisService.fetchAndSaveSnapshot(itemId, world);
                log.info("Snapshot saved for item {} on {}", itemId,world);

                //Get All alerts that are triggered.
                List<PriceAlertResponseDTO> triggered = priceAlertService.checkAlerts(itemId,world);

                if(!triggered.isEmpty()){
                    log.info("Triggered {} alerts for item {} on {}", triggered.size(),itemId,world);
                }
            } catch (Exception e) {
                log.error("Failed to poll item {} on {}:{}",
                        item.getItem().getItemId(), item.getWorld(), e.getMessage());
            }
        }
    }
}

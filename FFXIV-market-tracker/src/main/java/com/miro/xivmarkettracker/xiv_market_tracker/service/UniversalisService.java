package com.miro.xivmarkettracker.xiv_market_tracker.service;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.PriceSnapshotResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.UniversalisApiItemResponse;
import com.miro.xivmarkettracker.xiv_market_tracker.client.UniversalisApiClient;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.ItemEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.PriceSnapshotEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.exceptions.ResourceNotFoundException;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.ItemRepository;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.PriceSnapshotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UniversalisService {

    private final UniversalisApiClient universalisApiClient;
    private final PriceSnapshotRepository snapshotRepository;
    private final ItemRepository itemRepository;

    public PriceSnapshotResponseDTO fetchAndSaveSnapshot(Long itemId, String world){
        //Query db or get seed
        //Look up in universalis using
        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"+ itemId));

        UniversalisApiItemResponse response = universalisApiClient.getItem(world,itemId);

        PriceSnapshotEntity snapshot = PriceSnapshotEntity.builder()
                .item(item)
                .world(world)
                .avgPrice(response.getAvgPrice())
                .minPrice(response.getMinPrice())
                .maxPrice(response.getMaxPrice())
                .listingCount(response.getListingCount())
                .volumeSold(response.getVolumeSold())
                .avgPriceNq(response.getAvgPriceNq())
                .avgPriceHq(response.getAvgPriceHq())
                .lastUploadTime(response.getLastUploadTime())
                .build();

        return toPriceSnapshotDTO(snapshotRepository.save(snapshot));
    }

    //Get the item and pass that item to the snapshot repo. Return a list of price snapshot DTO
    public List<PriceSnapshotResponseDTO> getHistory(Long itemId, String world){
        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found: "+ itemId));
        return snapshotRepository.findByItemAndWorldOrderByCapturedAtDesc(item,world)
                .stream()
                .map(this::toPriceSnapshotDTO)
                .toList();
    }

    private PriceSnapshotResponseDTO toPriceSnapshotDTO(PriceSnapshotEntity priceSnapshotEntity){
        return PriceSnapshotResponseDTO.builder()
                .id(priceSnapshotEntity.getId())
                .itemName(priceSnapshotEntity.getItem().getItemName())
                .world(priceSnapshotEntity.getWorld())
                .avgPrice(priceSnapshotEntity.getAvgPrice())
                .minPrice(priceSnapshotEntity.getMinPrice())
                .maxPrice(priceSnapshotEntity.getMaxPrice())
                .listingCount(priceSnapshotEntity.getListingCount())
                .volumeSold(priceSnapshotEntity.getVolumeSold())
                .avgPriceNq(priceSnapshotEntity.getAvgPriceNq())
                .avgPriceHq(priceSnapshotEntity.getAvgPriceHq())
                .lastUploadTime(priceSnapshotEntity.getLastUploadTime())
                .capturedAt(priceSnapshotEntity.getCapturedAt())
                .build();
    }
}

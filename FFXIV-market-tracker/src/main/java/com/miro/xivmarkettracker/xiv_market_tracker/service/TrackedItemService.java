package com.miro.xivmarkettracker.xiv_market_tracker.service;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.TrackedItemRequestDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.TrackedItemResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.ItemEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.TrackedItemEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.UserEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.ItemRepository;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.TrackedItemRepository;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrackedItemService {
    private final TrackedItemRepository trackedItemRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    //Add CRUD Methods

    public TrackedItemResponseDTO addTrackedItem(TrackedItemRequestDTO dto){
        ItemEntity item = itemRepository.findById(dto.getItemId()).orElseThrow(() -> new RuntimeException("Item not found: "+ dto.getItemId()));
        UserEntity user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found: "+ dto.getUserId()));

        //Bind data to entity and save in repo
        TrackedItemEntity entity = TrackedItemEntity.builder()
                .user(user)
                .item(item)
                .world(dto.getWorld())
                .isTracking(true)
                .build();

        //Gets saved to db to generate fields
        TrackedItemEntity saved = trackedItemRepository.save(entity);

        //Return the entity after its been built in the db (what came back)
        return this.toResponseDTO(saved);
    }

    private TrackedItemResponseDTO toResponseDTO(TrackedItemEntity entity){
        return TrackedItemResponseDTO.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .itemId(entity.getItem().getItemId())
                .itemName(entity.getItem().getItemName())
                .world(entity.getWorld())
                .isTracking(entity.isTracking())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}

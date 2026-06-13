package com.miro.xivmarkettracker.xiv_market_tracker.service;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.TrackedItemRequestDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.TrackedItemResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.ItemEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.TrackedItemEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.UserEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.exceptions.DuplicateTrackedItemException;
import com.miro.xivmarkettracker.xiv_market_tracker.exceptions.ResourceNotFoundException;
import com.miro.xivmarkettracker.xiv_market_tracker.exceptions.UnauthorizedException;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.ItemRepository;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.TrackedItemRepository;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import javax.sound.midi.Track;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class TrackedItemService {
    private final TrackedItemRepository trackedItemRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;



    //Add TrackedItem
    public TrackedItemResponseDTO addTrackedItem(TrackedItemRequestDTO dto){
        //FOR DEBUG
        log.info("Checking for existing tracked item: userId={}, itemId={}, world={}",
                dto.getUserId(), dto.getItemId(), dto.getWorld());
        //

        ItemEntity item = itemRepository.findById(dto.getItemId()).orElseThrow(() -> new ResourceNotFoundException("Item not found: "+ dto.getItemId()));
        UserEntity user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found: "+ dto.getUserId()));

        //Check DB for Previous entry. Return it if found.
        Optional<TrackedItemEntity> existing = trackedItemRepository.findByUserIdAndItemItemIdAndWorld(dto.getUserId(),dto.getItemId(),dto.getWorld());
        if(existing.isPresent()){
            //Reactivate Items. TODO
            TrackedItemEntity entity = existing.get();
            if(!entity.isTracking()){
                entity.setTracking(true);
                return toResponseDTO(trackedItemRepository.save(entity));
            }
            throw new DuplicateTrackedItemException(toResponseDTO(existing.get()));
        }

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

    //Get All Tracked Items that are currently being tracked (true)
    public List<TrackedItemResponseDTO> getTrackedItems(Long userId){
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found: "+ userId));

        return trackedItemRepository.findByUserAndIsTrackingTrue(user)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    //Update (toggle isTracking?)
    public void updateTrackedItem(Long userId, Long id){
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found: "+ userId));
        TrackedItemEntity entity = trackedItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tracked Item not found: "+ id));

        if(!entity.getUser().getId().equals(user.getId())){
            throw new UnauthorizedException("Unauthorized");
        }
        entity.setTracking(!entity.isTracking());
        trackedItemRepository.save(entity);
    }

    //Delete TrackedItem
    public void deleteTrackedItem(Long userId, Long id){
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User not found: "+ userId));

        TrackedItemEntity trackedItem = trackedItemRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Tracked item not found: "+ id));

        if(!user.getId().equals(trackedItem.getUser().getId())){
            throw new UnauthorizedException("Unauthorized");
        }

        trackedItemRepository.delete(trackedItem);
    }

    private TrackedItemResponseDTO toResponseDTO(TrackedItemEntity entity){
        return TrackedItemResponseDTO.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .itemId(entity.getItem().getItemId())
                .itemName(entity.getItem().getItemName())
                .iconUrl(entity.getItem().getIconUrl())
                .world(entity.getWorld())
                .isTracking(entity.isTracking())
                .createdAt(entity.getCreatedAt())
                .canBeHq(entity.getItem().getCanBeHq())
                .build();
    }

}

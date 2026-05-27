package com.miro.xivmarkettracker.xiv_market_tracker.service;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.PriceAlertRequestDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.PriceAlertResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.ItemEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.PriceAlertsEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.PriceSnapshotEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.UserEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.exceptions.ResourceNotFoundException;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.ItemRepository;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.PriceAlertRepo;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.PriceSnapshotRepository;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PriceAlertService {
    private final PriceAlertRepo priceAlertRepo;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final PriceSnapshotRepository priceSnapshotRepository;

    public PriceAlertResponseDTO createAlert(PriceAlertRequestDTO dto){
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User Id not found: "+ dto.getUserId()));

        ItemEntity item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item id not found: "+ dto.getItemId()));

        PriceAlertsEntity entity = PriceAlertsEntity.builder()
                .user(user)
                .item(item)
                .world(dto.getWorld())
                .condition(dto.getCondition())
                .targetPrice(dto.getTargetPrice())
                .isHq(dto.getIsHq())
                .isActive(true)
                .triggerCount(0)
                .build();

        //Save new alert in repo
        return toPriceAlertResponseDTO(priceAlertRepo.save(entity));
    }

    public List<PriceAlertResponseDTO> getAlertsByUser(Long userId){
        return priceAlertRepo.findByUserId(userId)
                .stream()
                .map(this::toPriceAlertResponseDTO)
                .toList();
    }

    public void deleteAlert(Long id){
        priceAlertRepo.deleteById(id);
    }

    public List<PriceAlertResponseDTO> checkAlerts(Long itemId, String world){
        //Use entities for service, return dto's on controller
        ItemEntity item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item not found: "+ itemId));

        PriceSnapshotEntity snapshot = priceSnapshotRepository.findTopByItemAndWorldOrderByCapturedAtDesc(item,world)
                .orElseThrow(() -> new ResourceNotFoundException("Item snapshot not found: "+ item));

        List<PriceAlertsEntity> activeAlerts = priceAlertRepo.findByItemAndWorldAndIsActiveTrue(item, world);

        //TODO Finish implementing. Needs comparison of hq or nq per item.
        List<PriceAlertResponseDTO> triggered = new ArrayList<>();

        for(PriceAlertsEntity alert: activeAlerts){
            BigDecimal comparePrice = alert.getIsHq()
                    ? snapshot.getAvgPriceHq()
                    : snapshot.getAvgPriceNq();

            //If the compare price is lower or higher than the target price, alert respectively
            //Check alert condition: Below or above from enums
            //If the alert's condition is "Below" and the price is below the target, trigger it. Change state.

            boolean fires = false;
            if(alert.getCondition() == PriceAlertsEntity.AlertCondition.BELOW && comparePrice.compareTo(alert.getTargetPrice()) < 0){
                fires = true;
            }else if(alert.getCondition() == PriceAlertsEntity.AlertCondition.ABOVE && comparePrice.compareTo(alert.getTargetPrice()) > 0){
                fires=true;
            }

            //Update lastTriggeredAt, increase trigger count, save to repo, and save to triggered list
            if(fires){
                alert.setLastTriggeredAt(LocalDateTime.now());
                alert.setTriggerCount(alert.getTriggerCount()+1);
                priceAlertRepo.save(alert);
                triggered.add(this.toPriceAlertResponseDTO(alert));
            }
        }
        return triggered;
    }

    private PriceAlertResponseDTO toPriceAlertResponseDTO(PriceAlertsEntity entity){
        return PriceAlertResponseDTO.builder()
                .id(entity.getId())
                .itemId(entity.getItem().getItemId())
                .userId(entity.getUser().getId())
                .itemName(entity.getItem().getItemName())
                .world(entity.getWorld())
                .targetPrice(entity.getTargetPrice())
                .condition(entity.getCondition())
                .isHq(entity.getIsHq())
                .triggerCount(entity.getTriggerCount())
                .isActive(entity.isActive())
                .lastTriggerAt(entity.getLastTriggeredAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }



}

package com.miro.xivmarkettracker.xiv_market_tracker.service;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.PriceAlertRequestDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.PriceAlertResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.ItemEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.PriceAlertsEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.UserEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.ItemRepository;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.PriceAlertRepo;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.PriceSnapshotRepository;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PriceAlertServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private PriceAlertRepo priceAlertRepo;

    @Mock
    private PriceSnapshotRepository priceSnapshotRepository;

    @InjectMocks
    private PriceAlertService priceAlertService;

    @Test
    @DisplayName("Create Alert: Valid userId, valid Item Id")
    void createAlert_validCredentials(){
        UserEntity user = UserEntity.builder()
                .id(2L)
                .build();

        ItemEntity item = ItemEntity.builder()
                .itemId(23L)
                .build();

        PriceAlertsEntity alert = PriceAlertsEntity.builder()
                .user(user)
                .item(item)
                .world("Crystal")
                .isHq(true)
                .build();

        PriceAlertRequestDTO requestDTO = PriceAlertRequestDTO.builder()
                .userId(2L)
                .itemId(23L)
                .world("Crystal")
                .targetPrice(BigDecimal.valueOf(1.0))
                .alertCondition(PriceAlertsEntity.AlertCondition.BELOW)
                .isHq(true)
                .build();

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(23L)).thenReturn(Optional.of(item));
        when(priceAlertRepo.save(any(PriceAlertsEntity.class))).thenReturn(alert);

        PriceAlertResponseDTO response = priceAlertService.createAlert(requestDTO);

        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(2);
        assertThat(response.getItemId()).isEqualTo(23);
        assertThat(response.getWorld()).isEqualTo("Crystal");
        assertThat(response.isHq()).isTrue();

    }
}

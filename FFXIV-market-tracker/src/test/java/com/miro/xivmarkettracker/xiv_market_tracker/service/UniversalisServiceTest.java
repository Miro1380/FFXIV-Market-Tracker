package com.miro.xivmarkettracker.xiv_market_tracker.service;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.PriceSnapshotResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.UniversalisApiItemResponse;
import com.miro.xivmarkettracker.xiv_market_tracker.client.UniversalisApiClient;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.ItemEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.PriceAlertsEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.PriceSnapshotEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.exceptions.ResourceNotFoundException;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.ItemRepository;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.PriceSnapshotRepository;
import jakarta.mail.FetchProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UniversalisServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private PriceSnapshotRepository snapshotRepository;

    @Mock
    private UniversalisApiClient universalisApiClient;

    @InjectMocks
    private UniversalisService universalisService;

    @Test
    @DisplayName("fetch and Save: valid itemId, valid world")
    void fetchAndSaveSnapshot_validId_validWorld(){
        ItemEntity item = ItemEntity.builder()
                .itemId(23L)
                .build();

        UniversalisApiItemResponse apiResponse = UniversalisApiItemResponse.builder()
                .avgPrice(BigDecimal.valueOf(1500))
                .minPrice(BigDecimal.valueOf(1200))
                .maxPrice(BigDecimal.valueOf(1800))
                .listingCount(10)
                .volumeSold(5)
                .avgPriceNq(BigDecimal.valueOf(1400))
                .avgPriceHq(BigDecimal.valueOf(1600))
                .lastUploadTime(1700000000000L)
                .build();

        PriceSnapshotEntity snapshot = PriceSnapshotEntity.builder()
                .item(item)
                .world("Crystal")
                .avgPriceHq(BigDecimal.TWO)
                .maxPrice(BigDecimal.TEN)
                .id(23L)
                .build();

        when(itemRepository.findById(23L)).thenReturn(Optional.of(item));
        when(universalisApiClient.getItem("Crystal", 23L)).thenReturn(apiResponse);
        when(snapshotRepository.save(any(PriceSnapshotEntity.class))).thenReturn(snapshot);

        PriceSnapshotResponseDTO response = universalisService
                .fetchAndSaveSnapshot(23L,"Crystal");

        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("fetch and Save: invalid itemId, valid world")
    void fetchAndSaveSnapshot_invalidId_validWorld(){

        when(itemRepository.findById(23L)).thenReturn(Optional.empty());

        assertThatThrownBy( () -> universalisService.fetchAndSaveSnapshot(23L,"Crystal"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("getHistory: valid id, valid world")
    void getHistory_validId_validWorld(){
        ItemEntity item = ItemEntity.builder()
                .itemId(23L)
                .build();

        String world = "Crystal";

        PriceSnapshotEntity priceSnapshotEntity = PriceSnapshotEntity.builder()
                .id(23L)
                .world(world)
                .maxPrice(BigDecimal.valueOf(1000))
                .avgPriceHq(BigDecimal.valueOf(500))
                .item(item)
                .minPrice(BigDecimal.ONE)
                .build();

        List<PriceSnapshotEntity> entityList = new ArrayList<>();
        entityList.add(priceSnapshotEntity);

        when(itemRepository.findById(23L)).thenReturn(Optional.of(item));
        when(snapshotRepository.findByItemAndWorldOrderByCapturedAtDesc(item,world))
                .thenReturn(entityList);

        List<PriceSnapshotResponseDTO> response  = universalisService.getHistory(23L,world);

        assertThat(response).isNotNull();
        assertThat(response).size().isEqualTo(1);
    }

    @Test
    @DisplayName("get history: invalid id, valid world")
    void getHistory_invalidId_validWorld(){
        ItemEntity item = ItemEntity.builder()
                .itemId(23L)
                .build();

        String world = "Crystal";
        when(itemRepository.findById(23L)).thenReturn(Optional.empty());

        assertThatThrownBy( () -> universalisService.getHistory(23L, world))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

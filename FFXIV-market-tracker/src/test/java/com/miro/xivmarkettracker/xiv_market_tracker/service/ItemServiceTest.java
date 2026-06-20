package com.miro.xivmarkettracker.xiv_market_tracker.service;


import com.miro.xivmarkettracker.xiv_market_tracker.DTO.ItemResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.XivApiItemResponse;
import com.miro.xivmarkettracker.xiv_market_tracker.client.XivApiClient;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.ItemEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private XivApiClient xivApiClient;

    @InjectMocks
    private ItemService itemService;


    @Test
    @DisplayName("seedItem: Item id and save to db")
    void seedItem_itemId_NewItem(){
        //Arrange
        ItemEntity item = ItemEntity.builder()
                .itemId(2L)
                .build();

        XivApiItemResponse xivApiItemResponse = XivApiItemResponse.builder()
                .itemName("Mythril Ore")
                .itemId(2L)
                .build();

        //Act
        when(xivApiClient.getItem(2)).thenReturn(Mono.just(xivApiItemResponse));
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(item);

        ItemResponseDTO response = itemService.seedItem(2);

        //Assert
        assertThat(response).isNotNull();
        assertThat(response.getItemId()).isEqualTo(2L);

    }

    @Test
    @DisplayName("get Item: valid id case")
    void getItem_validId(){
        //Arrange
        ItemResponseDTO itemResponseDTO = ItemResponseDTO.builder()
                .itemId(2L)
                .build();
        ItemEntity item = ItemEntity.builder()
                .itemId(2L)
                .build();

        //Act
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));

        Optional<ItemResponseDTO> response = itemService.getItem(2L);

        //Assert
        assertThat(response).isNotNull();
        assertThat(response).isPresent();
        assertThat(response.get().getItemId()).isEqualTo(2);

    }

    @Test
    @DisplayName("get Items: list of item Response DTO: gets all the items?")
    void getItems_noArgs(){

        //Arrange
        List<ItemEntity> list = new ArrayList<>();

        ItemEntity item1 = ItemEntity.builder()
                .itemId(22L)
                .itemName("Iron ore")
                .build();

        ItemEntity item2 = ItemEntity.builder()
                .itemId(23L)
                .itemName("Mythril ore")
                .build();

        list.add(item1);
        list.add(item2);

        //Act
        //when(itemRepository.findAll()).thenReturn(Collections.emptyList());
        when(itemRepository.findAll()).thenReturn(list);

        List<ItemResponseDTO> listDTO = itemService.getItems();

        //Assert
        assertThat(listDTO).isNotNull();
        assertThat(listDTO).size().isEqualTo(2);
    }
}

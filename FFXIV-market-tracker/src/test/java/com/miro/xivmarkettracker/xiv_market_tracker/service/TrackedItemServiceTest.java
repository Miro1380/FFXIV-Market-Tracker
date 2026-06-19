package com.miro.xivmarkettracker.xiv_market_tracker.service;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.TrackedItemRequestDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.TrackedItemResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.ItemEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.TrackedItemEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.UserEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.exceptions.DuplicateTrackedItemException;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.ItemRepository;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.TrackedItemRepository;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.UserRepository;
import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.midi.Track;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TrackedItemServiceTest {

    //Needed repo for test
    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private TrackedItemRepository trackedItemRepository;

    //Inject class to be tested
    @InjectMocks
    private TrackedItemService trackedItemService;


    @Test
    @DisplayName("add Tracked items: returns non existing item, tracking is true ")
    void addTrackedItem_returnTrackedItemResponseDto(){
        //Arrange
        UserEntity user = UserEntity.builder()
                .id(2L)
                .build();

        ItemEntity item = ItemEntity.builder()
                .itemId(23L)
                .build();

        TrackedItemEntity trackedItem = TrackedItemEntity.builder()
                .user(user)
                .item(item)
                .isTracking(true)
                .build();

        //Act
        when(trackedItemRepository.findByUserIdAndItemItemIdAndWorld(user.getId(),item.getItemId(),user.getHomeWorld())).thenReturn(Optional.empty());
        when(itemRepository.findById(item.getItemId())).thenReturn(Optional.of(item));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(trackedItemRepository.save(any(TrackedItemEntity.class))).thenReturn(trackedItem);

        TrackedItemRequestDTO request = TrackedItemRequestDTO.builder()
                .userId(user.getId())
                .itemId(item.getItemId())
                .build();

        TrackedItemResponseDTO response = trackedItemService.addTrackedItem(request);

        //Assert
        assertThat(response).isNotNull();
        assertThat(response.isTracking()).isTrue();
        assertThat(response.getUserId()).isEqualTo(2);
        assertThat(response.getItemId()).isEqualTo(23L);
    }

    @Test
    @DisplayName("add Tracked items: existing item , not tracking")
    void addTrackedItem_notExisting_notTracking(){
        //Arrange
        UserEntity user = UserEntity.builder()
                .id(2L)
                .build();

        ItemEntity item = ItemEntity.builder()
                .itemId(23L)
                .build();

        TrackedItemEntity trackedItem = TrackedItemEntity.builder()
                .user(user)
                .item(item)
                .isTracking(false)
                .build();

        //Act
        when(trackedItemRepository.findByUserIdAndItemItemIdAndWorld(user.getId(),item.getItemId(),user.getHomeWorld())).thenReturn(Optional.of(trackedItem));
        when(itemRepository.findById(item.getItemId())).thenReturn(Optional.of(item));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(trackedItemRepository.save(any(TrackedItemEntity.class))).thenReturn(trackedItem);

        TrackedItemRequestDTO request = TrackedItemRequestDTO.builder()
                .userId(user.getId())
                .itemId(item.getItemId())
                .build();

        TrackedItemResponseDTO response = trackedItemService.addTrackedItem(request);

        //Assert
        assertThat(response).isNotNull();
        assertThat(response.isTracking()).isTrue();
        assertThat(response.getUserId()).isEqualTo(2);
        assertThat(response.getItemId()).isEqualTo(23L);
    }

    @Test
    @DisplayName("add Tracked Items: existing item, tracking")
    void addTrackedItem_existing_isTracking(){
        //Arrange
        UserEntity user = UserEntity.builder()
                .id(2L)
                .build();

        ItemEntity item = ItemEntity.builder()
                .itemId(23L)
                .build();

        TrackedItemEntity trackedItem = TrackedItemEntity.builder()
                .user(user)
                .item(item)
                .isTracking(true)
                .build();

        TrackedItemRequestDTO request = TrackedItemRequestDTO.builder()
                .userId(user.getId())
                .itemId(item.getItemId())
                .build();
        //Act
        when(trackedItemRepository.findByUserIdAndItemItemIdAndWorld(user.getId(),item.getItemId(),user.getHomeWorld())).thenReturn(Optional.of(trackedItem));
        when(itemRepository.findById(item.getItemId())).thenReturn(Optional.of(item));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        //when(trackedItemRepository.save(any(TrackedItemEntity.class))).thenReturn(trackedItem);


        //Assert
        assertThatThrownBy( () -> trackedItemService.addTrackedItem(request))
                .isInstanceOf(DuplicateTrackedItemException.class);

    }

    @Test
    @DisplayName("get Tracked Items: returns a list of tracked items")
    void getTrackedItems_validUser(){

        //Arrange
        UserEntity user = UserEntity.builder()
                .id(2L)
                .build();

        ItemEntity item = ItemEntity.builder()
                .itemId(21L)
                .build();

        TrackedItemEntity item1 = TrackedItemEntity.builder()
                .isTracking(true)
                .id(22L)
                .item(item)
                .user(user)
                .build();

        TrackedItemEntity item2 = TrackedItemEntity.builder()
                .isTracking(true)
                .id(23L)
                .item(item)
                .user(user)
                .build();

        ArrayList<TrackedItemEntity> list = new ArrayList<>();
        list.add(item1);
        list.add(item2);


        //Act
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(trackedItemRepository.findByUserAndIsTrackingTrue(user)).thenReturn(list);

        List<TrackedItemResponseDTO> response = trackedItemService.getTrackedItems(2L);

        //Assert
        assertThat(response).size().isEqualTo(2);
    }

    @Test
    @DisplayName("update Tracked Item: updates an item with user Id, item Id ")
    void updateTrackedItem_authorized(){

        //Arrange
        UserEntity user = UserEntity.builder()
                .id(2L)
                .build();

        ItemEntity item = ItemEntity.builder()
                .itemId(22L)
                .build();

        TrackedItemEntity trackedItem = TrackedItemEntity.builder()
                .id(23L)
                .user(user)
                .item(item)
                .isTracking(true)
                .build();


        //Act
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(trackedItemRepository.findById(trackedItem.getId())).thenReturn(Optional.of(trackedItem));

        trackedItemService.updateTrackedItem(user.getId(),trackedItem.getId());


        //Assert
        assertThat(trackedItem.getIsTracking()).isFalse();
        verify(trackedItemRepository).save(any(TrackedItemEntity.class));
    }

    @Test
    @DisplayName("delete Tracked item: valid userId, valid  id for item, void")
    void deleteTrackedItem_ValidIds(){
        //Arrange
        UserEntity user = UserEntity.builder()
                .id(2L)
                .build();


        TrackedItemEntity trackedItem = TrackedItemEntity.builder()
                .id(23L)
                .user(user)
                .build();

        //Act
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(trackedItemRepository.findById(trackedItem.getId())).thenReturn(Optional.of(trackedItem));

        trackedItemService.deleteTrackedItem(user.getId(),trackedItem.getId());

        //Assert
        verify(trackedItemRepository).delete(any(TrackedItemEntity.class));
    }
}

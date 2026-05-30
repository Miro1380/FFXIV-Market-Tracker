package com.miro.xivmarkettracker.xiv_market_tracker.service;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.ItemResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.XivApiItemResponse;
import com.miro.xivmarkettracker.xiv_market_tracker.client.XivApiClient;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.ItemEntity;
import org.springframework.stereotype.Service;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.ItemRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final XivApiClient xivApiClient;

    public ItemService(ItemRepository itemRepository, XivApiClient xivApiClient){
        this.itemRepository = itemRepository;
        this.xivApiClient = xivApiClient;
    }

    //Use API client to get item info. Pass in itemId return DTO object (XivItemResponse)
    //Map the response object using ItemEntity builder.
    //Close using build. Save itemEntity to repo (db).
    //Create an ItemResponse DTO to send back to controller and build.
    // Block for simplicity. TODO.
    public ItemResponseDTO seedItem(Integer itemId){

        //Check to see if key is already in db
        Optional<ItemEntity> item = itemRepository.findById(Long.valueOf(itemId));
        if(item.isPresent()){
            return toItemResponseDTO(item.get());
        }

        //Not in db, fetch from xivapi and save
        return xivApiClient.getItem(itemId)
                .map(this::toItemEntity)
                .map(itemRepository::save)
                .map(this::toItemResponseDTO)
                .block();
    }

    public Optional<ItemResponseDTO> getItem(Long itemId){
        return itemRepository.findById(itemId).map(this:: toItemResponseDTO);
    }

    public List<ItemResponseDTO> getItems(){
        return itemRepository.findAll()
                .stream()
                .map(this::toItemResponseDTO)
                .toList();
    }

    public ItemResponseDTO toItemResponseDTO(ItemEntity itemEntity){
        return ItemResponseDTO.builder()
                .itemId(itemEntity.getItemId())
                .itemName(itemEntity.getItemName())
                .iconUrl(itemEntity.getIconUrl())
                .canBeHq(itemEntity.getCanBeHq())
                .stackSize(itemEntity.getStackSize())
                .vendorPrice(itemEntity.getVendorPrice())
                .build();
    }

    private ItemEntity toItemEntity(XivApiItemResponse response) {
        return ItemEntity.builder()
                .itemId(response.getItemId())
                .itemName(response.getItemName())
                .iconUrl(response.getIconUrl())
                .canBeHq(response.getCanBeHQ())
                .stackSize(response.getStackSize())
                .vendorPrice(response.getVendor())
                .build();
    }

}

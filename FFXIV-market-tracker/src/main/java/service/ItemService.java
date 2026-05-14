package service;

import DTO.ItemResponseDTO;
import DTO.XivApiItemResponse;
import client.XivApiClient;
import entity.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import repository.ItemRepository;

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
        return xivApiClient.getItem(itemId)
                .map(this::toItemEntity)
                .map(itemRepository::save)
                .map(this::toItemResponseDTO)
                .block();
    }

    public Optional<ItemResponseDTO> getItem(Long itemId){
        return itemRepository.findById(itemId).map(this:: toItemResponseDTO);
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

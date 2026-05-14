package controller;

import DTO.ItemResponseDTO;
import entity.ItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import service.ItemService;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {


    private final ItemService itemService;

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItem(Long id){
        ItemResponseDTO itemDTO = itemService.getItem(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item Not Found"+id));
        return ResponseEntity.ok(itemDTO);
    }
}

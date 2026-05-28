package com.miro.xivmarkettracker.xiv_market_tracker.controller;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.ItemResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.miro.xivmarkettracker.xiv_market_tracker.service.ItemService;

import java.util.List;

@CrossOrigin(origins = "*")
@Component
@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;


    //Gets Data from xivAPI, stores in db for later use
    @PostMapping("/seed/{id}")
    public ResponseEntity<ItemResponseDTO> seedItem(@PathVariable Integer id){
        ItemResponseDTO itemDTO = itemService.seedItem(id);
        log.info("Sending request to seed with id: {}", id);
        return ResponseEntity.ok(itemDTO);
    }

    //Gets data from repo using service
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItem(@PathVariable Long id){
        log.info("Checking repo for item: {}",id);
        ItemResponseDTO itemResponseDTO = itemService.getItem(id)
                .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not Found: "+ id));
        return ResponseEntity.ok(itemResponseDTO);
    }

    //Gets all data from db. Not for use.
    @GetMapping("/all")
    public ResponseEntity<List<ItemResponseDTO>> getItems(){
        log.info("Getting all items. Not ideal");
        List<ItemResponseDTO> items = itemService.getItems();
        if(items.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }

    //Get data from universalis?
}

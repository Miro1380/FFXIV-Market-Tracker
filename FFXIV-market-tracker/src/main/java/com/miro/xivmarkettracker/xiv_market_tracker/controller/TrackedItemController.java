package com.miro.xivmarkettracker.xiv_market_tracker.controller;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.TrackedItemRequestDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.TrackedItemResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.service.TrackedItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/tracked")
public class TrackedItemController {
    private final TrackedItemService trackedItemService;

    @PostMapping
    public ResponseEntity<TrackedItemResponseDTO> addTrackedItem(@RequestBody TrackedItemRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(trackedItemService.addTrackedItem(dto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TrackedItemResponseDTO>> getTrackedItems(@PathVariable Long userId){
        return ResponseEntity.ok(trackedItemService.getTrackedItems(userId));
    }

    @DeleteMapping("/user/{userId}/item/{itemId}")
    public ResponseEntity<Void> deleteTrackedItem(@PathVariable Long userId, @PathVariable Long itemId){
        log.info("Deleting from user {} and itemId {}", userId,itemId);
        trackedItemService.deleteTrackedItem(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/user/{userId}/item/{itemId}")
    public ResponseEntity<Void> updateTrackedItem(@PathVariable Long userId, @PathVariable Long itemId){
        log.info("Updating user {} with id {}", userId,itemId);
        trackedItemService.updateTrackedItem(userId, itemId);
        return ResponseEntity.noContent().build();
    }
}

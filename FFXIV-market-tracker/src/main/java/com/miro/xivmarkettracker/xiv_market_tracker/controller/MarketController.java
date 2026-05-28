package com.miro.xivmarkettracker.xiv_market_tracker.controller;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.PriceSnapshotResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.service.UniversalisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/market")
public class MarketController {

    private final UniversalisService universalisService;

    //Get data on submitted object Id, name
    @PostMapping ("/snapshot/{itemId}/{world}")
    public ResponseEntity<PriceSnapshotResponseDTO> saveSnapshot(@PathVariable Long itemId, @PathVariable String world){
        return ResponseEntity.ok(universalisService.fetchAndSaveSnapshot(itemId,world));
    }

    @GetMapping ("/{itemId}/{world}/history")
    public ResponseEntity<List<PriceSnapshotResponseDTO>> getHistory(@PathVariable Long itemId, @PathVariable String world){
        return ResponseEntity.ok(universalisService.getHistory(itemId,world));
    }
}

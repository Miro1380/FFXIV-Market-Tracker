package com.miro.xivmarkettracker.xiv_market_tracker.controller;


import com.miro.xivmarkettracker.xiv_market_tracker.DTO.PriceAlertRequestDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.PriceAlertResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.service.PriceAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor

@RequestMapping("/api/alerts")
public class PriceAlertController {
    private final PriceAlertService priceAlertService;

    @PostMapping()
    ResponseEntity<PriceAlertResponseDTO> createAlerts(@RequestBody PriceAlertRequestDTO priceAlertRequestDTO){
        PriceAlertResponseDTO response = priceAlertService.createAlert(priceAlertRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<List<PriceAlertResponseDTO>> getAlertsByUser(@PathVariable Long userId){
        return ResponseEntity.ok(priceAlertService.getAlertsByUser(userId));
    }

    @DeleteMapping("/{alertId}")
    ResponseEntity<Void> deleteAlert(@PathVariable Long alertId){
        priceAlertService.deleteAlert(alertId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check/{itemId}/{world}")
    ResponseEntity<List<PriceAlertResponseDTO>> checkAlerts(@PathVariable Long itemId,@PathVariable String world){
        //TODO Implement checkAlerts in service

        return ResponseEntity.ok(priceAlertService.checkAlerts(itemId,world));
    }
}

package com.miro.xivmarkettracker.xiv_market_tracker.controller;


import com.miro.xivmarkettracker.xiv_market_tracker.service.PriceAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PriceAlertController {
    private final PriceAlertService priceAlertService;
}

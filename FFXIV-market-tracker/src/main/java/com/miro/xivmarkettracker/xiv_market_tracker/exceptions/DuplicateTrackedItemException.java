package com.miro.xivmarkettracker.xiv_market_tracker.exceptions;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.TrackedItemResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.TrackedItemEntity;

public class DuplicateTrackedItemException extends RuntimeException{
    private final TrackedItemResponseDTO dto;

    public DuplicateTrackedItemException(TrackedItemResponseDTO dto){
        super("Already Tracked");
        this.dto = dto;
    }

    public TrackedItemResponseDTO getDTO(){return dto;}
}

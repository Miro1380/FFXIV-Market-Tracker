package com.miro.xivmarkettracker.xiv_market_tracker.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String homeWorld;
}

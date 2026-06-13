package com.miro.xivmarkettracker.xiv_market_tracker.controller;


import com.miro.xivmarkettracker.xiv_market_tracker.DTO.AuthRequestDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.AuthResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request){
        return ResponseEntity.ok(authService.login(request));
    }
}

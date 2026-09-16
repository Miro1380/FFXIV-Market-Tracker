package com.miro.xivmarkettracker.xiv_market_tracker.controller;


import com.miro.xivmarkettracker.xiv_market_tracker.DTO.AuthRequestDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.AuthResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.RegisterRequestDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request){
        return ResponseEntity.ok(authService.login(request));
    }
}

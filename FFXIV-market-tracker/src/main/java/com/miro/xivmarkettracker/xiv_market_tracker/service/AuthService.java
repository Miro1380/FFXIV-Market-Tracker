package com.miro.xivmarkettracker.xiv_market_tracker.service;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.AuthRequestDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.AuthResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.UserEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.exceptions.ResourceNotFoundException;
import com.miro.xivmarkettracker.xiv_market_tracker.exceptions.UnauthorizedException;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public AuthResponseDTO login(AuthRequestDTO request){

        //Look up user with request info
        UserEntity user = userRepository.findByUsername(request.getUsername()).orElseThrow( () -> new ResourceNotFoundException("User not found"));

        //If passwords don't match, reject.
        if(!request.getPassword().equals(user.getPasswordHash())){
            throw new UnauthorizedException("Invalid credentials");
        }

        //Return new Auth response
        return AuthResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .homeWorld(user.getHomeWorld())
                .build();
    }
}

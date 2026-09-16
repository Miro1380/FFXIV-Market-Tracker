package com.miro.xivmarkettracker.xiv_market_tracker.service;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.AuthRequestDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.AuthResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.RegisterRequestDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.UserEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.exceptions.DuplicateUserException;
import com.miro.xivmarkettracker.xiv_market_tracker.exceptions.UnauthorizedException;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request){

        //Usernames are the login handle, so they have to be unique.
        if(userRepository.existsByUsername(request.getUsername())){
            throw new DuplicateUserException("Username already taken");
        }

        //Only ever store the hash. The raw password is never persisted or logged.
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .homeWorld(request.getHomeWorld())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();

        try{
            //saveAndFlush so the unique constraint is enforced here, inside the try,
            //instead of at commit time where this catch could no longer see it.
            return toResponse(userRepository.saveAndFlush(user));
        } catch (DataIntegrityViolationException ex){
            //Lost the race against a concurrent registration of the same username.
            //The constraint caught it; report it as a conflict, not a 500.
            throw new DuplicateUserException("Username already taken");
        }
    }

    public AuthResponseDTO login(AuthRequestDTO request){

        //Unknown user and wrong password both return the same error, otherwise
        //the response tells an attacker which usernames exist.
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow( () -> new UnauthorizedException("Invalid credentials"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())){
            throw new UnauthorizedException("Invalid credentials");
        }

        return toResponse(user);
    }

    private AuthResponseDTO toResponse(UserEntity user){
        return AuthResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .homeWorld(user.getHomeWorld())
                .build();
    }
}

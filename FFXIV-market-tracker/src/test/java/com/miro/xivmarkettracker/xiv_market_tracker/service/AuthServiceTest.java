package com.miro.xivmarkettracker.xiv_market_tracker.service;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.AuthRequestDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.AuthResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.UserEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.exceptions.ResourceNotFoundException;
import com.miro.xivmarkettracker.xiv_market_tracker.exceptions.UnauthorizedException;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.UserRepository;
import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    //Arrange
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("login:valid credentials -> returns AuthResponseDTO with correct fields")
    void login_validCredentials_returnDto(){
        UserEntity user = UserEntity.builder()
                .id(1L)
                .username("demo")
                .email("demo@example.com")
                .homeWorld("Crystal")
                .passwordHash("demo")
                .build();

        AuthRequestDTO request = AuthRequestDTO.builder()
                .username("demo")
                .password("demo")
                .build();

        when(userRepository.findByUsername("demo")).thenReturn(Optional.of(user));

        AuthResponseDTO result = authService.login(request);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("demo");
        assertThat(result.getHomeWorld()).isEqualTo("Crystal");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("login:invalid credentials -> Returns unauthorizedException")
    void login_wrongPassword_throwsUnauthorized(){

        //Arrange
        UserEntity user = UserEntity.builder()
                .username("demo")
                .passwordHash("demo")
                .build();

        AuthRequestDTO request = AuthRequestDTO.builder()
                .username("demo")
                .password("wrongpassword")
                .build();

        //Act
        when(userRepository.findByUsername("demo")).thenReturn(Optional.of(user));

        //Assert
        assertThatThrownBy( () -> authService.login(request)).isInstanceOf(UnauthorizedException.class);

    }

    @Test
    @DisplayName("login: unknown username -> throws Resource Not found exception")
    void login_unknownUser_throwsNotFound(){

        //Arrange
        UserEntity user = UserEntity.builder()
                .username("ghost")
                .build();

        AuthRequestDTO request = AuthRequestDTO.builder()
                .username("ghost")
                .password("anything")
                .build();

        //Act
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        //Assert
        assertThatThrownBy( () -> authService.login(request)).isInstanceOf(ResourceNotFoundException.class);
    }

}

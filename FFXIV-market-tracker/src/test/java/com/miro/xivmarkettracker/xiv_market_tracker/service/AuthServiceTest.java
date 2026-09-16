package com.miro.xivmarkettracker.xiv_market_tracker.service;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.AuthRequestDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.AuthResponseDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.DTO.RegisterRequestDTO;
import com.miro.xivmarkettracker.xiv_market_tracker.entity.UserEntity;
import com.miro.xivmarkettracker.xiv_market_tracker.exceptions.DuplicateUserException;
import com.miro.xivmarkettracker.xiv_market_tracker.exceptions.UnauthorizedException;
import com.miro.xivmarkettracker.xiv_market_tracker.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    //A real BCrypt hash of "demo", used so the stored value in these tests
    //looks like what the database actually holds.
    private static final String DEMO_HASH = "$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5XcHOZ2Tq0rHvZ5sVhZ0Pz9dFGKzq";

    //Arrange
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Captor
    private ArgumentCaptor<UserEntity> userCaptor;

    @Test
    @DisplayName("login:valid credentials -> returns AuthResponseDTO with correct fields")
    void login_validCredentials_returnDto(){
        UserEntity user = UserEntity.builder()
                .id(1L)
                .username("demo")
                .email("demo@example.com")
                .homeWorld("Crystal")
                .passwordHash(DEMO_HASH)
                .build();

        AuthRequestDTO request = AuthRequestDTO.builder()
                .username("demo")
                .password("demo")
                .build();

        when(userRepository.findByUsername("demo")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("demo", DEMO_HASH)).thenReturn(true);

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
                .passwordHash(DEMO_HASH)
                .build();

        AuthRequestDTO request = AuthRequestDTO.builder()
                .username("demo")
                .password("wrongpassword")
                .build();

        //Act
        when(userRepository.findByUsername("demo")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", DEMO_HASH)).thenReturn(false);

        //Assert
        assertThatThrownBy( () -> authService.login(request)).isInstanceOf(UnauthorizedException.class);

    }

    @Test
    @DisplayName("login: unknown username -> throws Unauthorized, not NotFound, so usernames can't be enumerated")
    void login_unknownUser_throwsUnauthorized(){

        //Arrange
        AuthRequestDTO request = AuthRequestDTO.builder()
                .username("ghost")
                .password("anything")
                .build();

        //Act
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        //Assert
        assertThatThrownBy( () -> authService.login(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Invalid credentials");
    }

    @Test
    @DisplayName("register: stores the hash, never the raw password")
    void register_newUser_storesHashedPassword(){

        //Arrange
        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .username("miro")
                .email("miro@example.com")
                .homeWorld("Behemoth")
                .password("plaintextpw")
                .build();

        when(userRepository.existsByUsername("miro")).thenReturn(false);
        when(passwordEncoder.encode("plaintextpw")).thenReturn(DEMO_HASH);
        when(userRepository.saveAndFlush(any(UserEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        //Act
        AuthResponseDTO result = authService.register(request);

        //Assert
        verify(userRepository).saveAndFlush(userCaptor.capture());
        UserEntity saved = userCaptor.getValue();

        assertThat(saved.getPasswordHash()).isEqualTo(DEMO_HASH);
        assertThat(saved.getPasswordHash()).isNotEqualTo("plaintextpw");
        assertThat(result.getUsername()).isEqualTo("miro");
        assertThat(result.getHomeWorld()).isEqualTo("Behemoth");
    }

    @Test
    @DisplayName("register: taken username -> throws DuplicateUserException and saves nothing")
    void register_duplicateUsername_throwsDuplicate(){

        //Arrange
        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .username("demo")
                .email("demo@example.com")
                .password("plaintextpw")
                .build();

        when(userRepository.existsByUsername("demo")).thenReturn(true);

        //Assert
        assertThatThrownBy( () -> authService.register(request))
                .isInstanceOf(DuplicateUserException.class);

        verify(userRepository, never()).saveAndFlush(any(UserEntity.class));
    }

    @Test
    @DisplayName("register: loses race to a concurrent signup -> constraint violation becomes DuplicateUserException")
    void register_concurrentDuplicate_throwsDuplicate(){

        //Arrange - existsByUsername passes, then the DB unique constraint rejects
        //the insert. This is the window a check-then-insert alone can't close.
        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .username("miro")
                .email("miro@example.com")
                .password("plaintextpw")
                .build();

        when(userRepository.existsByUsername("miro")).thenReturn(false);
        when(passwordEncoder.encode("plaintextpw")).thenReturn(DEMO_HASH);
        when(userRepository.saveAndFlush(any(UserEntity.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate key value violates unique constraint"));

        //Assert - a 409, not a leaked 500
        assertThatThrownBy( () -> authService.register(request))
                .isInstanceOf(DuplicateUserException.class)
                .hasMessage("Username already taken");
    }

    @Test
    @DisplayName("BCrypt round trip: a real encoder matches its own hash and rejects others")
    void bcrypt_roundTrip_matches(){

        //Arrange - deliberately NOT a mock. This is what catches a no-op encoder
        //being wired in by mistake.
        PasswordEncoder realEncoder = new BCryptPasswordEncoder();

        //Act
        String hash = realEncoder.encode("plaintextpw");

        //Assert
        assertThat(hash).isNotEqualTo("plaintextpw");
        assertThat(hash).startsWith("$2a$");
        assertThat(realEncoder.matches("plaintextpw", hash)).isTrue();
        assertThat(realEncoder.matches("wrongpassword", hash)).isFalse();
    }
}

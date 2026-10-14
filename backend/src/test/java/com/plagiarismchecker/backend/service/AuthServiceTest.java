package com.plagiarismchecker.backend.service;

import com.plagiarismchecker.backend.dto.AuthResponse;
import com.plagiarismchecker.backend.dto.LoginRequest;
import com.plagiarismchecker.backend.dto.RegisterRequest;
import com.plagiarismchecker.backend.exception.BadRequestException;
import com.plagiarismchecker.backend.exception.UnauthorizedException;
import com.plagiarismchecker.backend.model.Role;
import com.plagiarismchecker.backend.model.User;
import com.plagiarismchecker.backend.repository.UserRepository;
import com.plagiarismchecker.backend.security.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest("john_doe", "john@college.edu", "secret123", "John Doe", Role.STUDENT);
        testUser = new User("john_doe", "john@college.edu", "hashedSecret", "John Doe", Role.STUDENT);
        testUser.setId(1L);
    }

    @Test
    @DisplayName("Registration succeeds when username and email are unique")
    void testRegisterSuccess() {
        when(userRepository.existsByUsername("john_doe")).thenReturn(false);
        when(userRepository.existsByEmail("john@college.edu")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("hashedSecret");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals("john_doe", response.getUsername());
        assertEquals(Role.STUDENT, response.getRole());
        assertNotNull(response.getToken());

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Registration throws BadRequestException when username already taken")
    void testRegisterDuplicateUsername() {
        when(userRepository.existsByUsername("john_doe")).thenReturn(true);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> authService.register(registerRequest));
        assertEquals("Username 'john_doe' is already taken.", ex.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Registration throws BadRequestException when email already registered")
    void testRegisterDuplicateEmail() {
        when(userRepository.existsByUsername("john_doe")).thenReturn(false);
        when(userRepository.existsByEmail("john@college.edu")).thenReturn(true);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> authService.register(registerRequest));
        assertEquals("Email 'john@college.edu' is already registered.", ex.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Login succeeds with valid username and password")
    void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest("john_doe", "secret123");
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("secret123", "hashedSecret")).thenReturn(true);

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals("john_doe", response.getUsername());
        assertNotNull(response.getToken());
    }

    @Test
    @DisplayName("Login throws UnauthorizedException when user not found")
    void testLoginUserNotFound() {
        LoginRequest loginRequest = new LoginRequest("unknown_user", "password");
        when(userRepository.findByUsername("unknown_user")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("unknown_user")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> authService.login(loginRequest));
    }

    @Test
    @DisplayName("Login throws UnauthorizedException when password does not match")
    void testLoginWrongPassword() {
        LoginRequest loginRequest = new LoginRequest("john_doe", "wrongPassword");
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", "hashedSecret")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.login(loginRequest));
    }

    @Test
    @DisplayName("Login throws UnauthorizedException when user is inactive")
    void testLoginInactiveUser() {
        testUser.setActive(false);
        LoginRequest loginRequest = new LoginRequest("john_doe", "secret123");
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(testUser));

        assertThrows(UnauthorizedException.class, () -> authService.login(loginRequest));
    }
}

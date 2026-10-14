package com.plagiarismchecker.backend.service;

import com.plagiarismchecker.backend.dto.PasswordChangeRequest;
import com.plagiarismchecker.backend.dto.UserProfileResponse;
import com.plagiarismchecker.backend.exception.BadRequestException;
import com.plagiarismchecker.backend.exception.ResourceNotFoundException;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("alice_teacher", "alice@college.edu", "hashedSecret", "Alice Smith", Role.TEACHER);
        testUser.setId(10L);
    }

    @Test
    @DisplayName("GetAllUsers maps all entities to UserProfileResponse DTOs")
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        List<UserProfileResponse> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("alice_teacher", result.get(0).getUsername());
        assertEquals(Role.TEACHER, result.get(0).getRole());
    }

    @Test
    @DisplayName("GetUserById returns user profile when found")
    void testGetUserByIdSuccess() {
        when(userRepository.findById(10L)).thenReturn(Optional.of(testUser));

        UserProfileResponse response = userService.getUserById(10L);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("alice_teacher", response.getUsername());
    }

    @Test
    @DisplayName("GetUserById throws ResourceNotFoundException when user does not exist")
    void testGetUserByIdNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    @DisplayName("ChangePassword updates password when current password matches")
    void testChangePasswordSuccess() {
        PasswordChangeRequest request = new PasswordChangeRequest("oldPass", "newPass123");
        when(userRepository.findById(10L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("oldPass", "hashedSecret")).thenReturn(true);
        when(passwordEncoder.encode("newPass123")).thenReturn("newHashedSecret");

        userService.changePassword(10L, request);

        assertEquals("newHashedSecret", testUser.getPassword());
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("ChangePassword throws BadRequestException when old password is wrong")
    void testChangePasswordWrongOldPassword() {
        PasswordChangeRequest request = new PasswordChangeRequest("wrongOldPass", "newPass123");
        when(userRepository.findById(10L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongOldPass", "hashedSecret")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> userService.changePassword(10L, request));
    }

    @Test
    @DisplayName("ToggleUserStatus successfully updates active status")
    void testToggleUserStatus() {
        when(userRepository.findById(10L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(testUser)).thenReturn(testUser);

        UserProfileResponse response = userService.toggleUserStatus(10L, false);

        assertNotNull(response);
        assertFalse(response.isActive());
        verify(userRepository).save(testUser);
    }
}

package com.plagiarismchecker.backend.controller;

import com.plagiarismchecker.backend.dto.PasswordChangeRequest;
import com.plagiarismchecker.backend.dto.UserProfileResponse;
import com.plagiarismchecker.backend.model.Role;
import com.plagiarismchecker.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * REST Controller exposing user management and profile APIs.
 * Base route: /api/users
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Lists all registered user profiles.
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<List<UserProfileResponse>> getAllUsers() {
        List<UserProfileResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves a user profile by ID.
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable Long id) {
        UserProfileResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Retrieves all users belonging to a specific role (e.g., STUDENT or TEACHER).
     * GET /api/users/role/{role}
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserProfileResponse>> getUsersByRole(@PathVariable Role role) {
        List<UserProfileResponse> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    /**
     * Updates an existing user's password.
     * PUT /api/users/{id}/password
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<Map<String, String>> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody PasswordChangeRequest request) {
        userService.changePassword(id, request);
        return ResponseEntity.ok(Map.of("message", "Password updated successfully."));
    }

    /**
     * Activates or deactivates a user account.
     * PATCH /api/users/{id}/status?active=false
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserProfileResponse> toggleUserStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {
        UserProfileResponse updated = userService.toggleUserStatus(id, active);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a user by ID.
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

package com.plagiarismchecker.backend.controller;

import com.plagiarismchecker.backend.dto.AuthResponse;
import com.plagiarismchecker.backend.dto.LoginRequest;
import com.plagiarismchecker.backend.dto.RegisterRequest;
import com.plagiarismchecker.backend.model.Role;
import com.plagiarismchecker.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller exposing user authentication APIs.
 * Base route: /api/auth
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registers a new user account (Student, Teacher, or Admin).
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Authenticates an existing user and returns an active session token.
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Lists all supported application user roles.
     * GET /api/auth/roles
     */
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        List<Role> roles = authService.getAvailableRoles();
        return ResponseEntity.ok(roles);
    }
}

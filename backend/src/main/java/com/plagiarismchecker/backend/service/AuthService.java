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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Service handling user authentication flows (Registration, Login, and Role discovery).
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user after verifying that username and email are unique.
     * Encrypts the raw password before saving.
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username '" + request.getUsername() + "' is already taken.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email '" + request.getEmail() + "' is already registered.");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                hashedPassword,
                request.getFullName(),
                request.getRole()
        );

        User savedUser = userRepository.save(user);

        // Generate an authentication session token
        String token = "auth-token-" + UUID.randomUUID();

        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole(),
                "User registered successfully as " + savedUser.getRole()
        );
    }

    /**
     * Authenticates credentials and returns an active user session response.
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String identifier = request.getUsernameOrEmail().trim();

        // Search by username first; if not found, search by email
        User user = userRepository.findByUsername(identifier)
                .or(() -> userRepository.findByEmail(identifier))
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password."));

        if (!user.isActive()) {
            throw new UnauthorizedException("Your account has been deactivated. Please contact an administrator.");
        }

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!passwordMatches) {
            throw new UnauthorizedException("Invalid username or password.");
        }

        String token = "auth-token-" + UUID.randomUUID();

        return new AuthResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                "Login successful. Welcome back, " + user.getFullName() + "!"
        );
    }

    /**
     * Returns all supported user roles in the system.
     */
    public List<Role> getAvailableRoles() {
        return Arrays.asList(Role.values());
    }
}

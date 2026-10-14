package com.plagiarismchecker.backend.service;

import com.plagiarismchecker.backend.dto.PasswordChangeRequest;
import com.plagiarismchecker.backend.dto.UserProfileResponse;
import com.plagiarismchecker.backend.exception.BadRequestException;
import com.plagiarismchecker.backend.exception.ResourceNotFoundException;
import com.plagiarismchecker.backend.model.Role;
import com.plagiarismchecker.backend.model.User;
import com.plagiarismchecker.backend.repository.UserRepository;
import com.plagiarismchecker.backend.security.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service managing user profiles, role lookups, and account settings.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves all registered users in the system as safe profile DTOs.
     */
    @Transactional(readOnly = true)
    public List<UserProfileResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserProfileResponse::fromUser)
                .toList();
    }

    /**
     * Retrieves a single user profile by primary ID.
     */
    @Transactional(readOnly = true)
    public UserProfileResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return UserProfileResponse.fromUser(user);
    }

    /**
     * Retrieves all users filtered by a given role (e.g. STUDENT or TEACHER).
     */
    @Transactional(readOnly = true)
    public List<UserProfileResponse> getUsersByRole(Role role) {
        return userRepository.findByRole(role)
                .stream()
                .map(UserProfileResponse::fromUser)
                .toList();
    }

    /**
     * Changes a user's password after verifying the old password.
     */
    @Transactional
    public void changePassword(Long userId, PasswordChangeRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Current password does not match.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    /**
     * Activates or deactivates a user account (e.g., administrator action).
     */
    @Transactional
    public UserProfileResponse toggleUserStatus(Long userId, boolean active) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setActive(active);
        User updated = userRepository.save(user);
        return UserProfileResponse.fromUser(updated);
    }

    /**
     * Permanently deletes a user by ID.
     */
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }
}

package com.plagiarismchecker.backend.dto;

import com.plagiarismchecker.backend.model.Role;

/**
 * Data Transfer Object (DTO) returned upon successful registration or login.
 * Contains user identity, granted role, and token information.
 */
public class AuthResponse {

    private String token;
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private Role role;
    private String message;

    public AuthResponse() {
    }

    public AuthResponse(String token, Long userId, String username, String email, String fullName, Role role, String message) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

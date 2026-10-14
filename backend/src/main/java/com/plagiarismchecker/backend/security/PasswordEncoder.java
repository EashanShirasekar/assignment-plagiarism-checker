package com.plagiarismchecker.backend.security;

/**
 * Service interface for encoding passwords and verifying submitted passwords.
 * Follows industry-standard password encoder specifications.
 */
public interface PasswordEncoder {

    /**
     * Hashes the raw password with cryptographic salt.
     */
    String encode(CharSequence rawPassword);

    /**
     * Verifies that the raw password matches the stored encoded password hash.
     */
    boolean matches(CharSequence rawPassword, String encodedPassword);
}

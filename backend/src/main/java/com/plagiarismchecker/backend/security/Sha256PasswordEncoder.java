package com.plagiarismchecker.backend.security;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

/**
 * Password encoder implementation using salted SHA-256 with cryptographically
 * secure pseudo-random number generator (CSPRNG) salt and constant-time verification.
 * 
 * Storage format: <hex-salt>:<hex-hash>
 */
@Component
public class Sha256PasswordEncoder implements PasswordEncoder {

    private static final int SALT_BYTE_LENGTH = 16;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }

        // Generate 16 bytes of random cryptographic salt
        byte[] salt = new byte[SALT_BYTE_LENGTH];
        secureRandom.nextBytes(salt);

        // Compute SHA-256(salt + rawPassword)
        byte[] hash = computeHash(salt, rawPassword.toString());

        // Return formatted as salt:hash in hex
        return HexFormat.of().formatHex(salt) + ":" + HexFormat.of().formatHex(hash);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }

        String[] parts = encodedPassword.split(":");
        if (parts.length != 2) {
            return false;
        }

        try {
            byte[] salt = HexFormat.of().parseHex(parts[0]);
            byte[] expectedHash = HexFormat.of().parseHex(parts[1]);
            byte[] actualHash = computeHash(salt, rawPassword.toString());

            // Constant-time comparison to prevent timing attacks
            return MessageDigest.isEqual(expectedHash, actualHash);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private byte[] computeHash(byte[] salt, String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            return digest.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}

package com.plagiarismchecker.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Sha256PasswordEncoder Unit Tests")
class Sha256PasswordEncoderTest {

    private Sha256PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        encoder = new Sha256PasswordEncoder();
    }

    @Test
    @DisplayName("Encode returns salted hash in salt:hash format")
    void testEncodeFormat() {
        String encoded = encoder.encode("password123");
        assertNotNull(encoded);
        assertTrue(encoded.contains(":"), "Encoded string must contain ':' separator between salt and hash");
    }

    @Test
    @DisplayName("Matches returns true for correct raw password")
    void testMatchesSuccess() {
        String rawPassword = "securePassword!456";
        String encoded = encoder.encode(rawPassword);

        assertTrue(encoder.matches(rawPassword, encoded), "Correct password must match encoded hash");
    }

    @Test
    @DisplayName("Matches returns false for incorrect password")
    void testMatchesFailure() {
        String rawPassword = "securePassword!456";
        String encoded = encoder.encode(rawPassword);

        assertFalse(encoder.matches("wrongPassword", encoded), "Wrong password must not match encoded hash");
    }

    @Test
    @DisplayName("Two encodings of identical password produce distinct salted hashes")
    void testSaltRandomness() {
        String password = "samePassword";
        String hash1 = encoder.encode(password);
        String hash2 = encoder.encode(password);

        assertNotEquals(hash1, hash2, "Independent salt generation must ensure unique hashes for same password");
    }
}

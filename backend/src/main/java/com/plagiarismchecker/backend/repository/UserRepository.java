package com.plagiarismchecker.backend.repository;

import com.plagiarismchecker.backend.model.Role;
import com.plagiarismchecker.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for User entity operations.
 * Spring Data JPA automatically generates the SQL queries at runtime
 * based on the method names (Derived Query Methods).
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their unique username.
     * Generates: SELECT * FROM users WHERE username = ?
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by their unique email.
     * Generates: SELECT * FROM users WHERE email = ?
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user already exists with the given username.
     * Generates: SELECT COUNT(*) > 0 FROM users WHERE username = ?
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user already exists with the given email.
     * Generates: SELECT COUNT(*) > 0 FROM users WHERE email = ?
     */
    boolean existsByEmail(String email);

    /**
     * Finds all users belonging to a specific role (e.g., all STUDENTS or TEACHERS).
     * Generates: SELECT * FROM users WHERE role = ?
     */
    List<User> findByRole(Role role);

    /**
     * Finds all active users in the system.
     * Generates: SELECT * FROM users WHERE active = true
     */
    List<User> findByActiveTrue();
}

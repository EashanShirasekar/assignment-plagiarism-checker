package com.plagiarismchecker.backend.model;

/**
 * Enumeration representing user roles in the plagiarism checker system.
 * 
 * - STUDENT: Can submit assignments and view their own similarity reports.
 * - TEACHER: Can create assignments, view all submissions, and inspect similarity scores across students.
 * - ADMIN: Can manage user accounts and system configuration.
 */
public enum Role {
    STUDENT,
    TEACHER,
    ADMIN
}

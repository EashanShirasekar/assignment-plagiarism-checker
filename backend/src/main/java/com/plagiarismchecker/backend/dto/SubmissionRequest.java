package com.plagiarismchecker.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SubmissionRequest(

        @NotNull(message = "Assignment id is required")
        Long assignmentId,

        @NotBlank(message = "Student name is required")
        @Size(max = 150, message = "Student name must be at most 150 characters")
        String studentName,

        @NotBlank(message = "Student id is required")
        @Size(max = 50, message = "Student id must be at most 50 characters")
        String studentId,

        @NotBlank(message = "Content is required")
        String content) {
}
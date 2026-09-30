package com.plagiarismchecker.backend.dto;

import com.plagiarismchecker.backend.model.Submission;

import java.time.LocalDateTime;

public record SubmissionResponse(
        Long id,
        Long assignmentId,
        String assignmentTitle,
        String studentName,
        String studentId,
        String content,
        LocalDateTime submittedAt) {

    // Converts a database entity into this plain, JSON-safe object
    public static SubmissionResponse from(Submission submission) {
        return new SubmissionResponse(
                submission.getId(),
                submission.getAssignment().getId(),
                submission.getAssignment().getTitle(),
                submission.getStudentName(),
                submission.getStudentId(),
                submission.getContent(),
                submission.getSubmittedAt());
    }
}
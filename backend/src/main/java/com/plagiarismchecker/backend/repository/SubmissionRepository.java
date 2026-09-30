package com.plagiarismchecker.backend.repository;

import com.plagiarismchecker.backend.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    // All submissions for one assignment (needed to compare them against each other)
    List<Submission> findByAssignmentId(Long assignmentId);

    // All submissions made by one student
    List<Submission> findByStudentId(String studentId);

    // Has this student already submitted for this assignment?
    boolean existsByAssignmentIdAndStudentId(Long assignmentId, String studentId);

    // How many submissions does an assignment have?
    long countByAssignmentId(Long assignmentId);
}
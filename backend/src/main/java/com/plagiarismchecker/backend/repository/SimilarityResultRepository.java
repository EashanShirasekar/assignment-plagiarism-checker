package com.plagiarismchecker.backend.repository;

import com.plagiarismchecker.backend.model.SimilarityResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimilarityResultRepository extends JpaRepository<SimilarityResult, Long> {

    // All results for one assignment, highest similarity first
    @Query("SELECT r FROM SimilarityResult r "
            + "WHERE r.submissionA.assignment.id = :assignmentId "
            + "ORDER BY r.similarityScore DESC")
    List<SimilarityResult> findByAssignmentIdOrderByScoreDesc(@Param("assignmentId") Long assignmentId);

    // Results at or above a threshold, e.g. 70.0 means "70% similar or more"
    List<SimilarityResult> findBySimilarityScoreGreaterThanEqualOrderBySimilarityScoreDesc(double threshold);

    // Every result that involves a particular submission, on either side of the comparison
    @Query("SELECT r FROM SimilarityResult r "
            + "WHERE r.submissionA.id = :submissionId OR r.submissionB.id = :submissionId "
            + "ORDER BY r.similarityScore DESC")
    List<SimilarityResult> findBySubmissionId(@Param("submissionId") Long submissionId);
}
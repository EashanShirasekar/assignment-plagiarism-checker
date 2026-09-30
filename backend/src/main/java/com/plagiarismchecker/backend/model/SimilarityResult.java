package com.plagiarismchecker.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "similarity_results")
public class SimilarityResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The two submissions that were compared
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "submission_a_id", nullable = false)
    private Submission submissionA;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "submission_b_id", nullable = false)
    private Submission submissionB;

    // Similarity as a percentage, from 0.0 to 100.0
    @Column(name = "similarity_score", nullable = false)
    private double similarityScore;

    @Column(name = "checked_at", nullable = false, updatable = false)
    private LocalDateTime checkedAt;

    public SimilarityResult() {
    }

    public SimilarityResult(Submission submissionA, Submission submissionB, double similarityScore) {
        this.submissionA = submissionA;
        this.submissionB = submissionB;
        this.similarityScore = similarityScore;
    }

    @PrePersist
    protected void onCreate() {
        this.checkedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Submission getSubmissionA() {
        return submissionA;
    }

    public void setSubmissionA(Submission submissionA) {
        this.submissionA = submissionA;
    }

    public Submission getSubmissionB() {
        return submissionB;
    }

    public void setSubmissionB(Submission submissionB) {
        this.submissionB = submissionB;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(LocalDateTime checkedAt) {
        this.checkedAt = checkedAt;
    }
}
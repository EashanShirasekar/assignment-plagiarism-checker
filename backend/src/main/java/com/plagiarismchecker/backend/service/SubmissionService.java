package com.plagiarismchecker.backend.service;

import com.plagiarismchecker.backend.dto.SubmissionRequest;
import com.plagiarismchecker.backend.dto.SubmissionResponse;
import com.plagiarismchecker.backend.exception.ConflictException;
import com.plagiarismchecker.backend.exception.ResourceNotFoundException;
import com.plagiarismchecker.backend.model.Assignment;
import com.plagiarismchecker.backend.model.Submission;
import com.plagiarismchecker.backend.repository.SimilarityResultRepository;
import com.plagiarismchecker.backend.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final SimilarityResultRepository similarityResultRepository;
    private final AssignmentService assignmentService;

    public SubmissionService(SubmissionRepository submissionRepository,
                             SimilarityResultRepository similarityResultRepository,
                             AssignmentService assignmentService) {
        this.submissionRepository = submissionRepository;
        this.similarityResultRepository = similarityResultRepository;
        this.assignmentService = assignmentService;
    }

    @Transactional
    public SubmissionResponse create(SubmissionRequest request) {
        // Throws "not found" if the assignment does not exist
        Assignment assignment = assignmentService.getById(request.assignmentId());

        String studentId = request.studentId().trim();
        if (submissionRepository.existsByAssignmentIdAndStudentId(assignment.getId(), studentId)) {
            throw new ConflictException("Student " + studentId
                    + " has already submitted for assignment " + assignment.getId());
        }

        Submission submission = new Submission(
                request.studentName().trim(), studentId, request.content(), assignment);
        return SubmissionResponse.from(submissionRepository.save(submission));
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponse> getAll(Long assignmentId) {
        List<Submission> submissions;
        if (assignmentId == null) {
            submissions = submissionRepository.findAll();
        } else {
            assignmentService.getById(assignmentId); // 404 if the assignment is missing
            submissions = submissionRepository.findByAssignmentId(assignmentId);
        }
        return submissions.stream().map(SubmissionResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public SubmissionResponse getById(Long id) {
        return SubmissionResponse.from(findEntity(id));
    }

    // Returns the entity itself. The similarity service in Step 7 will use this.
    @Transactional(readOnly = true)
    public Submission findEntity(Long id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found with id " + id));
    }

    @Transactional
    public void delete(Long id) {
        Submission submission = findEntity(id);
        // Remove comparison results that involve this submission first (foreign keys)
        similarityResultRepository.deleteAll(similarityResultRepository.findBySubmissionId(id));
        submissionRepository.delete(submission);
    }
}
package com.plagiarismchecker.backend.service;

import com.plagiarismchecker.backend.dto.AssignmentRequest;
import com.plagiarismchecker.backend.exception.ConflictException;
import com.plagiarismchecker.backend.exception.ResourceNotFoundException;
import com.plagiarismchecker.backend.model.Assignment;
import com.plagiarismchecker.backend.repository.AssignmentRepository;
import com.plagiarismchecker.backend.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             SubmissionRepository submissionRepository) {
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
    }

    @Transactional
    public Assignment create(AssignmentRequest request) {
        Assignment assignment = new Assignment(request.title(), request.description());
        return assignmentRepository.save(assignment);
    }

    @Transactional(readOnly = true)
    public List<Assignment> getAll(String search) {
        if (search == null || search.isBlank()) {
            return assignmentRepository.findAllByOrderByCreatedAtDesc();
        }
        return assignmentRepository.findByTitleContainingIgnoreCase(search.trim());
    }

    @Transactional(readOnly = true)
    public Assignment getById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id " + id));
    }

    @Transactional
    public Assignment update(Long id, AssignmentRequest request) {
        Assignment assignment = getById(id);
        assignment.setTitle(request.title());
        assignment.setDescription(request.description());
        return assignmentRepository.save(assignment);
    }

    @Transactional
    public void delete(Long id) {
        Assignment assignment = getById(id);
        long count = submissionRepository.countByAssignmentId(id);
        if (count > 0) {
            throw new ConflictException("Cannot delete assignment " + id
                    + " because it has " + count + " submission(s)");
        }
        assignmentRepository.delete(assignment);
    }
}
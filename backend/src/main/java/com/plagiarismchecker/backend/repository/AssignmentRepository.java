package com.plagiarismchecker.backend.repository;

import com.plagiarismchecker.backend.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    // Search assignments whose title contains the given text, ignoring upper/lower case
    List<Assignment> findByTitleContainingIgnoreCase(String title);

    // Newest assignments first
    List<Assignment> findAllByOrderByCreatedAtDesc();
}
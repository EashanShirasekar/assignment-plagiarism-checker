package com.plagiarismchecker.backend.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlagiarismDetectionServiceTest {

    private final PlagiarismDetectionService service =
            new PlagiarismDetectionService();

    @Test
    void identicalDocumentsShouldHaveVeryHighSimilarity() {

        String documentA =
                "Java is a powerful programming language";

        String documentB =
                "Java is a powerful programming language";

        double similarity =
                service.calculateSimilarity(documentA, documentB);

        assertEquals(1.0, similarity, 0.0001);
    }

    @Test
    void completelyDifferentDocumentsShouldHaveLowSimilarity() {

        String documentA =
                "Java programming language";

        String documentB =
                "football stadium players";

        double similarity =
                service.calculateSimilarity(documentA, documentB);

        assertEquals(0.0, similarity, 0.0001);
    }

    @Test
    void similarDocumentsShouldHavePositiveSimilarity() {

        String documentA =
                "Java is used for software development";

        String documentB =
                "Java is widely used for application development";

        double similarity =
                service.calculateSimilarity(documentA, documentB);

        assertTrue(similarity > 0.0);
    }

    @Test
    void nullDocumentShouldReturnZero() {

        String documentA = null;

        String documentB =
                "Java programming language";

        double similarity =
                service.calculateSimilarity(documentA, documentB);

        assertEquals(0.0, similarity);
    }

    @Test
    void similarityPercentageShouldReturnValueBetweenZeroAndHundred() {

        String documentA =
                "Java programming language";

        String documentB =
                "Java programming language";

        double percentage =
                service.calculateSimilarityPercentage(
                        documentA,
                        documentB);

        assertTrue(percentage >= 0.0);
        assertTrue(percentage <= 100.0);
    }
}
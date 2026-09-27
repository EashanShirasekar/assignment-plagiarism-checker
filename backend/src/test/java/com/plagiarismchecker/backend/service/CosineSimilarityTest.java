package com.plagiarismchecker.backend.service;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CosineSimilarityTest {

    private final CosineSimilarity cosineSimilarity =
            new CosineSimilarity();

    @Test
    void shouldReturnOneForIdenticalVectors() {

        Map<String, Double> vectorA = Map.of(
                "java", 0.8,
                "programming", 0.6
        );

        Map<String, Double> vectorB = Map.of(
                "java", 0.8,
                "programming", 0.6
        );

        double result =
                cosineSimilarity.calculate(vectorA, vectorB);

        assertEquals(1.0, result, 0.000001);
    }

    @Test
    void shouldReturnZeroForEmptyVectors() {

        Map<String, Double> vectorA = Map.of();
        Map<String, Double> vectorB = Map.of();

        double result =
                cosineSimilarity.calculate(vectorA, vectorB);

        assertEquals(0.0, result);
    }

    @Test
    void shouldReturnValueBetweenZeroAndOneForDifferentVectors() {

        Map<String, Double> vectorA = Map.of(
                "java", 0.8,
                "programming", 0.6
        );

        Map<String, Double> vectorB = Map.of(
                "java", 0.2,
                "python", 0.9
        );

        double result =
                cosineSimilarity.calculate(vectorA, vectorB);

        assertTrue(result >= 0.0 && result <= 1.0);
    }
}
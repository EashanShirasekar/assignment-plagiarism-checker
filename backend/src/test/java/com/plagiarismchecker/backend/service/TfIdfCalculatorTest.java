package com.plagiarismchecker.backend.service;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TfIdfCalculatorTest {

    private final TfIdfCalculator calculator = new TfIdfCalculator();

    @Test
    void shouldCalculateTfIdfForDocuments() {

        List<List<String>> documents = List.of(
                List.of("java", "programming", "java"),
                List.of("java", "database")
        );

        List<Map<String, Double>> result =
                calculator.calculateTfIdf(documents);

        assertEquals(2, result.size());

        assertTrue(result.get(0).containsKey("java"));
        assertTrue(result.get(0).containsKey("programming"));

        assertTrue(result.get(1).containsKey("java"));
        assertTrue(result.get(1).containsKey("database"));
    }

    @Test
    void shouldReturnEmptyListForNoDocuments() {

        List<Map<String, Double>> result =
                calculator.calculateTfIdf(List.of());

        assertEquals(List.of(), result);
    }

    @Test
    void shouldGiveNonZeroWeightsForIdenticalDocuments() {

        List<List<String>> documents = List.of(
            List.of("java", "programming", "java"),
            List.of("java", "programming", "java")
    );

        List<Map<String, Double>> result =
            calculator.calculateTfIdf(documents);

        assertTrue(result.get(0).get("java") > 0);
        assertTrue(result.get(0).get("programming") > 0);

        assertTrue(result.get(1).get("java") > 0);
        assertTrue(result.get(1).get("programming") > 0);
    }
}
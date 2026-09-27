package com.plagiarismchecker.backend.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CosineSimilarity {

    public double calculate(
            Map<String, Double> vectorA,
            Map<String, Double> vectorB) {

        if (vectorA == null || vectorB == null) {
            return 0.0;
        }

        if (vectorA.isEmpty() || vectorB.isEmpty()) {
            return 0.0;
        }

        Set<String> allWords = new HashSet<>();
        allWords.addAll(vectorA.keySet());
        allWords.addAll(vectorB.keySet());

        double dotProduct = 0.0;
        double magnitudeA = 0.0;
        double magnitudeB = 0.0;

        for (String word : allWords) {

            double valueA = vectorA.getOrDefault(word, 0.0);
            double valueB = vectorB.getOrDefault(word, 0.0);

            dotProduct += valueA * valueB;

            magnitudeA += valueA * valueA;
            magnitudeB += valueB * valueB;
        }

        double denominator =
                Math.sqrt(magnitudeA) * Math.sqrt(magnitudeB);

        if (denominator == 0.0) {
            return 0.0;
        }

        return dotProduct / denominator;
    }
}
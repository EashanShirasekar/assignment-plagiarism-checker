package com.plagiarismchecker.backend.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PlagiarismDetectionService {

    private final TextPreprocessor textPreprocessor;
    private final TfIdfCalculator tfIdfCalculator;
    private final CosineSimilarity cosineSimilarity;

    public PlagiarismDetectionService() {
        this.textPreprocessor = new TextPreprocessor();
        this.tfIdfCalculator = new TfIdfCalculator();
        this.cosineSimilarity = new CosineSimilarity();
    }

    public double calculateSimilarity(String documentA, String documentB) {

        if (documentA == null || documentB == null) {
            return 0.0;
        }

        List<String> tokensA = textPreprocessor.preprocess(documentA);
        List<String> tokensB = textPreprocessor.preprocess(documentB);

        if (tokensA.isEmpty() || tokensB.isEmpty()) {
            return 0.0;
        }

        List<List<String>> documents = List.of(tokensA, tokensB);

        List<Map<String, Double>> tfIdfVectors =
                tfIdfCalculator.calculateTfIdf(documents);

        Map<String, Double> vectorA = tfIdfVectors.get(0);
        Map<String, Double> vectorB = tfIdfVectors.get(1);

        return cosineSimilarity.calculate(vectorA, vectorB);
    }

    public double calculateSimilarityPercentage(
            String documentA,
            String documentB) {

        double similarity = calculateSimilarity(documentA, documentB);

        return similarity * 100;
    }
}
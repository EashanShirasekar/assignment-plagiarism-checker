package com.plagiarismchecker.backend.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TfIdfCalculator {

    public List<Map<String, Double>> calculateTfIdf(
            List<List<String>> documents) {

        if (documents == null || documents.isEmpty()) {
            return List.of();
        }

        Map<String, Integer> documentFrequency = calculateDocumentFrequency(documents);

        int totalDocuments = documents.size();

        return documents.stream()
                .map(document -> calculateDocumentVector(
                        document,
                        documentFrequency,
                        totalDocuments))
                .toList();
    }

    private Map<String, Integer> calculateDocumentFrequency(
            List<List<String>> documents) {

        Map<String, Integer> documentFrequency = new HashMap<>();

        for (List<String> document : documents) {

            Set<String> uniqueWords = new HashSet<>(document);

            for (String word : uniqueWords) {
                documentFrequency.put(
                        word,
                        documentFrequency.getOrDefault(word, 0) + 1
                );
            }
        }

        return documentFrequency;
    }

    private Map<String, Double> calculateDocumentVector(
            List<String> document,
            Map<String, Integer> documentFrequency,
            int totalDocuments) {

        Map<String, Integer> termFrequency = new HashMap<>();

        for (String word : document) {
            termFrequency.put(
                    word,
                    termFrequency.getOrDefault(word, 0) + 1
            );
        }

        Map<String, Double> tfIdfVector = new HashMap<>();

        for (Map.Entry<String, Integer> entry : termFrequency.entrySet()) {

            String word = entry.getKey();

            int wordCount = entry.getValue();

            double tf = (double) wordCount / document.size();

            int documentCount = documentFrequency.get(word);

            double idf = Math.log(
                ((double) totalDocuments + 1) / (documentCount + 1)
            ) + 1;

            double tfIdf = tf * idf;

            tfIdfVector.put(word, tfIdf);
        }

        return tfIdfVector;
    }
}
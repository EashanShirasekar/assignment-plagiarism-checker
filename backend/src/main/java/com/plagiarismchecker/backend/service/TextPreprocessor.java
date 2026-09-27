package com.plagiarismchecker.backend.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TextPreprocessor {

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "an", "the", "and", "or", "but",
            "is", "are", "was", "were", "be", "been",
            "in", "on", "at", "to", "for", "of",
            "with", "by", "from", "as", "this", "that",
            "it", "its", "into", "about", "than",
            "then", "so", "if", "not", "no"
    ));

    public List<String> preprocess(String text) {

        if (text == null || text.isBlank()) {
            return List.of();
        }

        String cleanedText = text.toLowerCase();

        cleanedText = cleanedText.replaceAll("[^a-z0-9\\s]", " ");

        cleanedText = cleanedText.replaceAll("\\s+", " ").trim();

        if (cleanedText.isEmpty()) {
            return List.of();
        }

        List<String> words = Arrays.asList(cleanedText.split(" "));

        return words.stream()
                .filter(word -> !STOP_WORDS.contains(word))
                .collect(Collectors.toList());
    }
}
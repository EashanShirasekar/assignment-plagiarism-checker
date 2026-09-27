package com.plagiarismchecker.backend.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextPreprocessorTest {

    private final TextPreprocessor preprocessor = new TextPreprocessor();

    @Test
    void shouldPreprocessTextCorrectly() {

        String input = "The Computer is VERY powerful!";

        List<String> result = preprocessor.preprocess(input);

        List<String> expected = List.of(
                "computer",
                "very",
                "powerful"
        );

        assertEquals(expected, result);
    }

    @Test
    void shouldReturnEmptyListForBlankText() {

        List<String> result = preprocessor.preprocess("   ");

        assertEquals(List.of(), result);
    }
}
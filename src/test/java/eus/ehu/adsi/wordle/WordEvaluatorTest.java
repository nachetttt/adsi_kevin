package eus.ehu.adsi.wordle;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordEvaluatorTest {

    private final WordEvaluator evaluator = new WordEvaluator();

    @Test
    void detectsCorrectPresentAndAbsentLetters() {
        assertEquals(
                List.of(
                        LetterResult.PRESENT,
                        LetterResult.ABSENT,
                        LetterResult.ABSENT,
                        LetterResult.ABSENT,
                        LetterResult.CORRECT),
                evaluator.evaluate("PERRO", "RADIO"));
    }

    @Test
    void doesNotReuseARepeatedLetterFromTargetWord() {
        assertEquals(
                List.of(
                        LetterResult.CORRECT,
                        LetterResult.CORRECT,
                        LetterResult.ABSENT,
                        LetterResult.ABSENT,
                        LetterResult.PRESENT),
                evaluator.evaluate("CASAS", "CABRA"));
    }
}

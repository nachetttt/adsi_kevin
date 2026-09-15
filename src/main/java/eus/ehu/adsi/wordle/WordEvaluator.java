package eus.ehu.adsi.wordle;

import java.util.Arrays;
import java.util.List;

public class WordEvaluator {

    public List<LetterResult> evaluate(String targetWord, String attemptedWord) {
        if (targetWord.length() != attemptedWord.length()) {
            throw new IllegalArgumentException("Las palabras deben tener la misma longitud");
        }

        int length = targetWord.length();
        LetterResult[] results = new LetterResult[length];
        boolean[] targetUsed = new boolean[length];

        // Primero se marcan las coincidencias exactas.
        for (int i = 0; i < length; i++) {
            if (attemptedWord.charAt(i) == targetWord.charAt(i)) {
                results[i] = LetterResult.CORRECT;
                targetUsed[i] = true;
            }
        }

        // Después se buscan letras presentes en posiciones distintas.
        // targetUsed evita contar dos veces una misma letra de la palabra objetivo.
        for (int i = 0; i < length; i++) {
            if (results[i] != null) {
                continue;
            }

            results[i] = LetterResult.ABSENT;

            for (int j = 0; j < length; j++) {
                if (!targetUsed[j] && attemptedWord.charAt(i) == targetWord.charAt(j)) {
                    results[i] = LetterResult.PRESENT;
                    targetUsed[j] = true;
                    break;
                }
            }
        }

        return List.copyOf(Arrays.asList(results));
    }
}

package eus.ehu.adsi.wordle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Dictionary {

    private final List<String> words;
    private final Random random;

    public Dictionary() {
        this(loadWordsFromResource(), new Random());
    }

    Dictionary(Collection<String> words, Random random) {
        this.words = words.stream()
                .map(Dictionary::normalize)
                .filter(Dictionary::isValidWord)
                .distinct()
                .toList();
        this.random = random;

        if (this.words.isEmpty()) {
            throw new IllegalArgumentException("El diccionario no contiene palabras válidas");
        }
    }

    public boolean contains(String word) {
        return words.contains(normalize(word));
    }

    public String randomWord() {
        return words.get(random.nextInt(words.size()));
    }

    private static List<String> loadWordsFromResource() {
        InputStream input = Dictionary.class.getResourceAsStream("/words.txt");
        if (input == null) {
            throw new IllegalStateException("No se ha encontrado el fichero words.txt");
        }

        List<String> loadedWords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(input, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                loadedWords.add(line);
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se ha podido leer el diccionario", e);
        }

        return loadedWords;
    }

    private static boolean isValidWord(String word) {
        return word.matches("[A-ZÑ]{" + Game.WORD_LENGTH + "}");
    }

    private static String normalize(String word) {
        if (word == null) {
            return "";
        }
        return word.trim().toUpperCase(Locale.ROOT);
    }
}

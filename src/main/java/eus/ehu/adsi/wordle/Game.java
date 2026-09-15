package eus.ehu.adsi.wordle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Game {

    public static final int WORD_LENGTH = 5;
    public static final int MAX_ATTEMPTS = 6;

    private final Dictionary dictionary;
    private final WordEvaluator evaluator;
    private final List<Attempt> attempts;

    private String targetWord;
    private boolean finished;
    private boolean won;

    public Game(Dictionary dictionary) {
        this(dictionary, new WordEvaluator());
    }

    public Game(Dictionary dictionary, WordEvaluator evaluator) {
        this.dictionary = dictionary;
        this.evaluator = evaluator;
        this.attempts = new ArrayList<>();
        startNewGame();
    }

    public Attempt tryWord(String word) {
        if (finished) {
            throw new IllegalStateException("La partida ya ha terminado");
        }

        String normalizedWord = normalize(word);

        if (normalizedWord.length() != WORD_LENGTH) {
            throw new IllegalArgumentException(
                    "La palabra debe tener " + WORD_LENGTH + " letras");
        }

        if (!dictionary.contains(normalizedWord)) {
            throw new IllegalArgumentException("La palabra no está en el diccionario");
        }

        Attempt attempt = new Attempt(
                normalizedWord,
                evaluator.evaluate(targetWord, normalizedWord));
        attempts.add(attempt);

        if (normalizedWord.equals(targetWord)) {
            won = true;
            finished = true;
        } else if (attempts.size() == MAX_ATTEMPTS) {
            finished = true;
        }

        return attempt;
    }

    public void startNewGame() {
        targetWord = dictionary.randomWord();
        attempts.clear();
        finished = false;
        won = false;
    }

    public List<Attempt> getAttempts() {
        return List.copyOf(attempts);
    }

    public int getRemainingAttempts() {
        return MAX_ATTEMPTS - attempts.size();
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean hasWon() {
        return won;
    }

    public String getTargetWord() {
        if (!finished) {
            throw new IllegalStateException(
                    "La palabra objetivo solo puede consultarse al terminar la partida");
        }
        return targetWord;
    }

    private String normalize(String word) {
        if (word == null) {
            return "";
        }
        return word.trim().toUpperCase(Locale.ROOT);
    }
}

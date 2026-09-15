package eus.ehu.adsi.wordle;

import java.util.List;

public class Attempt {
    private final String word;
    private final List<LetterResult> results;

    public Attempt(String word, List<LetterResult> results) {
        this.word = word;
        this.results = List.copyOf(results);
    }

    public String getWord() {
        return word;
    }

    public List<LetterResult> getResults() {
        return results;
    }
}

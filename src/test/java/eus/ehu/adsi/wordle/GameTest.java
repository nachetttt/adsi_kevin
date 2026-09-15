package eus.ehu.adsi.wordle;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game gameWithTarget(String target, String... otherWords) {
        List<String> words = new java.util.ArrayList<>();
        words.add(target);
        words.addAll(List.of(otherWords));

        Dictionary dictionary = new Dictionary(words, new Random() {
            @Override
            public int nextInt(int bound) {
                return 0;
            }
        });

        return new Game(dictionary);
    }

    @Test
    void aNewGameStartsWithoutAttempts() {
        Game game = gameWithTarget("PERRO");

        assertFalse(game.isFinished());
        assertFalse(game.hasWon());
        assertTrue(game.getAttempts().isEmpty());
        assertEquals(Game.MAX_ATTEMPTS, game.getRemainingAttempts());
    }

    @Test
    void rejectsWordsWithWrongLength() {
        Game game = gameWithTarget("PERRO");

        assertThrows(IllegalArgumentException.class,
                () -> game.tryWord("CASA"));
    }

    @Test
    void rejectsWordsOutsideDictionary() {
        Game game = gameWithTarget("PERRO");

        assertThrows(IllegalArgumentException.class,
                () -> game.tryWord("ZZZZZ"));
    }

    @Test
    void winningFinishesTheGame() {
        Game game = gameWithTarget("PERRO");

        game.tryWord("PERRO");

        assertTrue(game.isFinished());
        assertTrue(game.hasWon());
        assertEquals("PERRO", game.getTargetWord());
    }

    @Test
    void targetWordCannotBeReadBeforeGameEnds() {
        Game game = gameWithTarget("PERRO");

        assertThrows(IllegalStateException.class, game::getTargetWord);
    }

    @Test
    void gameEndsAfterMaximumNumberOfFailedAttempts() {
        Game game = gameWithTarget("PERRO", "CASAS");

        for (int i = 0; i < Game.MAX_ATTEMPTS; i++) {
            game.tryWord("CASAS");
        }

        assertTrue(game.isFinished());
        assertFalse(game.hasWon());
        assertEquals(0, game.getRemainingAttempts());
    }

    @Test
    void startingNewGameClearsPreviousState() {
        Game game = gameWithTarget("PERRO");
        game.tryWord("PERRO");

        game.startNewGame();

        assertFalse(game.isFinished());
        assertFalse(game.hasWon());
        assertTrue(game.getAttempts().isEmpty());
    }
}

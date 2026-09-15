package eus.ehu.adsi.wordle;

import javax.swing.SwingUtilities;

public class WordleApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dictionary dictionary = new Dictionary();
            Game game = new Game(dictionary);
            WordleFrame frame = new WordleFrame(game);
            frame.setVisible(true);
        });
    }
}

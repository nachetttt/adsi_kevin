package eus.ehu.adsi.wordle;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.util.Locale;

public class WordleFrame extends JFrame {

    private final Game game;

    private final JLabel[][] cells;
    private JTextField input;
    private JButton tryButton;
    private JButton newGameButton;
    private JLabel message;

    public WordleFrame(Game game) {
        this.game = game;
        this.cells = new JLabel[Game.MAX_ATTEMPTS][Game.WORD_LENGTH];

        setTitle("Wordle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        content.add(createHeader(), BorderLayout.NORTH);
        content.add(createBoard(), BorderLayout.CENTER);
        content.add(createControls(), BorderLayout.SOUTH);

        setContentPane(content);
        pack();
        setLocationRelativeTo(null);
    }

    private JComponent createHeader() {
        JLabel title = new JLabel("WORDLE", SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        return title;
    }

    private JPanel createBoard() {
        JPanel board = new JPanel(
                new GridLayout(Game.MAX_ATTEMPTS, Game.WORD_LENGTH, 5, 5));

        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 24);

        for (int row = 0; row < Game.MAX_ATTEMPTS; row++) {
            for (int column = 0; column < Game.WORD_LENGTH; column++) {
                JLabel cell = new JLabel("", SwingConstants.CENTER);
                cell.setPreferredSize(new Dimension(55, 55));
                cell.setOpaque(true);
                cell.setBackground(Color.WHITE);
                cell.setBorder(new LineBorder(Color.GRAY, 1));
                cell.setFont(font);

                cells[row][column] = cell;
                board.add(cell);
            }
        }

        return board;
    }

    private JPanel createControls() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel inputPanel = new JPanel(new FlowLayout());

        input = new JTextField(Game.WORD_LENGTH + 1);
        ((AbstractDocument) input.getDocument()).setDocumentFilter(new WordFilter());

        tryButton = new JButton("Probar");
        newGameButton = new JButton("Nueva partida");
        newGameButton.setEnabled(false);

        inputPanel.add(input);
        inputPanel.add(tryButton);
        inputPanel.add(newGameButton);

        message = new JLabel(initialMessage());
        message.setAlignmentX(Component.CENTER_ALIGNMENT);

        tryButton.addActionListener(e -> submitWord());
        input.addActionListener(e -> submitWord());
        newGameButton.addActionListener(e -> resetGame());

        panel.add(inputPanel);
        panel.add(message);

        return panel;
    }

    private void submitWord() {
        try {
            Attempt attempt = game.tryWord(input.getText());
            int row = game.getAttempts().size() - 1;
            paintAttempt(row, attempt);

            input.setText("");

            if (game.isFinished()) {
                finishGame();
            } else {
                message.setText("Te quedan " + game.getRemainingAttempts() + " intentos.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            message.setText(e.getMessage());
        }

        input.requestFocusInWindow();
    }

    private void paintAttempt(int row, Attempt attempt) {
        for (int column = 0; column < Game.WORD_LENGTH; column++) {
            JLabel cell = cells[row][column];
            cell.setText(String.valueOf(attempt.getWord().charAt(column)));

            LetterResult result = attempt.getResults().get(column);

            if (result == LetterResult.CORRECT) {
                cell.setBackground(new Color(106, 170, 100));
                cell.setForeground(Color.WHITE);
            } else if (result == LetterResult.PRESENT) {
                cell.setBackground(new Color(201, 180, 88));
                cell.setForeground(Color.WHITE);
            } else {
                cell.setBackground(new Color(120, 124, 126));
                cell.setForeground(Color.WHITE);
            }
        }
    }

    private void finishGame() {
        input.setEnabled(false);
        tryButton.setEnabled(false);
        newGameButton.setEnabled(true);

        if (game.hasWon()) {
            message.setText("¡Has acertado en " + game.getAttempts().size() + " intentos!");
        } else {
            message.setText("La palabra era: " + game.getTargetWord());
        }
    }

    private void resetGame() {
        game.startNewGame();

        for (int row = 0; row < Game.MAX_ATTEMPTS; row++) {
            for (int column = 0; column < Game.WORD_LENGTH; column++) {
                JLabel cell = cells[row][column];
                cell.setText("");
                cell.setBackground(Color.WHITE);
                cell.setForeground(Color.BLACK);
            }
        }

        input.setEnabled(true);
        tryButton.setEnabled(true);
        newGameButton.setEnabled(false);
        input.setText("");
        input.requestFocusInWindow();
        message.setText(initialMessage());
    }

    private String initialMessage() {
        return "Introduce una palabra de " + Game.WORD_LENGTH + " letras.";
    }

    private static class WordFilter extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            replace(fb, offset, 0, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            String normalized = text == null ? "" : text.toUpperCase(Locale.ROOT);
            String current = fb.getDocument().getText(0, fb.getDocument().getLength());
            int newLength = current.length() - length + normalized.length();

            if (newLength <= Game.WORD_LENGTH && normalized.matches("[A-ZÑ]*")) {
                fb.replace(offset, length, normalized, attrs);
            }
        }
    }
}

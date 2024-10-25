import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CandyCrushGame extends JFrame {
    private static final int BOARD_SIZE = 8;
    private static final int CELL_SIZE = 60;

    private JButton[][] buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
    private Point selectedCandy = null;
    private int score = 0; // Score variable
    private JLabel scoreLabel; // Label to display the score
    private JButton replayButton; // Button to replay the game
    private Color[] candyColors = { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA }; // Define colors
                                                                                                       // for candies

    public CandyCrushGame() {
        setTitle("Candy Crush Game");
        setSize(BOARD_SIZE * CELL_SIZE, BOARD_SIZE * CELL_SIZE + 100); // Adjust height to fit the replay button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create score label
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Create replay button
        replayButton = new JButton("Replay");
        replayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame(); // Reset the game when pressed
            }
        });

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(scoreLabel, BorderLayout.CENTER);
        northPanel.add(replayButton, BorderLayout.SOUTH); // Add replay button to the north panel

        add(northPanel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        initializeBoard(boardPanel);
        add(boardPanel, BorderLayout.CENTER);
    }

    private void initializeBoard(JPanel boardPanel) {
        Random random = new Random();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                JButton button = new JButton();
                button.setBackground(candyColors[random.nextInt(candyColors.length)]); // Set a random candy color
                button.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                button.addActionListener(new ButtonClickListener(i, j));
                buttons[i][j] = button;
                boardPanel.add(button);
            }
        }
    }

    private class ButtonClickListener implements ActionListener {
        private int row;
        private int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public void actionPerformed(ActionEvent e) {
            if (selectedCandy == null) {
                selectedCandy = new Point(row, col);
                buttons[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            } else {
                if (isAdjacent(selectedCandy, new Point(row, col))) {
                    swapCandies(selectedCandy, new Point(row, col));
                    if (!checkMatches()) {
                        swapCandies(selectedCandy, new Point(row, col)); // Swap back if no match
                    } else {
                        // Check if score reached 1050
                        if (score >= 1050) {
                            showLevelCompletedDialog();
                        }
                    }
                }
                buttons[selectedCandy.x][selectedCandy.y].setBorder(null);
                selectedCandy = null;
            }
        }
    }

    private boolean isAdjacent(Point p1, Point p2) {
        return (Math.abs(p1.x - p2.x) == 1 && p1.y == p2.y) ||
                (Math.abs(p1.y - p2.y) == 1 && p1.x == p2.x);
    }

    private void swapCandies(Point p1, Point p2) {
        Color tempColor = buttons[p1.x][p1.y].getBackground();
        buttons[p1.x][p1.y].setBackground(buttons[p2.x][p2.y].getBackground());
        buttons[p2.x][p2.y].setBackground(tempColor);
    }

    private boolean checkMatches() {
        List<Point> matchedPoints = new ArrayList<>();

        // Check for horizontal matches
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE - 2; j++) {
                Color color = buttons[i][j].getBackground();
                if (color != null && color.equals(buttons[i][j + 1].getBackground())
                        && color.equals(buttons[i][j + 2].getBackground())) {
                    matchedPoints.add(new Point(i, j));
                    matchedPoints.add(new Point(i, j + 1));
                    matchedPoints.add(new Point(i, j + 2));
                }
            }
        }

        // Check for vertical matches
        for (int i = 0; i < BOARD_SIZE - 2; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Color color = buttons[i][j].getBackground();
                if (color != null && color.equals(buttons[i + 1][j].getBackground())
                        && color.equals(buttons[i + 2][j].getBackground())) {
                    matchedPoints.add(new Point(i, j));
                    matchedPoints.add(new Point(i + 1, j));
                    matchedPoints.add(new Point(i + 2, j));
                }
            }
        }

        if (!matchedPoints.isEmpty()) {
            removeMatches(matchedPoints);
            refillBoard();
            updateScore(matchedPoints.size());
            return true;
        }
        return false;
    }

    private void removeMatches(List<Point> matchedPoints) {
        for (Point point : matchedPoints) {
            buttons[point.x][point.y].setBackground(null); // Set the background to null to "remove" the candy
        }
    }

    private void refillBoard() {
        Random random = new Random();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (buttons[i][j].getBackground() == null) {
                    buttons[i][j].setBackground(candyColors[random.nextInt(candyColors.length)]); // Fill with a random
                                                                                                  // candy color
                }
            }
        }
    }

    private void updateScore(int matches) {
        score += (matches * 10);
        scoreLabel.setText("Score: " + score);
    }

    private void showLevelCompletedDialog() {
        // Create a dialog to show "Level Completed" message
        JDialog dialog = new JDialog(this, "Level Completed", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);

        JLabel label = new JLabel("Congratulations! Level Completed!", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        dialog.add(label, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton nextLevelButton = new JButton("Next Level");
        nextLevelButton.addActionListener(e -> {
            dialog.dispose();
            score = 0; // Reset score for the next level
            scoreLabel.setText("Score: 0");
            initializeBoard((JPanel) getContentPane().getComponent(1)); // Reinitialize the board
        });

        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> {
            dialog.dispose();
            System.exit(0); // For simplicity, exit the game
        });

        buttonPanel.add(nextLevelButton);
        buttonPanel.add(homeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // New method to reset the game
    private void resetGame() {
        score = 0; // Reset score
        scoreLabel.setText("Score: 0");
        selectedCandy = null; // Reset selected candy reference
        JPanel boardPanel = (JPanel) getContentPane().getComponent(1); // Get the board panel
        boardPanel.removeAll(); // Clear the panel
        initializeBoard(boardPanel); // Reinitialize the board
        boardPanel.revalidate(); // Refresh the UI
        boardPanel.repaint(); // Repaint the board
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CandyCrushGame game = new CandyCrushGame();
            game.setVisible(true);
        });
    }
}
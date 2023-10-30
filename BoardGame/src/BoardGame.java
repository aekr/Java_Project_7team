import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class BoardGame {
    private JFrame frame;
    private JPanel boardPanel;
    private JButton[][] buttons;
    private JButton rollDiceButton;
    private JLabel playerPosition;
    private int currentPosition = 0;
    private final int totalCells = 12; // 12 outer cells

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                BoardGame window = new BoardGame();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public BoardGame() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(4, 5)); // 5 rows, 6 columns
        buttons = new JButton[5][6];
        Border emptyBorder = BorderFactory.createEmptyBorder();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                JButton button = new JButton();
                button.setEnabled(false);

                // Remove border for internal cells
                if (i != 0 && i != 3 && j != 0 && j != 4) {
                    button.setBorder(emptyBorder);
                }

                buttons[i][j] = button;
                boardPanel.add(button);
            }
        }

        frame.getContentPane().add(boardPanel, BorderLayout.CENTER);

        rollDiceButton = new JButton("Roll Dice");
        rollDiceButton.addActionListener(new RollDiceListener());
        frame.getContentPane().add(rollDiceButton, BorderLayout.SOUTH);

        playerPosition = new JLabel("Player is at position: " + currentPosition);
        frame.getContentPane().add(playerPosition, BorderLayout.NORTH);

        updateBoard();
    }

    private void updateBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                buttons[i][j].setBackground(Color.WHITE);
            }
        }

        int row = 0, col = 0;
        if (currentPosition < 6) {
            row = 0;
            col = currentPosition;
        } else if (currentPosition < 7) {
            row = currentPosition - 6;
            col = 4;
        } else {
            row = 4;
            col = 9 - currentPosition;
        }

        buttons[row][col].setBackground(Color.BLUE); // Set the background color to BLUE to indicate the player's position
    }

    private class RollDiceListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Random rand = new Random();
            int roll = rand.nextInt(1) + 1; // Dice roll (1-6)
            currentPosition = (currentPosition + roll) % totalCells; // Move and loop around
            playerPosition.setText("Player is at position: " + currentPosition);

            updateBoard(); // Update the board to reflect the new position
        }
    }
}

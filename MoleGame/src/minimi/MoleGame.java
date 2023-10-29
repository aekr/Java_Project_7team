package minimi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MoleGame {
    private JFrame frame;
    private JPanel gridPanel;
    private JButton[][] buttons;
    private int score;
    private int currentRow, currentCol;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                MoleGame window = new MoleGame();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public MoleGame() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(4, 4));
        frame.getContentPane().add(gridPanel, BorderLayout.CENTER);

        buttons = new JButton[4][4];
        score = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                JButton button = new JButton();
                button.setBackground(Color.WHITE);
                button.addActionListener(new ButtonClickListener(i, j));
                buttons[i][j] = button;
                gridPanel.add(button);
            }
        }

        randomizeButton();
    }

    private void randomizeButton() {
        if (currentRow >= 0 && currentCol >= 0) {
            buttons[currentRow][currentCol].setBackground(Color.WHITE);
        }

        Random rand = new Random();
        currentRow = rand.nextInt(4);
        currentCol = rand.nextInt(4);
        buttons[currentRow][currentCol].setBackground(Color.BLUE);
    }

    private class ButtonClickListener implements ActionListener {
        int row, col;

        ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (row == currentRow && col == currentCol) {
                score++;
                System.out.println("Score: " + score);
                randomizeButton();
            }
        }
    }
}

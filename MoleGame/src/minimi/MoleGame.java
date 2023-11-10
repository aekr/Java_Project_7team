package minimi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MoleGame {
    private JFrame frame;
    private JPanel gridPanel, topPanel;
    private JButton[][] buttons;
    private JLabel scoreLabel, timerLabel;
    private Timer gameTimer, clockTimer;
    private int score, time;
    private int currentRow, currentCol;
    private boolean isGameActive;

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
        // 기본 UI 설정
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        // 상단 패널 설정 (점수와 타이머 표시)
        topPanel = new JPanel();
        scoreLabel = new JLabel("점수: 0");
        timerLabel = new JLabel("시간: 0");
        topPanel.add(scoreLabel);
        topPanel.add(timerLabel);
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);

        // 그리드 패널 설정
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(4, 4));
        frame.getContentPane().add(gridPanel, BorderLayout.CENTER);

        // 버튼 설정
        buttons = new JButton[4][4];
        score = 0;
        time = 0;
        isGameActive = true;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                JButton button = new JButton();
                button.setBackground(Color.WHITE);
                button.addActionListener(new ButtonClickListener(i, j));
                buttons[i][j] = button;
                gridPanel.add(button);
            }
        }

        // 게임 타이머 설정 (30초 후 게임 종료)
        gameTimer = new Timer(30000, e -> endGame());
        gameTimer.start();

        // 시계 타이머 설정 (1초마다 시간 업데이트)
        clockTimer = new Timer(1000, e -> updateTimer());
        clockTimer.start();

        randomizeButton();
    }

    private void randomizeButton() {
        if (!isGameActive) {
            return;
        }

        if (currentRow >= 0 && currentCol >= 0) {
            buttons[currentRow][currentCol].setBackground(Color.WHITE);
        }

        Random rand = new Random();
        currentRow = rand.nextInt(4);
        currentCol = rand.nextInt(4);
        buttons[currentRow][currentCol].setBackground(Color.BLUE);
    }

    private void updateTimer() {
        if (isGameActive) {
            time++;
            timerLabel.setText("시간: " + time);
        }
    }

    private void endGame() {
        isGameActive = false;
        for (JButton[] buttonRow : buttons) {
            for (JButton button : buttonRow) {
                button.setEnabled(false);
            }
        }
        gameTimer.stop();
        clockTimer.stop();
        JOptionPane.showMessageDialog(frame, "게임 끝! 점수: " + score);
    }

    private class ButtonClickListener implements ActionListener {
        int row, col;

        ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isGameActive) {
                return;
            }

            if (row == currentRow && col == currentCol) {
                score++;
                scoreLabel.setText("점수: " + score);
                randomizeButton();
            }
        }
    }
}

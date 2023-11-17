import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MultiplayerMathGame {
    private JFrame frame;
    private JPanel questionAnswerPanel;
    private JTextField questionField, answerField;
    private JLabel scoreLabel, timeLabel;
    private JButton calculateButton;
    private TimerClass timer;
    private int currentPlayer = 0;
    private int[] playerScores = new int[2];

    public MultiplayerMathGame() {
        initializeUI();
        startGame();
    }

    private void initializeUI() {
        frame = new JFrame("Multiplayer Math Game");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Initialize UI components here (similar to Mathgame)
        // ...

        // Initialize TimerClass
        timer = new TimerClass(timeLabel, new JProgressBar());

        frame.setVisible(true);
    }

    private void startGame() {
        // Reset score and time for the current player
        playerScores[currentPlayer] = 0;
        updateUIForNewGame();

        // Start the timer
        timer.startNewQuestion();
    }

    private void endGame() {
        // Update score for the current player
        playerScores[currentPlayer] = // calculate the score

        // Check if it's time to switch players
        currentPlayer++;
        if (currentPlayer < 2) {
            JOptionPane.showMessageDialog(frame, "플레이어 " + currentPlayer + "  끝! 점수: " + playerScores[currentPlayer - 1]);
            startGame();
        } else {
            // Both players have played, show results
            showResults();
        }
    }

    private void updateUIForNewGame() {
        // Update UI elements for new game
        scoreLabel.setText("Score: 0");
        questionField.setText(""); // Set new question
        answerField.setText("");
        // More UI updates as needed
    }

    private void showResults() {
        // Compare scores and show results
    	String resultMessage = "플레이어 1 점수: " + playerScores[0] + "\n" + "플레이어 2 점수: " + playerScores[1] + "\n"
				+ (playerScores[0] > playerScores[1] ? "플레이어 1 승리!"
						: playerScores[0] < playerScores[1] ? "플레이어 2 승리!" : "무승부!");
		JOptionPane.showMessageDialog(frame, resultMessage);

        // Reset for a new game or exit
//        currentPlayer = 0;
//        startGame();
    }

    // More methods as needed for game logic and UI updates
}

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PlayerManager {
	private JFrame frame;
    private JPanel questionAnswerPanel;
    private JTextField questionField, answerField;
    private JLabel scoreLabel, timeLabel;
    private JButton calculateButton;
    private TimerClass timer;
    private int currentPlayer = 0;
    private int[] playerScores = new int[2];
    private Mathgame gameInstance;
    private static SoundManager soundManager;
    
    public PlayerManager(Mathgame game, int numPlayers, JTextField questionField, JTextField answerField, SoundManager soundManager) {
        this.gameInstance = game;
        this.currentPlayer = 0;
     // GUI 컴포넌트 설정
        this.questionField = questionField;
        this.answerField = answerField;
    }

    private void startGameForNextPlayer() {
		// JOptionPane.showMessageDialog(frame, "플레이어 1 끝! 점수: " + playerScores[0]);
    	
		JOptionPane.showMessageDialog(frame, "스페이스바를 눌러 게임을 시작하세요.");
		currentPlayer = 1; // 두 번째 플레이어로 설정
		
		answerField.setText("");
		
		String initialProblem = RandomLogic.generateProblem();
        questionField.setText(initialProblem);
        
        timer.start();
        
        timer.startNewQuestion();
	}

    public void setTimer(TimerClass timer) {
        this.timer = timer;
    }

    public void setPlayerScore(int player, int score) {
          playerScores[player] = score;
    }
    
    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void endGame() {
    	soundManager = gameInstance.soundManager;
    	Mathgame.endPlayerTurn();
		/*
		 * JOptionPane.showMessageDialog(frame, "플레이어 1 끝! 점수: " + score1);
		 * JOptionPane.showMessageDialog(frame, "스페이스바를 눌러 게임을 시작하세요.");
		 */
        currentPlayer++;
        if (currentPlayer < 2) {
        	
            JOptionPane.showMessageDialog(frame, "플레이어 " + currentPlayer + "  끝! 점수: " + playerScores[currentPlayer - 1]);
            startGameForNextPlayer();
        } else {
            // Both players have played, show results
            showResults();
            
            soundManager.stopAllSounds();
            frame.dispose();
        }
    }

    public int getCurrentPlayer() {
        return currentPlayer;
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
}
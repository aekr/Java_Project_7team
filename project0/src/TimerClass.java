import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class TimerClass {
	private double MainTime = 5.0; //불변 시간
    private double initialTime = 5.0; // 초기 시간
    private double timeLeft; // 현재 남은 시간
    private Timer timer;
    private JLabel timeLabel;
    private JProgressBar progressBar;
    private PlayerManager playermanager;
    private boolean isCriticalState = false;
    private Mathgame gameInstance;
    private Timer backgroundSwitchTimer;
    
    class BackgroundPanel extends JPanel {
		private Image backgroundImage;
		  private int x, y;

		public BackgroundPanel(String imagePath) {
			try {
				backgroundImage = ImageIO.read(getClass().getResource(imagePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
			setLayout(null);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (backgroundImage != null) {
				g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
			}
		}
	}

    public TimerClass(JLabel timeLabel, JProgressBar progressBar, PlayerManager playerManager, Mathgame game) {
    	JFrame frame = game.getFrame();
    	initialTime = MainTime;
    	timeLeft = initialTime;
        this.timeLabel = timeLabel;
        this.progressBar = progressBar;
        this.playermanager = playerManager;
        this.gameInstance = game;
        this.progressBar.setMaximum((int)(initialTime * 1000));

        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	 // 남은 시간이 1.5초 이하일 때 프로그래스 바 색상 변경
            	if (timeLeft <= 1.5 && !isCriticalState) {
                    isCriticalState = true;
                    progressBar.setForeground(Color.RED); // 빨간색으로 변경
                    
                 // centerBackground3 추가
                    gameInstance.getCenterBackground2().setVisible(false);
                    gameInstance.getCenterBackground3().setVisible(true);
                    gameInstance.controlTimer1(false);
                    frame.revalidate(); // 프레임 레이아웃 재계산
                    frame.repaint(); // 프레임 다시 그리기
                    
                } else if (timeLeft > 1.5 && isCriticalState) {
                	isCriticalState = false;
                    progressBar.setForeground(Color.GREEN); // 그 외에는 초록색으로 유지
                 // 타이머 초기화
                    backgroundSwitchTimer = new Timer(500, ev -> {
                        SwingUtilities.invokeLater(() -> {
                            gameInstance.getCenterBackground2().setVisible(true);
                            gameInstance.getCenterBackground3().setVisible(false);
                            gameInstance.controlTimer1(true);
                            frame.revalidate();
                            frame.repaint();
                        });
                    });
                    backgroundSwitchTimer.setRepeats(false); // 타이머가 한 번만 실행되도록 설정
                    backgroundSwitchTimer.start();
                }
                if (timeLeft > 0) {
                    timeLeft -= 0.05;
                    progressBar.setValue((int) (timeLeft * 1000));
                    updateTimeLabel();
                } else {
                	timeLeft = 0;
                	updateTimeLabel();
                    timer.stop();
                    
                    playermanager.endGame(); // 게임 종료 로직
                }
            }
        });
    }
    
    public void resetTimerForNewPlayer() {
        initialTime = MainTime; // MainTime을 사용하여 초기 시간 재설정
        timeLeft = initialTime; // 남은 시간도 초기 시간으로 재설정
        // 타이머를 다시 시작할 준비
    }

    public void startNewQuestion() {
        timeLeft = initialTime; // 새 문제 시작 시 초기 시간으로 재설정
    }

    public void correctAnswer() {
        initialTime *= 0.9; // 정답을 맞출 때마다 초기 시간 5% 감소
    }
    
	/*
	 * private void updateTimeLabel() { long seconds = timeLeft / 1000; long
	 * milliseconds = elapsedTime % 1000; String time = String.format("%02d.%03d",
	 * seconds, milliseconds); timeLabel.setText(time); }
	 */

    private void updateTimeLabel() {
        timeLabel.setText("남은 시간: " + String.format("%.2f", timeLeft) + "초");
        timeLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
    }

    public void start() {
        timer.start();
    }
}

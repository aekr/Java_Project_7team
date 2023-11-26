import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class TimerClass {
	private double MainTime = 5.0; //불변 시간
    private double initialTime = 5.0; // 초기 시간
    private double timeLeft; // 현재 남은 시간
    private Timer timer;
    private JLabel timeLabel;
    private JProgressBar progressBar;
    private PlayerManager playermanager;

    public TimerClass(JLabel timeLabel, JProgressBar progressBar, PlayerManager playerManager) {
    	initialTime = MainTime;
    	timeLeft = initialTime;
        this.timeLabel = timeLabel;
        this.progressBar = progressBar;
        this.playermanager = playerManager;
        this.progressBar.setMaximum((int)(initialTime * 1000));

        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	 // 남은 시간이 1.5초 이하일 때 프로그래스 바 색상 변경
                if (timeLeft <= 1.5) {
                    progressBar.setForeground(Color.RED); // 빨간색으로 변경
                } else {
                    progressBar.setForeground(Color.GREEN); // 그 외에는 초록색으로 유지
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
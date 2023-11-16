
import javax.swing.*;
import java.awt.event.*;

public class TimerClass {
	private double timeLeft = 5.0; // 시작 시간을 5초로 설정
	private Timer timer;
	private JLabel timeLabel;
	private JProgressBar progressBar; // JProgressBar 추가

	public TimerClass(JLabel timeLabel, JProgressBar progressBar) {
		this.timeLabel = timeLabel;
		this.progressBar = progressBar; // 프로그레스 바 초기화
		this.progressBar.setMaximum(5000); // 최대값을 설정 (5초를 밀리초로 환산)
		this.progressBar.setValue(5000); // 초기값 설정

		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (timeLeft > 0) {
					timeLeft *= 0.95; // 시간을 5% 감소
					progressBar.setValue((int) (timeLeft * 1000)); // 프로그레스 바 값을 업데이트
					updateTimeLabel();
				} else {
					timer.stop();
					// 게임 종료 로직
				}
			}
		});
	}

	private void updateTimeLabel() {
		timeLabel.setText("남은 시간: " + String.format("%.2f", timeLeft) + "초");
	}

	public void start() {
		timer.start();
	}
}

/*
 * private void updateTimeLabel() { timeLabel.setText("Time left: " + timeLeft +
 * "s"); }
 */

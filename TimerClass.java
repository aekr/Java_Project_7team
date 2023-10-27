
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimerClass {
    private int timeLeft = 60; // 60 seconds
    private Timer timer;
    private JLabel timeLabel;

    public TimerClass(JLabel timeLabel) {
        this.timeLabel = timeLabel;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timeLeft--;
                    updateTimeLabel();
                } else {
                    timer.stop();
                    // Game over logic
                }
            }
        });
    }

    public void start() {
        timer.start();
    }

    private void updateTimeLabel() {
        timeLabel.setText("Time left: " + timeLeft + "s");
    }
}

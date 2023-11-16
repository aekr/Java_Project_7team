import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Game1 extends JFrame{
	private JFrame j;
    private JLabel timeLabel;
    private JLabel ready;
    private JLabel p1;
    private JLabel p2;
    private long elapsedTime;
    private Timer timer;
    private long startTime;
    private int n;
    private long record1;
    private long record2;
    private boolean isRunning=true;
    private boolean start=true;
    private int alpha = 255;
    private boolean key1 = true;
    private boolean key2 = true;
    public Game1(JFrame j) {
        setTitle("Game1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        Container c=getContentPane();
        c.setLayout(null);
        timeLabel = new JLabel("00.000");
        ready=new JLabel("준비완료 : spacebar클릭");
        ready.setFont(new Font("맑음 고딕", Font.BOLD, 30));
        timeLabel.setFont(new Font("Arial", Font.BOLD, 50));
        ready.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setForeground(new Color(0, 0, 0, alpha));
        ready.setLocation(80,80);
        ready.setSize(400,30);
        timeLabel.setLocation(120,20);
        timeLabel.setSize(300,50);
        c.add(timeLabel);
        c.add(ready);
        p1=new JLabel("");
        p2=new JLabel("");
        c.add(p1);
        c.add(p2);
        p1.setLocation(50,300);
        p1.setSize(300,30);
        p2.setLocation(400,300);
        p2.setSize(300,30);
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
            	Random random=new Random();
                if (e.getKeyChar() == ' '&&start) {
                	start=false;
                    n=random.nextInt(6) + 10;
                    String s=n+"초 맞추기";
                    ready.setText(s);
                    start();
                }
                else if(key1&&!(start)&&(e.getKeyChar() == 'z'||e.getKeyChar() == 'Z')){
                	pause1();
                	key1=false;
                	if(key2==false) {
                		timer.stop();
                        timeLabel.setForeground(new Color(0,0,0,255));
                		if(Math.abs(n*1000-record1)>Math.abs(n*1000-record2)) {
                			ready.setText("플레이어 2 승리");
                			setVisible(false);
                        	j.setVisible(true);
                		}
                		else if(Math.abs(n*1000-record1)<Math.abs(n*1000-record2)) {
                			ready.setText("플레이어 1 승리");
                			setVisible(false);
                        	j.setVisible(true);
                		}
                		else {
                			ready.setText("무승부");
                			setVisible(false);
                        	j.setVisible(true);
                		}
                	}
                }
                else if(key2&&!(start)&&(e.getKeyChar() == '/'||e.getKeyChar() == '?')){
                	pause2();
                	key2=false;
                	if(key1==false) {
                		timer.stop();
                        timeLabel.setForeground(new Color(0,0,0,255));
                		if(Math.abs(n*1000-record1)>Math.abs(n*1000-record2)) {
                			ready.setText(Long.toString(n)+"초 맞추기 플레이어 2 승리");
                        	setVisible(false);
                        	j.setVisible(true);
                		}
                		else if(Math.abs(n*1000-record1)<Math.abs(n*1000-record2)) {
                			ready.setText(Long.toString(n)+"초 맞추기 플레이어 1 승리");
                			setVisible(false);
                        	j.setVisible(true);
                		}
                		else {
                			ready.setText("무승부");
                			setVisible(false);
                			j.setVisible(true);
                		}
                	}
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        timer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	alpha-=1;
            	if(alpha<0) {
            		alpha=0;
            	}
                if (isRunning) {
                    long currentTime = System.currentTimeMillis();
                    elapsedTime = currentTime - startTime;
                    updateTimeLabel();
                }
                timeLabel.setForeground(new Color(0,0,0,alpha));
            }
        });

        setVisible(true);
    }

    private void start() {
        isRunning = true;
        startTime = System.currentTimeMillis();
        timer.start();
    }

    private void pause1() {
    	record1=elapsedTime;
        p1.setText(Long.toString(record1/1000)+"."+(Long.toString(record1%1000/100))+(Long.toString(record1%1000%100/10))+(Long.toString(record1%1000%100%10)));
    }
    private void pause2() {
    	record2=elapsedTime;
    	p2.setText(Long.toString(record2/1000)+"."+(Long.toString(record2%1000/100))+(Long.toString(record2%1000%100/10))+(Long.toString(record2%1000%100%10)));
    }
    private void reset() {
        isRunning = false;
        timer.stop();
        timeLabel.setText("00.000");
    }

    private void updateTimeLabel() {
        long seconds = elapsedTime / 1000;
        long milliseconds = elapsedTime % 1000;
        String time = String.format("%02d.%03d", seconds, milliseconds);
        timeLabel.setText(time);
    }

    
}
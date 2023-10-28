//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.util.Random;
//import java.util.Timer;
//import java.util.TimerTask;
//public class test {
//	public static void main(String[] args)
//	{
//		
//		System.out.println(System.currentTimeMillis()/1000);
//		System.out.println(System.currentTimeMillis()/1000);
//	}
//}
//
//



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Main extends JFrame {
    private JFrame frame;
    private ImagePanel keyPanel; // JPanel 대신 ImagePanel 사용
    private JLabel resultLabel;
    private int successCount;
    private String expectedKeys;
    private int index;
    private Timer gameTimer;
    private boolean game = true;
    private Image background=new ImageIcon(Main.class.getResource("1.png")).getImage();
    public Main() 
    {
        frame = new JFrame("Taja Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 900);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        keyPanel = new ImagePanel(); // ImagePanel 인스턴스 생성
        resultLabel = new JLabel("");
        resultLabel.setHorizontalAlignment(JLabel.CENTER);

        keyPanel.setLayout(new GridBagLayout());

        frame.add(keyPanel, BorderLayout.CENTER);
        frame.add(resultLabel, BorderLayout.NORTH);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameTimer == null && e.getKeyCode() == KeyEvent.VK_SPACE) {
                    startGameTimer();
                } else if (game) {
                    checkKey(e.getKeyChar());
                }
            }
        });

        startNewGame();
        frame.setVisible(true);
    }
    class ImagePanel extends JPanel { // 배경 이미지를 그리는 JPanel 서브클래스
        private Image background = new ImageIcon(Main.class.getResource("ex2.png")).getImage();

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(background, 0, 0, this);
        }
    }
    private void startGameTimer() {
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
            	JLabel label = new JLabel("Game Over. Scores: " + successCount);
                keyPanel.add(label);
                gameTimer.cancel();
                game = false;
            }
        }, 30000); // 30 seconds
    }

    private void checkKey(char key) {
        char expectedKey = expectedKeys.charAt(index);
        if (key == expectedKey) {
            removeKeyLabel();
            index++;
            if (index == expectedKeys.length()) {
                successCount++;
                resultLabel.setText("");
                startNewGame();
            }
        } else {
            resultLabel.setText("");
            startNewGame();
        }
    }

    private void startNewGame() {
    	expectedKeys = generateRandomKeys(8);
        displayKeysInPanel(expectedKeys);
        index = 0;
        frame.requestFocus();
    }

    private void displayKeysInPanel(String keys) {
        keyPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        for (char key : keys.toCharArray()) {
            JLabel keyLabel = new JLabel(String.valueOf(key));
            keyLabel.setOpaque(true);
            keyLabel.setBackground(Color.LIGHT_GRAY);
            keyLabel.setPreferredSize(new Dimension(40, 40));
            keyLabel.setHorizontalAlignment(JLabel.CENTER);
            keyLabel.setFont(new Font("Arial", Font.PLAIN, 24));
            keyPanel.add(keyLabel, gbc); // GridBagConstraints 사용
        }
        keyPanel.revalidate();
        keyPanel.repaint();
    }

    private void removeKeyLabel() {
        Component[] components = keyPanel.getComponents();
        if (components.length > 0) {
            keyPanel.remove(0);
            keyPanel.revalidate();
            keyPanel.repaint();
        }
    }

    private String generateRandomKeys(int length) {
        String validKeys = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char randomChar = validKeys.charAt(random.nextInt(validKeys.length()));
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}



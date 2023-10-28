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
                JLabel gameOverLabel = new JLabel("Game Over. Scores: " + successCount);
                gameOverLabel.setFont(new Font("Arial", Font.BOLD, 50)); // 폰트 크기와 스타일 설정
                gameOverLabel.setHorizontalAlignment(JLabel.CENTER); // 라벨 내용 중앙 정렬
                
                // keyPanel에 라벨 추가
                keyPanel.removeAll(); // 기존에 있던 컴포넌트 제거
                keyPanel.setLayout(new BorderLayout()); // 레이아웃 매니저를 BorderLayout으로 설정
                keyPanel.add(gameOverLabel, BorderLayout.CENTER); // 라벨을 패널 중앙에 추가
                
                keyPanel.revalidate();
                keyPanel.repaint();
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





//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.util.Random;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class Main extends JFrame {
//    private JFrame frame;
//    private JPanel keyPanel; // 패널을 사용하여 키를 가운데 표시
//    private JLabel resultLabel;
//    private int successCount;
//    private int totalAttempts;
//    private String expectedKeys;
//    private int index;
//
//    public Main() {
//        frame = new JFrame("Key Input Game");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(1300, 900);
//        frame.setLayout(new FlowLayout());
//        setLocationRelativeTo(null);
//
//        keyPanel = new JPanel(); // 패널로 키를 표시
//        resultLabel = new JLabel("");
//
//        keyPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // 가운데 정렬을 위한 설정
//
//        frame.add(keyPanel); // keyPanel을 프레임에 추가
//        frame.add(resultLabel);
//        Timer timer = new Timer();  
//        frame.addKeyListener(new KeyAdapter() {
//        private long current_time;
//        private long after_time;
//        private boolean a = true;
//            @Override
//            public void keyTyped(KeyEvent e) 
//            {     
//	        	System.out.println("s");
//            	after_time = System.currentTimeMillis()/1000;
//                if (e.getKeyCode() == KeyEvent.VK_SPACE && a)
//                {
//                	current_time = System.currentTimeMillis()/1000;
//                	a = false;
//                }
//                char expectedKey = expectedKeys.charAt(index);
//                char actualKey = e.getKeyChar();
//            	if (after_time - current_time < 30)
//		        {
//		        	resultLabel.setText("Game Over Scores" + successCount);
//		        	System.out.println("a");
//		        }
//            	if (actualKey == expectedKey) 
//                {
//		        	System.out.println("c");
//                    removeKeyLabel(); // 맞았을 때 키 하나씩 사라지게
//                    index++;
//                    if (index == expectedKeys.length() ) {
//                        successCount++;
//                        resultLabel.setText("Success!");
//                        timer.schedule(new TimerTask() 
//                        {
//                            @Override
//                            public void run() {
//                                SwingUtilities.invokeLater(() -> startNewGame());
//                            }
//                        }, 1000);
//                    }
//                }else 
//                {
//		        	System.out.println("d");
//                    startNewGame(); // 틀렸을 때 초기화
//                }
//            }
//        });
//
//        startNewGame();
//        frame.setVisible(true);
//    }
//
//    private void startNewGame() 
//    {
//    	System.out.println("f");
//        expectedKeys = generateRandomKeys(8);
//        displayKeysInPanel(expectedKeys); // 패널에 새 키를 표시
//        index = 0;
//        frame.requestFocus();
//    }
//
//    private void displayKeysInPanel(String keys) {
//        keyPanel.removeAll();
//        for (char key : keys.toCharArray()) {
//            JLabel keyLabel = new JLabel(String.valueOf(key));
//            keyLabel.setOpaque(true);
//            keyLabel.setBackground(Color.LIGHT_GRAY);
//            keyLabel.setPreferredSize(new Dimension(40, 40));
//            keyLabel.setHorizontalAlignment(JLabel.CENTER);
//            keyLabel.setFont(new Font("Arial", Font.PLAIN, 24));
//            keyPanel.add(keyLabel);
//        }
//    }
//
//    private void removeKeyLabel() {
//        Component[] components = keyPanel.getComponents();
//        if (components.length > 0) {
//            keyPanel.remove(components[0]);
//            keyPanel.revalidate();
//            keyPanel.repaint();
//        }
//    }
//
//    private String generateRandomKeys(int length) {
//        String validKeys = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        Random random = new Random();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < length; i++) {
//            char randomChar = validKeys.charAt(random.nextInt(validKeys.length()));
//            sb.append(randomChar);
//        }
//        return sb.toString();
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new Main());
//    }
//}


//import javax.swing.*;
//import java.awt.event.*;
//import java.awt.*;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.*;
//public class Main extends JFrame {
//
//    private int successCount = 0;
//    private JLabel statusLabel = new JLabel("Press the space key to start.");
//    private CountdownTimer timer;
//    private String keys = "abcdefghijklmnopqrstcvwxyz0123456789[];',./";
//    private String targetKeys;
//    private int currentIndex = 0;
//    private JLabel keyLabel = new JLabel("", SwingConstants.CENTER);
//
//    public Main() {
//        setTitle("Sample Frame");
//        setSize(1300, 900);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        // 상단 레이아웃 설정
//        JPanel topPanel = new JPanel();
//        topPanel.add(statusLabel);
//
//        // 중앙 패널 설정
//        JPanel centerPanel = new JPanel();
//        centerPanel.add(keyLabel);
//
//        // 레이아웃 구성
//        setLayout(new BorderLayout());
//        add(topPanel, BorderLayout.NORTH);
//        add(centerPanel, BorderLayout.CENTER);
//        // KeyListener 추가
//        addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_SPACE && timer == null) {
//                    timer = new CountdownTimer(30);
//                    timer.start();
//                    generateTargetKeys();
//                    statusLabel.setText("Target Keys: " + targetKeys);
//                } else if (timer != null && timer.isRunning()) {
//                    checkKey(e.getKeyChar());
//                }
//            }
//        });
//
//        // 패널을 추가하여 레이아웃 구성
//        setLayout(new BorderLayout());
//        add(topPanel, BorderLayout.NORTH);
//        setFocusable(true);
//        requestFocus();
//    }
//
//    private void generateTargetKeys() {
//        StringBuilder sb = new StringBuilder();
//        Random rand = new Random();
//        for (int i = 0; i < 8; i++) {
//            sb.append(keys.charAt(rand.nextInt(keys.length())));
//        }
//        targetKeys = sb.toString();
//        currentIndex = 0;
//        updateKeyLabel();
//    }
//    private void updateKeyLabel() {
//        if (currentIndex < targetKeys.length()) {
//            keyLabel.setText(String.valueOf(targetKeys.charAt(currentIndex)));
//            keyLabel.setFont(new Font("Arial", Font.BOLD, 40)); // 글자 크기 설정
//            keyLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 테두리 추가
//            keyLabel.setPreferredSize(new Dimension(100, 100)); // 레이블 크기 설정
//        } else {
//            keyLabel.setText("");
//        }
//    }
//    private void checkKey(char key) {
//        if (key == targetKeys.charAt(currentIndex)) {
//            currentIndex++;
//            if (currentIndex == targetKeys.length()) {
//                successCount++;
//                statusLabel.setText("Success! Count: " + successCount);
//                generateTargetKeys();
//            }
//        } else {
//            statusLabel.setText("Failed! Try again. Target Keys: " + targetKeys);
//            currentIndex = 0; // Reset the current index if a wrong key is pressed
//        }
//    }
//
//    public class CountdownTimer {
//
//        private int remainingSeconds;
//        private Timer timer;
//        private boolean isRunning;
//
//        public CountdownTimer(int seconds) {
//            this.remainingSeconds = seconds;
//            this.timer = new Timer();
//            this.isRunning = false;
//        }
//
//        public void start() {
//            if (!isRunning) {
//                isRunning = true;
//                timer.scheduleAtFixedRate(new TimerTask() 
//                {
//                    @Override
//                    public void run() {
//                        if (remainingSeconds > 0) {
//                            remainingSeconds--;
//                        } else {
//                            timer.cancel();
//                            isRunning = false;
//                            statusLabel.setText("Time's up! Total Success Count: " + successCount);
//                        }
//                    }
//                }, 0, 1000);
//            }
//        }
//
//        public boolean isRunning() {
//            return isRunning;
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new Main().setVisible(true);
//        });
//    }
//}

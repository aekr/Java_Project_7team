import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

class BackgroundMusic {
    private Clip clip;

    public void playOneBackgroundMusic(String filePath) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start(); // 한번 반복
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    public void playLoopBackgroundMusic(String filePath) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // 무한 반복
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    public void stopBackgroundMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
public class Tajagame extends JFrame {
    private JFrame frame;
    private ImagePanel keyPanel; 
    private JLabel resultLabel;
    private int successCount;
    private String expectedKeys;
    private int index;
    private boolean game = true;
    private Timer gameTimer;
    private Timer gameTimer2;
    private int playercount_1;
    public boolean playerturn = false;
    private int playercount_2;
    public int winner;
    private JFrame main;
   private KeyListener first_Listener;
   private KeyListener second_Listener;
   private BoardGame MainFrame;
   private BackgroundMusic bgm = new BackgroundMusic();
    public Tajagame(JFrame j, BoardGame Main)
    {
    	MainFrame = Main;
    	main = j;
        frame = new JFrame("Taja Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 800);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        keyPanel = new ImagePanel(); 
        resultLabel = new JLabel("");
        resultLabel.setHorizontalAlignment(JLabel.CENTER);

        first_start();
    }
    class ImagePanel extends JPanel { 
        private Image background = new ImageIcon(Tajagame.class.getResource("tajagame_background.jpg")).getImage();

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
    private void gameOver(int player_1,int player_2) 
    {
       if (player_1 > player_2)
       {
          JOptionPane.showMessageDialog(this, player_1+ " : " + player_2 + "으로 1번 플레이어의 승리입니다!", "\nGame Over!", JOptionPane.INFORMATION_MESSAGE);
          MainFrame.P1_win();
          main.setVisible(true);
          bgm.stopBackgroundMusic();
          frame.dispose();
       }
       else if (player_1 < player_2)
       {
          JOptionPane.showMessageDialog(this, player_1+ " : " + player_2 + "으로 2번 플레이어의 승리입니다!", "\nGame Over!", JOptionPane.INFORMATION_MESSAGE);
          MainFrame.P2_win();
          main.setVisible(true);
          bgm.stopBackgroundMusic();
          frame.dispose();
       }
       else
       {
          JOptionPane.showMessageDialog(this, player_1+ " : " + player_2 + "으로 아쉽게 무승부입니다!", "\nGame Over!", JOptionPane.INFORMATION_MESSAGE);
          MainFrame.draw();
          main.setVisible(true);
          bgm.stopBackgroundMusic();
          frame.dispose();
       }
     }
    private void startGameTimer() {
        bgm.playLoopBackgroundMusic("tajagame.wav");
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
            JLabel gameOverLabel = new JLabel("");
            playercount_1 = successCount;
            keyPanel.removeAll(); 
            keyPanel.setLayout(new BorderLayout()); 
            keyPanel.add(gameOverLabel, BorderLayout.CENTER); 
            
            keyPanel.revalidate();
            keyPanel.repaint();
            game = false;
            second_start();
            }
        }, 15000);
    }
    private void startGameTimer2() 
    {
        gameTimer2 = new Timer();
        gameTimer2.schedule(new TimerTask() {
            @Override
            public void run() {
            JLabel gameOverLabel2 = new JLabel("");
            gameOverLabel2.setHorizontalAlignment(JLabel.CENTER); 
            playercount_2 = successCount - playercount_1;
            keyPanel.removeAll();
            keyPanel.setLayout(new BorderLayout()); 
            keyPanel.add(gameOverLabel2, BorderLayout.CENTER); 
            
            keyPanel.revalidate();
            keyPanel.repaint();
        
            gameOver(playercount_1,playercount_2);
            gameTimer2.cancel();
            game = false;
            }
        }, 15000); 
    }
    
    private void checkKey(char key) {
        char expectedKey = expectedKeys.charAt(index);
        if (key == expectedKey) {
            removeKeyLabel();
            index++;
            BackgroundMusic bgm = new BackgroundMusic();
            bgm.playOneBackgroundMusic("tajagame_correct.wav");
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
       expectedKeys = generateRandomKeys(5);
        displayKeysInPanel(expectedKeys);
        index = 0;
        frame.requestFocus();
    }

    private void displayKeysInPanel(String keys) {
        keyPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        for (char key : keys.toCharArray()) {
            JLabel keyLabel = new JLabel(String.valueOf(key));
            try {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("DNFBitBitv2\\DNFBitBitv2.ttf")).deriveFont(35f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);
                keyLabel.setFont(customFont);
            } catch (IOException | FontFormatException e) {
                e.printStackTrace();
            }
            keyLabel.setOpaque(true);
            keyLabel.setBackground(Color.CYAN);
            keyLabel.setPreferredSize(new Dimension(40, 40));
            keyLabel.setHorizontalAlignment(JLabel.CENTER);
            
            keyPanel.add(keyLabel, gbc); 
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

    private String generateRandomKeys(int length) 
    {
        String validKeys = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char randomChar = validKeys.charAt(random.nextInt(validKeys.length()));
            sb.append(randomChar);
        }
        return sb.toString();
    }
    void first_start()
    {
        JLabel gamestartLabel = new JLabel("Space를 눌러 1번 플레이어의 게임을 시작하세요"); 
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("DNFBitBitv2\\DNFBitBitv2.ttf")).deriveFont(35f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            gamestartLabel.setFont(customFont);
        } catch (IOException | FontFormatException e) 
        {
            e.printStackTrace();
        }
        gamestartLabel.setForeground(Color.cyan);
        keyPanel.setLayout(new GridBagLayout());
        keyPanel.add(gamestartLabel);
        frame.add(keyPanel, BorderLayout.CENTER);
        frame.add(resultLabel, BorderLayout.NORTH);
        
        first_Listener = new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameTimer == null && e.getKeyCode() == KeyEvent.VK_SPACE) 
                {
                   keyPanel.removeAll();
                   startNewGame();
                    startGameTimer(); 
                } else if (game) {
                    checkKey(e.getKeyChar());
                }
            }
        };
        frame.addKeyListener(first_Listener);
        frame.setVisible(true); 
    }
    void second_start()
    {
       gameTimer.cancel();
        keyPanel.removeAll();
       frame.requestFocus();
        frame.removeKeyListener(first_Listener);
        Timer tmp = new Timer();
        JLabel gamestartLabel2 = new JLabel("Space를 눌러 2번 플레이어의 게임을 시작하세요");
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("DNFBitBitv2\\DNFBitBitv2.ttf")).deriveFont(35f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            gamestartLabel2.setFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        gamestartLabel2.setForeground(Color.cyan);
        keyPanel.setLayout(new GridBagLayout());
        keyPanel.add(gamestartLabel2);
        frame.add(keyPanel, BorderLayout.CENTER);
        frame.add(resultLabel, BorderLayout.NORTH);
        
        second_Listener = new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) 
            {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) 
                {
                   game = true;
                   keyPanel.removeAll();
                   startNewGame();
                    startGameTimer2();
                } else if (game) {
                    checkKey(e.getKeyChar());
                }
            }
        };
        frame.addKeyListener(second_Listener);
        frame.setVisible(true);
    }
}

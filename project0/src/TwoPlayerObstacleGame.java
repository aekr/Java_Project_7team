import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.awt.FontFormatException;
class BackgroundMusic2{
    private Clip clip;

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
class Player {
    private int x, y;
    private int width, height;
    private int speed;
    private boolean leftPressed, rightPressed;

    public Player(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.leftPressed = false;
        this.rightPressed = false;
    }

    public void move() {
        if (leftPressed && x > 0) {
            x -= speed;
        }
        if (rightPressed && x < GamePanel.WIDTH - width) {
            x += speed;
        }
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

class Obstacle {
    private int x, y;
    private int width, height;
    private int speed;

    public Obstacle(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public void move() {
        y += speed;
        if (y > GamePanel.HEIGHT) {
            resetPosition();
        }
    }

    public void resetPosition() {
        Random random = new Random();
        x = random.nextInt(GamePanel.WIDTH - width);
        y = -height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

class GamePanel extends JPanel implements KeyListener {
    public static final int WIDTH = 1300;
    public static final int HEIGHT = 800;
    private Player player1, player2;
    private List<Obstacle> obstacles;
    private Image backgroundImage;
    private int overcount = 0;
    public GamePanel(JFrame j, JFrame minigameframe ,BackgroundMusic2 bgm ) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        requestFocusInWindow();
        addKeyListener(this);
        backgroundImage = new ImageIcon(TwoPlayerObstacleGame.class.getResource("twoplayerobs_background.jpg")).getImage(); // 이미지 경로

        player1 = new Player(WIDTH - 70, 750, 40, 20, 5);
        player2 = new Player(50, 750, 40, 20, 5);

        obstacles = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int obstacleHeight = random.nextInt(HEIGHT / 2); 
            obstacles.add(new Obstacle(random.nextInt(WIDTH + 40), -obstacleHeight + 20, 40, obstacleHeight, 12));
        }

        Timer timer = new Timer(10, e -> {
            player1.move();
            player2.move();
            for (Obstacle obstacle : obstacles) {
                obstacle.move();
                if ((player1.getX() < obstacle.getX() + obstacle.getWidth() && player1.getX() + player1.getWidth() > obstacle.getX()
                        && player1.getY() < obstacle.getY() + obstacle.getHeight() && player1.getY() + player1.getHeight() > obstacle.getY()) && overcount == 0 ) 
                {
                	overcount +=1;
                    gameOver(1,minigameframe, j ,bgm);
                
                }
                if ((player2.getX() < obstacle.getX() + obstacle.getWidth() && player2.getX() + player2.getWidth() > obstacle.getX()
                        && player2.getY() < obstacle.getY() + obstacle.getHeight() && player2.getY() + player2.getHeight() > obstacle.getY()) && overcount == 0) 
                {
                	overcount +=1;
                    gameOver(2,minigameframe, j ,bgm);
                }
            }

            repaint();
        });
        timer.start();
    }
    

    private void gameOver(int n, JFrame minigameframe, JFrame mainFrame,BackgroundMusic2 bgm)  {
        JOptionPane.showMessageDialog(this, n + "번 플레이어의 승리!", "Game Over!", JOptionPane.INFORMATION_MESSAGE);
        
        bgm.stopBackgroundMusic(); // 배경 음악 정지
        minigameframe.dispose(); // 미니게임 프레임 닫기
        mainFrame.setVisible(true); // 메인 프레임을 다시 보이게 함
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        
        g.setColor(Color.blue);
        g.fillRect(player1.getX(), player1.getY(), player1.getWidth(), player1.getHeight());

        g.setColor(Color.red);
        g.fillRect(player2.getX(), player2.getY(), player2.getWidth(), player2.getHeight());

        g.setColor(Color.white); // 장애물의 색상을 여기서 설정
        for (Obstacle obstacle : obstacles) {
            g.fillRect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player1.setLeftPressed(true);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player1.setRightPressed(true);
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            player2.setLeftPressed(true);
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            player2.setRightPressed(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player1.setLeftPressed(false);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player1.setRightPressed(false);
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            player2.setLeftPressed(false);
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            player2.setRightPressed(false);
        }
    }
}

class BackgroundPanel_twoplayerobs extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel_twoplayerobs(String imagePath) {
        backgroundImage = new ImageIcon(TwoPlayerObstacleGame.class.getResource(imagePath)).getImage();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
public class TwoPlayerObstacleGame {
	JFrame frame;
    private BackgroundMusic2 bgm = new BackgroundMusic2();
    public void startGame(JFrame j) {
    	frame = new JFrame("Two Player Obstacle Game");
        frame.setSize(GamePanel.WIDTH, GamePanel.HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        
        BackgroundPanel_twoplayerobs backgroundPanel = new BackgroundPanel_twoplayerobs("twoplayerobs_background.jpg"); // 이미지 경로
        frame.setContentPane(backgroundPanel);
        backgroundPanel.setLayout(new BorderLayout());

        JLabel gamestartLabel = new JLabel("Space를 눌러 게임을 시작하세요", SwingConstants.CENTER); //글꼴추가
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("DNFBitBitv2\\DNFBitBitv2.ttf")).deriveFont(35f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            gamestartLabel.setFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        backgroundPanel.add(gamestartLabel, BorderLayout.CENTER);
        gamestartLabel.setForeground(Color.white);
        
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    frame.remove(gamestartLabel);
                    bgm.playLoopBackgroundMusic("twoplayerobs.wav");
                    GamePanel gamePanel = new GamePanel(j,frame, bgm);
                    frame.add(gamePanel);
                    frame.pack();
                    frame.revalidate();
                    gamePanel.requestFocusInWindow();
                }
            }
        });

        frame.setVisible(true);
    }
}
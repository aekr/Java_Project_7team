import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game2 {
    private JFrame frame;
    private GamePanel gamePanel;
    private int player1X, player1Y, player2X, player2Y, ballX, ballY;
    private int playerSizeX,playerSizeY, ballSize, playerSpeed ;
    private double ballSpeedMultiplier;
    private double initialBallSpeedX, initialBallSpeedY;
    private boolean start=true;
    private boolean upPressed, downPressed,wPressed,sPressed;
    public void move() {
        if (upPressed && player2Y > 0) {
        	player2Y -= playerSpeed;
        }
        if (downPressed && player2Y < frame.getHeight() - playerSizeY) {
            player2Y += playerSpeed;
        }
        if (wPressed && player1Y > 0) {
        	player1Y -= playerSpeed;
        }
        if (sPressed && player1Y < frame.getHeight() - playerSizeY) {
        	player1Y += playerSpeed;
        }
    }
    public Game2() {
    	
        frame = new JFrame("Game2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setResizable(false);
        playerSizeX = 10 ;
        playerSizeY=50;
        ballSize = 20;
        playerSpeed = 12;
        initialBallSpeedX = 0;
        initialBallSpeedY = 0;
        ballSpeedMultiplier = 1.01;

        player1X = 50;
        player1Y = frame.getHeight() / 2 - playerSizeY / 2;

        player2X = frame.getWidth() - playerSizeX - 50;
        player2Y = frame.getHeight() / 2 - (playerSizeY-50);

        ballX = frame.getWidth() / 2 - ballSize / 2;
        ballY = frame.getHeight() / 2 - ballSize / 2;

        gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (e.getKeyChar() == ' '&&start) {
                	start=false;
                	initialBallSpeedX = 4;
                    initialBallSpeedY = 2;
                }
                if (keyCode == KeyEvent.VK_W) {
                    setWPressed(true);
                } else if (keyCode == KeyEvent.VK_S) {
                    setSPressed(true);
                } else if (keyCode == KeyEvent.VK_UP) {
                    setUpPressed(true);
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    setDownPressed(true);
                }
            }
            

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
            	if (keyCode == KeyEvent.VK_W) {
                    setWPressed(false);
                } else if (keyCode == KeyEvent.VK_S) {
                    setSPressed(false);
                } else if (keyCode == KeyEvent.VK_UP) {
                    setUpPressed(false);
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    setDownPressed(false);
                }
            }
        });

        Timer timer = new Timer(20, e -> {
        	move();
            ballX += initialBallSpeedX;
            ballY += initialBallSpeedY;
            // 플레이어와 공의 충돌 검사
            if (ballX < player1X + playerSizeX && ballY > player1Y && ballY+ballSize < player1Y + playerSizeY) {
                initialBallSpeedX = (int) (initialBallSpeedX * ballSpeedMultiplier);
                initialBallSpeedX = -initialBallSpeedX;
                initialBallSpeedX++;
                initialBallSpeedY+=0.5f;
            } else if (ballX + ballSize > player2X && ballY > player2Y && ballY+ballSize < player2Y + playerSizeY) {
                initialBallSpeedX = (int) (initialBallSpeedX * ballSpeedMultiplier);
                initialBallSpeedX = -initialBallSpeedX;
            }

            // 공이 화면 경계에 부딪혔을 때 처리
            if (ballX <= 0) {
            	// p1 true
                frame.setVisible(false);
            }
            else if (ballX + ballSize+ballSize >= frame.getWidth()) {
            	// p2 true
            	frame.setVisible(false);
            }
            
            if (ballY <= 0 || ballY + ballSize+ballSize+ballSize >= frame.getHeight()) {
                initialBallSpeedY = -initialBallSpeedY;
            }

            gamePanel.repaint();
        });
        timer.start();

        frame.setVisible(true);
    }
    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }
    public void setWPressed(boolean wPressed) {
        this.wPressed = wPressed;
    }
    public void setSPressed(boolean sPressed) {
        this.sPressed = sPressed;
    }
    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }
    class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.RED);
            g.fillRect(player1X, player1Y, playerSizeX, playerSizeY);

            g.setColor(Color.BLUE);
            g.fillRect(player2X, player2Y, playerSizeX, playerSizeY);

            g.setColor(Color.YELLOW);
            g.fillOval(ballX, ballY, ballSize, ballSize);
            if(start) {
            	String text = "준비완료 : spacebar클릭"; // 출력할 글자
                int x = 100; // X 좌표
                int y = 50; // Y 좌표

                // 글자 그리기
                g.setColor(Color.BLACK); // 글자 색상 설정
                g.setFont(new Font("맑음 고딕", Font.BOLD, 30)); // 글자 폰트 설정
                g.drawString(text, x, y);
            }
            //p1 p2 true에 따라 각자 승리 글자 출력
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Game2());
    }
}
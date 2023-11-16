import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
public class Pingpong_Game {
    private JFrame frame;
    private GamePanel gamePanel;
    private int player1X, player1Y, player2X, player2Y, ballX, ballY;
    private int playerSizeX,playerSizeY, ballSize, playerSpeed ;
    private double ballSpeedMultiplier;
    private double initialBallSpeedX, initialBallSpeedY;
    private boolean start=true;
    private boolean upPressed, downPressed,wPressed,sPressed;
    private BufferedImage player1Image;
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
    public Pingpong_Game(JFrame j) {
    	
        frame = new JFrame("Game2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 800);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        playerSizeX = 1 ;
        playerSizeY=100;
        ballSize = 40;
        playerSpeed = 12;
        initialBallSpeedX = 0;
        initialBallSpeedY = 0;
        ballSpeedMultiplier = 1.01;

        player1X = 100;
        player1Y = frame.getHeight() / 2 - playerSizeY / 2;

        player2X = frame.getWidth() - playerSizeX - 100;
        player2Y = frame.getHeight() / 2 - (playerSizeY-100);

        ballX = frame.getWidth() / 2 - ballSize / 2;
        ballY = frame.getHeight() / 2 - ballSize / 2;

        gamePanel = new GamePanel();
        frame.add(gamePanel);
        try {
            player1Image = ImageIO.read(getClass().getResource("blackhole.jpg"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (e.getKeyChar() == ' '&&start) {
                	start=false;
                	initialBallSpeedX = 6;
                    initialBallSpeedY = 3;
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
                initialBallSpeedX+=2;
                initialBallSpeedY++;
            } else if (ballX + ballSize > player2X && ballY > player2Y && ballY+ballSize < player2Y + playerSizeY) {
                initialBallSpeedX = (int) (initialBallSpeedX * ballSpeedMultiplier);
                initialBallSpeedX = -initialBallSpeedX;
            }

            // 공이 화면 경계에 부딪혔을 때 처리
            if (ballX <= 0) {
            	if(!start) {
            		JOptionPane.showMessageDialog(null, "Player 1가 이겼습니다!");
            		start=true;
                    frame.setVisible(false);
                	j.setVisible(true);
            	}


            }
            else if (ballX + ballSize+ballSize >= frame.getWidth()) {
            	if(!start) {
            		JOptionPane.showMessageDialog(null, "Player 2가 이겼습니다!");
            		start=true;
                    frame.setVisible(false);
                	j.setVisible(true);
            	}
                

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
            g.drawImage(player1Image, 0, 0,1300, 800, this);
            g.drawImage(player1Image, player1X, player1Y,playerSizeX, playerSizeY, this);

            g.drawImage(player1Image, player2X, player2Y,playerSizeX, playerSizeY, this);

            g.drawImage(player1Image, ballX, ballY, ballSize, ballSize, this);
            

            if(start) {
            	String text = "준비완료 : spacebar클릭"; // 출력할 글자
                int x = 440; // X 좌표
                int y = 300; // Y 좌표

                // 글자 그리기
                g.drawImage(player1Image, x, y-40,100, 40, this);
                g.setColor(Color.BLACK); // 글자 색상 설정
                g.setFont(new Font("맑음 고딕", Font.BOLD, 40)); // 글자 폰트 설정
                g.drawString(text, x, y);
            }

        }
    }
}
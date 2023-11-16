import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.net.URL;

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}

public class Rapid_Fire_Game extends JFrame {
    private static final int WIDTH = 1300;
    private static final int HEIGHT = 800;
    private static final int CAR_WIDTH = 100;
    private static final int CAR_HEIGHT = 60;
    private JLabel ready;
    private JLabel car1, car2;
    private int player1Position, player2Position;
    private boolean start = true;
    private boolean p1Pressed = false;
    private boolean p2Pressed = false;

    public Rapid_Fire_Game() {
        setTitle("Game3");
        setSize(WIDTH, HEIGHT);
        setResizable(false);

        // Image 객체를 이용하여 배경 이미지 로드
        Image backgroundImage = loadImage("blackhole.jpg");

        // BackgroundPanel 생성 및 배경 이미지 설정
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);
        backgroundPanel.setLayout(null);

        ready = new JLabel("준비완료 : spacebar클릭");
        ready.setFont(new Font("맑음 고딕", Font.BOLD, 30));
        ready.setLocation(80, 80);
        ready.setSize(400, 30);
        backgroundPanel.add(ready);
        ready.setHorizontalAlignment(SwingConstants.CENTER);

        // Image 객체를 이용하여 차 이미지 로드
        Image carImage1 = loadImage("blackhole.jpg");
        Image carImage2 = loadImage("blackhole.jpg");

        car1 = new JLabel(new ImageIcon(carImage1.getScaledInstance(CAR_WIDTH, CAR_HEIGHT, Image.SCALE_DEFAULT)));
        car1.setBounds(10, 200, CAR_WIDTH, CAR_HEIGHT);

        car2 = new JLabel(new ImageIcon(carImage2.getScaledInstance(CAR_WIDTH, CAR_HEIGHT, Image.SCALE_DEFAULT)));
        car2.setBounds(10, 400, CAR_WIDTH, CAR_HEIGHT);

        backgroundPanel.add(car1);
        backgroundPanel.add(car2);

        player1Position = 10;
        player2Position = 10;

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == ' ' && start) {
                    start = false;
                    ready.setText("");
                }
                if (e.getKeyChar() == 'z' && (!start) && !p1Pressed) {
                    player1Position += (int) (Math.random() * 10) + 1;
                    car1.setBounds(player1Position, 50, CAR_WIDTH, CAR_HEIGHT);
                    p1Pressed = true;
                } else if (e.getKeyChar() == '/' && (!start) && !p2Pressed) {
                    player2Position += (int) (Math.random() * 10) + 1;
                    car2.setBounds(player2Position, 150, CAR_WIDTH, CAR_HEIGHT);
                    p2Pressed = true;
                }

                if (player1Position >= WIDTH - CAR_WIDTH) {
                    JOptionPane.showMessageDialog(null, "Player 1이 이겼습니다!");
                    System.exit(0);
                } else if (player2Position >= WIDTH - CAR_WIDTH) {
                    JOptionPane.showMessageDialog(null, "Player 2가 이겼습니다!");
                    System.exit(0);
                }
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == 'z') {
                    p1Pressed = false;
                } else if (e.getKeyChar() == '/') {
                    p2Pressed = false;
                }
            }
        });

        backgroundPanel.setFocusable(true);
        add(backgroundPanel);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private Image loadImage(String path) {
        URL imgUrl = getClass().getResource(path);
        if (imgUrl != null) {
            return Toolkit.getDefaultToolkit().getImage(imgUrl);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public static void main(String[] args) {
        new Rapid_Fire_Game();
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.awt.event.KeyAdapter;
import java.net.URL;
import javax.sound.sampled.*;
import java.io.File;

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
    private Clip bgm;
    private Clip car;
    private static final int WIDTH = 1300;
    private static final int HEIGHT = 800;
    private static final int CAR_WIDTH = 100;
    private static final int CAR_HEIGHT = 60;
    private int count1=0;
    private int count2=0;
    private JLabel ready;
    private JLabel car1, car2;
    private int player1Position, player2Position;
    private boolean start = true;
    private boolean p1Pressed = false;
    private boolean p2Pressed = false;
    private int a1,a2;

    public Rapid_Fire_Game(JFrame j,BoardGame b) {
        setTitle("Game3");
        setSize(WIDTH, HEIGHT);
        setResizable(false);

        // Image 객체를 이용하여 배경 이미지 로드
        Image backgroundImage = loadImage("rapid_image.jpg");

        // BackgroundPanel 생성 및 배경 이미지 설정
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);
        backgroundPanel.setLayout(null);

        ready = new JLabel("준비완료 : spacebar클릭");
        ready.setFont(new Font("맑음 고딕", Font.BOLD, 30));
        ready.setLocation(80, 80);
        ready.setSize(400, 30);
        ready.setHorizontalAlignment(SwingConstants.CENTER);

        // Image 객체를 이용하여 차 이미지 로드
        Image carImage1 = loadImage("car1.png");
        Image carImage2 = loadImage("car2.png");

        car1 = new JLabel(new ImageIcon(carImage1.getScaledInstance(CAR_WIDTH, CAR_HEIGHT, Image.SCALE_DEFAULT)));
        car1.setBounds(10, 480, CAR_WIDTH, CAR_HEIGHT);

        car2 = new JLabel(new ImageIcon(carImage2.getScaledInstance(CAR_WIDTH, CAR_HEIGHT, Image.SCALE_DEFAULT)));
        car2.setBounds(10, 600, CAR_WIDTH, CAR_HEIGHT);

        backgroundPanel.add(car1);
        backgroundPanel.add(car2);
        backgroundPanel.add(ready);

        player1Position = 10;
        player2Position = 10;

        // KeyBindings를 이용하여 스페이스바, 'z' 키, '/' 키 이벤트 처리
        Action spaceAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (start) {
                   playBgm("rapid_bgm.wav");
                    start = false;
                    ready.setText("");
                }
            }
        };

        Action zPressedAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!start && !p1Pressed) {
                    a1 = (int) (Math.random() * 15) + 1;
                    player1Position+=a1;
                    car1.setBounds(player1Position, 480, CAR_WIDTH, CAR_HEIGHT);
                    count1++;
                    p1Pressed = true;
                    if(count1>5&&a1>12) {
                       count1=0;
                       carBgm("car.wav");
                    }
                    if (player1Position >= WIDTH - CAR_WIDTH) {
                        JOptionPane.showMessageDialog(null, "Player 1이 이겼습니다!");
                        b.P1_win();
                        j.setVisible(true);
                        dispose();
                    } 
                }
                
            }
        };

        Action zReleasedAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p1Pressed = false;
            }
        };

        Action slashPressedAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!start && !p2Pressed) {
                    a2= (int) (Math.random() * 15) + 1;
                    player2Position +=a2;
                    car2.setBounds(player2Position, 600, CAR_WIDTH, CAR_HEIGHT);
                    p2Pressed = true;
                    count2++;
                    if(count2>5&&a2>12) {
                       carBgm("car.wav");
                       count2=0;
                       
                    }
                    if (player2Position >= WIDTH - CAR_WIDTH) {
                        JOptionPane.showMessageDialog(null, "Player 2가 이겼습니다!");
                        b.P2_win();
                        j.setVisible(true);
                        dispose();
                    }
                }
                
            }
        };

        Action slashReleasedAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p2Pressed = false;
            }
        };

        backgroundPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "SpacePressed");
        backgroundPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "ZPressed");
        backgroundPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0, true), "ZReleased");
        backgroundPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, 0), "SlashPressed");
        backgroundPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, 0, true), "SlashReleased");

        backgroundPanel.getActionMap().put("SpacePressed", spaceAction);
        backgroundPanel.getActionMap().put("ZPressed", zPressedAction);
        backgroundPanel.getActionMap().put("ZReleased", zReleasedAction);
        backgroundPanel.getActionMap().put("SlashPressed", slashPressedAction);
        backgroundPanel.getActionMap().put("SlashReleased", slashReleasedAction);

        add(backgroundPanel);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                 stopBgm(); 
            }
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                stopBgm();
            }
        });
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
    public void playBgm(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            bgm = AudioSystem.getClip();
            bgm.open(audioInputStream);
            bgm.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    public void stopBgm() {
        if (bgm != null) {
            if (bgm.isRunning()) {
                bgm.stop();
            }
            bgm.close();
        }
    }
    public void carBgm(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            car = AudioSystem.getClip();
            car.open(audioInputStream);
            car.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyAdapter;

public class Game3 extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int CAR_WIDTH = 50;
    private static final int CAR_HEIGHT = 30;
    private JLabel ready;
    private JLabel car1, car2;
    private int player1Position, player2Position;
    private boolean start =true;
    public Game3() {
        setTitle("Game3");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        ready=new JLabel("준비완료 : spacebar클릭");
        ready.setFont(new Font("맑음 고딕", Font.BOLD, 30));
        ready.setLocation(80,80);
        ready.setSize(400,30);
        Container c=getContentPane();
        c.setLayout(null);
        c.add(ready);
        ready.setHorizontalAlignment(SwingConstants.CENTER);
        car1 = new JLabel("Player 1");
        car1.setBounds(10, 50, CAR_WIDTH, CAR_HEIGHT);
        car1.setOpaque(true);
        car1.setBackground(java.awt.Color.RED);
        car2 = new JLabel("Player 2");
        car2.setBounds(10, 150, CAR_WIDTH, CAR_HEIGHT);
        car2.setOpaque(true);
        car2.setBackground(java.awt.Color.BLUE);
        add(car1);
        add(car2);
        player1Position = 10;
        player2Position = 10;
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	if (e.getKeyChar() == ' '&&start) {
                	start=false;
                    ready.setText("");
                }
                if (e.getKeyChar() == 'z'&&(!start)) {
                    player1Position += (int) (Math.random() * 10) + 1;
                    car1.setBounds(player1Position, 50, CAR_WIDTH, CAR_HEIGHT);
                } else if (e.getKeyChar() == '/'&&(!start)) {
                    player2Position += (int) (Math.random() * 10) + 1;
                    car2.setBounds(player2Position, 150, CAR_WIDTH, CAR_HEIGHT);
                }

                if (player1Position >= WIDTH - CAR_WIDTH) {
                    JOptionPane.showMessageDialog(null, "Player 1이 이겼습니다!");
                    System.exit(0);
                } else if (player2Position >= WIDTH - CAR_WIDTH) {
                    JOptionPane.showMessageDialog(null, "Player 2가 이겼습니다!");
                    System.exit(0);
                }
            }
        });
        setFocusable(true);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Game3();
            }
        });
    }
}
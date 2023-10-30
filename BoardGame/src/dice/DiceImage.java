package dice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class DiceImage {
    private JButton rollDiceButton;
    private JLabel diceLabel;
    private Timer timer;
    private int rollCount;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DiceImage window = new DiceImage();
                window.createAndShowGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setSize(400, 225);  // 16:9 aspect ratio

        // Initial image, replace with your image's absolute path
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/dice1.png"));
        diceLabel = new JLabel(icon);
        diceLabel.setPreferredSize(new Dimension(100, 100));  // Set this based on your actual image size
        rollDiceButton = new JButton("Roll Dice");
        rollDiceButton.addActionListener(new RollDiceListener());

        frame.add(diceLabel);
        frame.add(rollDiceButton);

        frame.setVisible(true);
    }

    private class RollDiceListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            rollCount = 0;
            timer = new Timer(100, new ActionListener() {
                Random random = new Random();

                @Override
                public void actionPerformed(ActionEvent e) {
                    int randomRoll = random.nextInt(4) + 1;  // For a 4-sided dice
                 // 상대 경로를 사용하고 ClassLoader로 이미지를 로드
                    String imagePath = String.format("images/dice%d.png", randomRoll);
                    ImageIcon newIcon = new ImageIcon(getClass().getClassLoader().getResource(imagePath));
                    diceLabel.setIcon(newIcon);
                    rollCount++;

                    if (rollCount >= 10) {
                        timer.stop();
                        // Do something with the final dice roll, e.g., move player
                    }
                }
            });
            timer.start();
        }
    }
}


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class Mathgame {
	private static int timeLeft = 60; // 60 seconds
    private static int score1 = 0;
    private static int score2 = 0;
    
    public static void updateTimer(JLabel timerLabel) {
        if (timeLeft > 0) {
            timeLeft--;
            timerLabel.setText("Time left: " + timeLeft);
        } else {
        	timerLabel.setText("끝!");
        }
    }

    // 스코어 업데이트 (수정예정)
    public static void updateScore(JLabel scoreLabel, int player) {
        if (player == 1) {
            score1++;
        } else if (player == 2) {
            score2++;
        }
        scoreLabel.setText("Score: " + score1 + " | " + score2);
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Calculator Game");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 2));

        // Player 1 Panel
        JPanel panel1 = new JPanel();
        JTextField questionField1 = new JTextField(10); // 문제 패널
        JTextField answerField1 = new JTextField(10); // 답변 패널
        JButton button1 = new JButton("Calculate"); 
        panel1.add(questionField1);
        panel1.add(answerField1);
        panel1.add(button1);
        
        String initialProblem1 = RandomLogic.generateProblem();
        questionField1.setText(initialProblem1);
        questionField1.setEditable(false);

        // Player 2 Panel
        JPanel panel2 = new JPanel();
        JTextField questionField2 = new JTextField(10);
        JTextField answerField2 = new JTextField(10);
        JButton button2 = new JButton("Calculate");
        panel2.add(questionField2);
        panel2.add(answerField2);
        panel2.add(button2);
        
        // 문제 초기화
        String initialProblem2 = RandomLogic.generateProblem();
        questionField2.setText(initialProblem2);
        questionField2.setEditable(false);

        // Add panels to frame
        frame.add(panel1);
        frame.add(panel2);
        
        JPanel panelBottom = new JPanel();
        panelBottom.setLayout(new BoxLayout(panelBottom, BoxLayout.Y_AXIS));
        
     // Add Timer and Score labels
        JLabel timerLabel = new JLabel("Time left: 60");
        JLabel scoreLabel = new JLabel("Score: 0 | 0");
        panel1.add(timerLabel);
        panel2.add(scoreLabel);
        
        //작동 제대로 안함
        //frame.add(panelBottom, BorderLayout.SOUTH);

        // Initialize Timer
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimer(timerLabel);
            }
        });
        timer.start();

     // Action listeners
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Generate a random arithmetic problem
                String problem = RandomLogic.generateProblem();
                questionField1.setText(problem);
                questionField1.setCaretPosition(problem.length());
                updateScore(scoreLabel, 1);
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Generate a random arithmetic problem
                String problem = RandomLogic.generateProblem();
                questionField2.setText(problem);
                questionField2.setCaretPosition(problem.length());
                updateScore(scoreLabel, 2);
            }
        });

        frame.setVisible(true);
        
     // Set focus to the answer field
        answerField2.requestFocusInWindow();
    }
}

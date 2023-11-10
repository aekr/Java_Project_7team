
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class Mathgame {
	private static int timeLeft = 30; // 60 seconds
    private static int score1 = 0;
    
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
    	score1++;
        scoreLabel.setText("Score: " + score1);
    }
    
    private static Timer answerCheckTimer; // 답변을 체크하는 타이머
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Calculator Game");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(2, 1));

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
        
        
        
        // 문제 초기화
        String initialProblem = RandomLogic.generateProblem();
        questionField1.setText(initialProblem);
        questionField1.setEditable(false);

        // Add panels to frame
        frame.add(panel1);
        
        JPanel panelBottom = new JPanel();
        
     // Add Timer and Score labels
        JLabel timerLabel = new JLabel("Time left: 30");
        JLabel scoreLabel = new JLabel("Score: 0");
        panelBottom.add(timerLabel);
        
     // 스코어 라벨 추가
        panelBottom.add(scoreLabel);
        
        //작동 제대로 안함
        frame.add(panelBottom);

        // Initialize Timer
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimer(timerLabel);
            }
        });
        timer.start();
        
     // 답변을 체크하는 타이머 초기화
        answerCheckTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userAnswer = answerField1.getText(); // 사용자의 답을 가져옵니다.
                if (!userAnswer.isEmpty()) {
                    String correctAnswer = RandomLogic.calculateAnswer(questionField1.getText()); // 올바른 답을 계산합니다.
                    if (userAnswer.equals(correctAnswer)) { // 사용자의 답과 올바른 답을 비교합니다.
                        updateScore(scoreLabel, 1); // 점수를 업데이트합니다.
                        String problem = RandomLogic.generateProblem();
                        questionField1.setText(problem);
                        questionField1.setCaretPosition(problem.length());
                        answerField1.setText(""); // 답변 필드 초기화
                        answerField1.requestFocusInWindow(); // 커서를 다시 답변 필드로 설정합니다.
                    }
                }
            }
        });
        answerCheckTimer.start();

     // Action listeners 수정
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userAnswer = answerField1.getText(); // 사용자의 답을 가져옵니다.
                String correctAnswer = RandomLogic.calculateAnswer(questionField1.getText()); // 올바른 답을 계산합니다.
                if (userAnswer.equals(correctAnswer)) { // 사용자의 답과 올바른 답을 비교합니다.
                    updateScore(scoreLabel, 1); // 점수를 업데이트합니다.
                }
                // 새 문제를 생성하고 UI를 업데이트합니다.
                String problem = RandomLogic.generateProblem();
                questionField1.setText(problem);
                questionField1.setCaretPosition(problem.length());
                answerField1.setText(""); // 답변 필드 초기화
            }
        });

        frame.setVisible(true);
        
     // Set focus to the answer field
        answerField1.requestFocusInWindow();
    }
}

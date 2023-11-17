
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class Mathgame {
    private static int score1 = 0;
    private static TimerClass timer;
    private static JFrame frame;
    private static JPanel questionAnswerPanel;
    private static PlayerManager playerManager;
    
    private void createAndShowGUI() {
    	frame = new JFrame("Calculator Game");
        frame.setSize(400, 300); // 크기 조정
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout()); // BorderLayout 사용

        // 프로그레스 바와 타이머 레이블을 위한 상단 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        JProgressBar progressBar = new JProgressBar();
        JLabel timeLabel = new JLabel("Time left: 5"); // 타이머 레이블 추가
        topPanel.add(progressBar, BorderLayout.CENTER);
        topPanel.add(timeLabel, BorderLayout.EAST); // 타이머 레이블을 오른쪽에 배치
        frame.add(topPanel, BorderLayout.NORTH);

        // 문제와 답변을 위한 중앙 패널
        JPanel centerPanel = new JPanel();
        JTextField questionField = new JTextField(10); // 문제 필드
        JTextField answerField = new JTextField(10); // 답변 필드
        JButton button = new JButton("Calculate");
        centerPanel.add(questionField);
        centerPanel.add(answerField);
        centerPanel.add(button);
        frame.add(centerPanel, BorderLayout.CENTER);
        
        String initialProblem = RandomLogic.generateProblem();
        questionField.setText(initialProblem);
        questionField.setEditable(false);

        // 스코어 레이블을 위한 하단 패널
        JPanel bottomPanel = new JPanel();
        JLabel scoreLabel = new JLabel("Score: 0"); // 점수 레이블 추가
        bottomPanel.add(scoreLabel);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        
     // 답변을 체크하는 타이머 초기화
        answerCheckTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userAnswer = answerField.getText(); // 사용자의 답을 가져옵니다.
                if (!userAnswer.isEmpty()) {
                    String correctAnswer = RandomLogic.calculateAnswer(questionField.getText()); // 올바른 답을 계산합니다.
                    if (userAnswer.equals(correctAnswer)) { // 사용자의 답과 올바른 답을 비교합니다.
                        updateScore(scoreLabel, 1); // 점수를 업데이트합니다.
                        String problem = RandomLogic.generateProblem();
                        questionField.setText(problem);
                        questionField.setCaretPosition(problem.length());
                        answerField.setText(""); // 답변 필드 초기화
                        answerField.requestFocusInWindow(); // 커서를 다시 답변 필드로 설정합니다.
                    }
                }
            }
        });
        answerCheckTimer.start();

     // Action listeners 수정
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userAnswer = answerField.getText(); // 사용자의 답을 가져옵니다.
                String correctAnswer = RandomLogic.calculateAnswer(questionField.getText()); // 올바른 답을 계산합니다.
                if (userAnswer.equals(correctAnswer)) { // 사용자의 답과 올바른 답을 비교합니다.
                    updateScore(scoreLabel, 1); // 점수를 업데이트합니다.
                }
                // 새 문제를 생성하고 UI를 업데이트합니다.
                String problem = RandomLogic.generateProblem();
                questionField.setText(problem);
                questionField.setCaretPosition(problem.length());
                answerField.setText(""); // 답변 필드 초기화
            }
        });

        frame.setVisible(true);
        
     // Set focus to the answer field
//        answerField.requestFocusInWindow();
        
        
		/*
		 * progressBar.setBounds(10, 10, 200, 20); // 위치와 크기 설정 frame.add(progressBar);
		 * // 프레임에 프로그레스 바 추가
		 */
        // TimerClass 인스턴스 생성 및 시작
        timer = new TimerClass(timeLabel, progressBar, playerManager);
        PlayerManager playerManager = new PlayerManager(this, 2, timer);
        timer.start();
    }

    // 스코어 업데이트
    public static void updateScore(JLabel scoreLabel, int player) {
    	score1++;
        scoreLabel.setText("Score: " + score1);
        timer.correctAnswer();
        timer.startNewQuestion();
    }
    
    private static Timer answerCheckTimer; // 답변을 체크하는 타이머
    
    public static void main(String[] args) {
    	Mathgame game = new Mathgame();
        game.createAndShowGUI();
    	
		/*
		 * JFrame frame = new JFrame("Calculator Game"); frame.setSize(400, 200);
		 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); frame.setLayout(new
		 * GridLayout(2, 1));
		 * 
		 * // Player 1 Panel JPanel panel1 = new JPanel(); JTextField questionField1 =
		 * new JTextField(10); // 문제 패널 JTextField answerField1 = new JTextField(10); //
		 * 답변 패널 JButton button1 = new JButton("Calculate"); panel1.add(questionField1);
		 * panel1.add(answerField1); panel1.add(button1);
		 * 
		 * String initialProblem1 = RandomLogic.generateProblem();
		 * questionField1.setText(initialProblem1); questionField1.setEditable(false);
		 */
    }
    
 // 플레이어가 게임을 마칠 때 호출되는 메소드
    public static void endPlayerTurn() {
        // 현재 플레이어 점수 업데이트
        playerManager.setPlayerScore(playerManager.getCurrentPlayer(), score1);
    }
}

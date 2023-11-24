
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Mathgame {
	private static int score1 = 0; // 플레이어 1의 점수
	private static int score2 = 0; // 플레이어 2의 점수
	private static TimerClass timer;
	private static JFrame frame;
	private static JPanel questionAnswerPanel;
	private static PlayerManager playerManager;
	private static JLabel startGameMessage;
	private static JPanel centerPanel;
	private static JLabel preStartMessage;
	private static JLabel scoreLabel;

	public void createAndShowGUI() {
		
		
	    SoundManager soundManager = new SoundManager();
	    
		
		frame = new JFrame("Calculator Game");
		frame.setSize(1080, 607); // 크기 조정
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout()); // BorderLayout 사용

		// 게임 시작 전 메시지 레이블
		preStartMessage = new JLabel("스페이스바를 눌러 게임을 시작!", SwingConstants.CENTER);
		preStartMessage.setBounds(280, 200, 500, 50);
		preStartMessage.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
	    frame.add(preStartMessage);
	    preStartMessage.setVisible(true);

		// 게임 시작 메시지 레이블 초기화
		startGameMessage = new JLabel("게임 시작!", SwingConstants.CENTER);
		startGameMessage.setFont(new Font("Malgun Gothic", Font.BOLD, 24));
		// startGameMessage.setForeground(Color.GREEN);
		startGameMessage.setBounds(430, 200, 200, 50); // 적절한 위치와 크기 설정
		frame.add(startGameMessage);
		startGameMessage.setVisible(false); // 초기에는 보이지 않도록 설정

		// 프로그레스 바와 타이머 레이블을 위한 상단 패널
		JPanel topPanel = new JPanel(new BorderLayout());
		JProgressBar progressBar = new JProgressBar();
		progressBar.setForeground(Color.GREEN);
		progressBar.setMaximum((int)(5000));
		progressBar.setValue((int) (5000));
		JLabel timeLabel = new JLabel("남은 시간: 5"); // 타이머 레이블 추가
		timeLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
		topPanel.add(progressBar, BorderLayout.CENTER);
		topPanel.add(timeLabel, BorderLayout.EAST); // 타이머 레이블을 오른쪽에 배치
		frame.add(topPanel, BorderLayout.NORTH);

		// 문제와 답변을 위한 중앙 패널
		centerPanel = new JPanel();
		JTextField questionField = new JTextField(10); // 문제 필드
		questionField.setFont(new Font("Malgun Gothic", Font.BOLD, 24));
		JTextField answerField = new JTextField(10); // 답변 필드
		answerField.setFont(new Font("Malgun Gothic", Font.BOLD, 24));
		JButton button = new JButton("Calculate");
		button.setFont(new Font("Malgun Gothic", Font.BOLD, 24));
		button.setBackground(Color.WHITE);
		centerPanel.add(questionField);
		centerPanel.add(answerField);
		centerPanel.add(button);
		frame.add(centerPanel, BorderLayout.CENTER);

		String initialProblem = RandomLogic.generateProblem();
		questionField.setText(initialProblem);
		questionField.setEditable(false);
		
	    // 초기에는 중앙 패널과 관련 컴포넌트를 숨깁니다.
	    centerPanel.setVisible(false);

		// 스코어 레이블을 위한 하단 패널
		JPanel bottomPanel = new JPanel();
		JLabel scoreLabel = new JLabel("Score: 0"); // 점수 레이블 추가
		scoreLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
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
						updateScore(scoreLabel); // 점수를 업데이트합니다.
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
					soundManager.loadEffect("bgm2.wav"); // 실제 파일 경로로 대체
			        soundManager.playEffect();
					updateScore(scoreLabel); // 점수를 업데이트합니다.
				}
				// 새 문제를 생성하고 UI를 업데이트합니다.
				String problem = RandomLogic.generateProblem();
				questionField.setText(problem);
				questionField.setCaretPosition(problem.length());
				answerField.setText(""); // 답변 필드 초기화
			}
		});

		frame.setFocusable(true);
		frame.requestFocusInWindow();
		frame.setVisible(true);
		
		frame.setLocationRelativeTo(null);

		/*
		 * progressBar.setBounds(10, 10, 200, 20); // 위치와 크기 설정 frame.add(progressBar);
		 * // 프레임에 프로그레스 바 추가
		 */

		// 먼저 PlayerManager 객체를 생성합니다.
		playerManager = new PlayerManager(this, 2, questionField, answerField);

		// 이제 TimerClass 객체를 생성하고, 여기에 playerManager를 전달합니다.
		timer = new TimerClass(timeLabel, progressBar, playerManager);
		playerManager.setFrame(frame);

		// TimerClass에 PlayerManager를 전달한 후, TimerClass에 대한 참조를 PlayerManager에 설정합니다.
		playerManager.setTimer(timer);

		// 키 리스너 추가
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					preStartMessage.setVisible(false);
					startGame();
				}
			}
		});
		
		// 배경음악 재생
	    soundManager.loadBGM("bgm1.wav"); // 실제 파일 경로로 대체
	    soundManager.playBGM();

	}

	// 스코어 업데이트
	public static void updateScore(JLabel scoreLabel) {
		if (playerManager.getCurrentPlayer() == 0) {
			score1++;
			scoreLabel.setText("Score: " + score1);
		} else {
			score2++;
			scoreLabel.setText("Score: " + score2);
		}
		timer.correctAnswer();
		timer.startNewQuestion();
	}

	private static Timer answerCheckTimer; // 답변을 체크하는 타이머

	public void startGame() {
		// 중앙 패널과 관련 컴포넌트를 보이게 합니다.
	    centerPanel.setVisible(true);
		
		// "게임 시작!" 메시지 표시 및 숨기기 로직
		startGameMessage.setVisible(true);
		new Timer(1000, e -> startGameMessage.setVisible(false)).start();
		
		// 타이머 시작
		timer.start();

	}
	
	public void updateScoreLabel(int score) {
        scoreLabel.setText("Score: " + score);
    }

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
		if (playerManager.getCurrentPlayer() == 0) {
			playerManager.setPlayerScore(0, score1);
			score1 = 0;
			timer.resetTimerForNewPlayer();
		} else {
			playerManager.setPlayerScore(1, score2);
		}
	}
	
	
}


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Mathgame {
	private static int score1 = 0; // 플레이어 1의 점수
	private static int score2 = 0; // 플레이어 2의 점수
	private static TimerClass timer;
	private JFrame frame;
	private static JPanel questionAnswerPanel;
	private static PlayerManager playerManager;
	private static JLabel startGameMessage;
	private static JPanel centerPanel;
	private static JLabel preStartMessage;
	private static JLabel scoreLabel;
	private static JTextField answerField;
	private boolean movingToSecondPosition = true;
	private Timer timer1; // 이동하는 타이머
    private BackgroundPanel centerBackground2;
    private boolean isBackground2Visible = true;
    SoundManager soundManager;

	class BackgroundPanel extends JPanel {
		private Image backgroundImage;
		  private int x, y;

		public BackgroundPanel(String imagePath) {
			try {
				backgroundImage = ImageIO.read(getClass().getResource(imagePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
			setLayout(null);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (backgroundImage != null) {
				g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
			}
		}
	}

	public Mathgame(JFrame j) {
		this.frame = j;
		createAndShowGUI();
	}
	
	public void createAndShowGUI() {
		soundManager = new SoundManager();
		// 배경 이미지를 가진 새로운 패널 생성
//        BackgroundPanel centerBackground = new BackgroundPanel("Math_Image1.png"); // 배경 이미지 경로 설정
//        centerBackground.setLayout(new BorderLayout()); // 레이아웃 설정
        
//		 이미지 로드 및 배경 설정
//        BackgroundPanel centerBackground = new BackgroundPanel("Math_Image1.png"); // 배경 이미지 경로 설정
//        centerBackground.setOpaque(false);
//        centerBackground.setMinimumSize(new Dimension(200, 200));
//        centerBackground.setMaximumSize(new Dimension(200, 200));
//        centerBackground.setPreferredSize(new Dimension(200, 200));


		frame = new JFrame("Calculator Game");
		frame.setSize(1080, 607); // 크기 조정
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout()); // BorderLayout 사용

		// 게임 시작 전 메시지 레이블
		preStartMessage = new JLabel("스페이스바를 눌러 게임을 시작!", SwingConstants.CENTER);
		preStartMessage.setBounds(500, 200, 500, 50);
		preStartMessage.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
		frame.add(preStartMessage);
		preStartMessage.setVisible(true);

		// 게임 시작 메시지 레이블 초기화
		startGameMessage = new JLabel("게임 시작!", SwingConstants.CENTER);
		startGameMessage.setFont(new Font("Malgun Gothic", Font.BOLD, 24));
		// startGameMessage.setForeground(Color.GREEN);
		startGameMessage.setBounds(630, 200, 200, 50); // 적절한 위치와 크기 설정
		frame.add(startGameMessage);
		startGameMessage.setVisible(false); // 초기에는 보이지 않도록 설정

		// 프로그레스 바와 타이머 레이블을 위한 상단 패널
		JPanel topPanel = new JPanel(new BorderLayout());
		JProgressBar progressBar = new JProgressBar();
		progressBar.setForeground(Color.GREEN);
		progressBar.setMaximum((int) (5000));
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
		answerField = new JTextField(10); // 답변 필드
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

		
		
		// 배경 이미지를 가진 새로운 패널 생성 및 추가
	    BackgroundPanel centerBackground = new BackgroundPanel("Math_Image2.png"); // 배경 이미지 경로 설정
	    centerBackground.setBounds(100, 100, 400, 400); // 위치와 크기 설정
	    frame.add(centerBackground);
		
	    BackgroundPanel centerBackground2 = new BackgroundPanel("Math_Image3.png");
	    centerBackground2.setBounds(600, 300, 200, 200);
	    frame.add(centerBackground2);
	    
	    // 위치를 변경하는 메서드
	    ActionListener moveAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (movingToSecondPosition) {
                    centerBackground2.setBounds(600, 320, 200, 200);
                } else {
                    centerBackground2.setBounds(600, 300, 200, 200);
                }
                movingToSecondPosition = !movingToSecondPosition; // 이동 방향 전환
            }
        };
	    
     // 1초마다 위치를 변경하는 타이머
     // 타이머 시작
        timer1 = new Timer(1000, moveAction);
        timer1.setRepeats(true); // 반복 설정
        timer1.start();
	    
		 // 중앙 패널에 배경 패널을 추가하기 위한 GridBagLayout 설정
//        centerPanel = new JPanel(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.weightx = 1;
//        gbc.weighty = 1;
//        gbc.fill = GridBagConstraints.NONE; // 패널이 그리드 셀을 채우지 않도록 설정
//        centerPanel.add(centerBackground, gbc);

        //frame.add(centerPanel, BorderLayout.WEST);
		
        frame.add(centerPanel, BorderLayout.CENTER);
	    
		frame.setFocusable(true);
		frame.requestFocusInWindow();
		frame.setVisible(true);

		frame.setLocationRelativeTo(null);
		
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
								
								// centerBackground4 추가
								toggleBackground2Visibility(false);
					            BackgroundPanel centerBackground4 = new BackgroundPanel("Math_Image5.png");
					            centerBackground4.setBounds(600, 300, 200, 200);
					            frame.add(centerBackground4);

					            // 0.5초 후에 centerBackground2로 전환
					            new Timer(500, ev -> {
					                frame.remove(centerBackground4);
					                frame.repaint();
					            }).start();
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

		/*
		 * progressBar.setBounds(10, 10, 200, 20); // 위치와 크기 설정 frame.add(progressBar);
		 * // 프레임에 프로그레스 바 추가
		 */

		// 먼저 PlayerManager 객체를 생성합니다.
		playerManager = new PlayerManager(this, 2, questionField, answerField, soundManager);

		// 이제 TimerClass 객체를 생성하고, 여기에 playerManager를 전달합니다.
		timer = new TimerClass(timeLabel, progressBar, playerManager, this);
		playerManager.setFrame(frame);

		// TimerClass에 PlayerManager를 전달한 후, TimerClass에 대한 참조를 PlayerManager에 설정합니다.
		playerManager.setTimer(timer);

		// 키 리스너 추가
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					answerField.requestFocusInWindow();
					preStartMessage.setVisible(false);
					startGame();
				}
			}
		});
		
		frame.setVisible(true);

		// 이미지 로드
		// frame.setContentPane(backgroundPanel);

		// 메인 패널을 배경 패널에 추가
		//backgroundPanel.add(centerPanel, BorderLayout.CENTER);

		// 배경음악 재생
		soundManager.loadBGM("bgm1.wav"); // 실제 파일 경로로 대체
		soundManager.playBGM();

	}

	public void toggleBackground2Visibility(boolean isVisible) {
	    // centerBackground2와 timer1이 초기화되었는지 확인
	    if (centerBackground2 != null && timer1 != null) {
	        if (isVisible != isBackground2Visible) {
	            centerBackground2.setVisible(isVisible);
	            isBackground2Visible = isVisible;

	            if (isVisible) {
	                timer1.start(); // 타이머 재시작
	            } else {
	                timer1.stop(); // 타이머 일시중지
	            }
	        }
	    }
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
		
		answerField.requestFocusInWindow();

	}
	
	public JFrame getFrame() {
        return frame;
    }

	public void updateScoreLabel(int score) {
		scoreLabel.setText("Score: " + score);
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

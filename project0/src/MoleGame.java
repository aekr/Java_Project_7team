import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Random;

public class MoleGame {
	private JFrame frame;
	private JPanel gridPanel, topPanel;
	private JButton[][] buttons, buttons2;
	private JLabel scoreLabel, timerLabel;
	private Timer gameTimer, clockTimer;
	private int score, time;
	private int currentRow, currentCol;
	private int Gold_currentRow, Gold_currentCol, GoldCheck;
	private boolean isGameActive;
	private int currentPlayer;
	private int[] playerScores = new int[2];
	private static JLabel startGameMessage;
	private JPanel startMessagePanel;
	private static JLabel preStartMessage;
	private ImageIcon backgroundImage;
    private ImageIcon whitePanelImage;
    private ImageIcon bluePanelImage;
    private ImageIcon yellowPanelImage;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				MoleGame window = new MoleGame();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private Timer blueSquareTimer; // 클래스 필드로 Timer 추가

	public MoleGame() {
		initialize();
		preStartMessage = new JLabel("스페이스바를 눌러 게임을 시작!", SwingConstants.CENTER);
		preStartMessage.setBounds(100, 100, 200, 50);
		frame.add(preStartMessage, BorderLayout.SOUTH);
		preStartMessage.setVisible(true);
		gridPanel.setVisible(false);
		// 초기 메시지 표시
		// JOptionPane.showMessageDialog(frame, "스페이스바를 눌러 게임을 시작하세요.");
	}

	private void initialize() {
		SoundManager soundManager = new SoundManager();
		
		int imageWidth = 100;
		int imageHeight = 100;
		
		// 이미지 로드
        backgroundImage = new ImageIcon(getClass().getResource("Image1.png"));
        //whitePanelImage = new ImageIcon(getClass().getResource("Image2.png"));
        //bluePanelImage = new ImageIcon(getClass().getResource("Image3.png"));
        //yellowPanelImage = new ImageIcon(getClass().getResource("Image4.png"));
        
        // whitePanelImage
        ImageIcon originalWhitePanelIcon = new ImageIcon(getClass().getResource("Image2.png"));
        Image scaledImage2 = originalWhitePanelIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
        whitePanelImage = new ImageIcon(scaledImage2);
        
        // bluePanelImage
        ImageIcon originalbluePanelIcon = new ImageIcon(getClass().getResource("Image3.png"));
        Image scaledImage3 = originalbluePanelIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
        bluePanelImage = new ImageIcon(scaledImage3);
        
        // yellowPanelImage
        ImageIcon originalyellowPanelIcon = new ImageIcon(getClass().getResource("Image4.png"));
        Image scaledImage4 = originalyellowPanelIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
        yellowPanelImage = new ImageIcon(scaledImage4);
        
        // 배경 설정
        // frame.setContentPane(new BackgroundPanel(backgroundImage.getImage()));

		// 플레이어 스코어
		currentPlayer = 0;
		playerScores[0] = 0;
		playerScores[1] = 0;

		// 기본 UI 설정
		frame = new JFrame();
		frame.setBounds(100, 100, 720, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		// 상단 패널 설정 (점수와 타이머 표시)
		topPanel = new JPanel();
		scoreLabel = new JLabel("점수: 0");
		timerLabel = new JLabel("시간: 30");
		topPanel.add(scoreLabel);
		topPanel.add(timerLabel);
		frame.getContentPane().add(topPanel, BorderLayout.NORTH);

		// 그리드 패널 설정
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(4, 4));
		frame.getContentPane().add(gridPanel, BorderLayout.CENTER);

		// 버튼 설정
		buttons = new JButton[4][4];
		buttons2 = new JButton[4][4];
		score = 0;
		time = 30;
		isGameActive = true;

		// 게임 시작 메시지 패널 및 레이블 초기화
        startMessagePanel = new JPanel(new BorderLayout());
        startGameMessage = new JLabel("게임 시작!", SwingConstants.CENTER);
        // startGameMessage 설정...
        startGameMessage.setBounds(100, 100, 200, 50); // 적절한 위치와 크기 설정
        startMessagePanel.add(startGameMessage, BorderLayout.CENTER);
        frame.add(startMessagePanel, BorderLayout.SOUTH); // SOUTH에 배치
		// startGameMessage.setFont(new Font("Arial", Font.BOLD, 24));
		// startGameMessage.setForeground(Color.GREEN);
		
		startGameMessage.setVisible(false); // 초기에는 보이지 않도록 설정

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				JButton button = new JButton();
				button.setIcon(whitePanelImage);
				// button.setBackground(Color.WHITE);
				button.addActionListener(new ButtonClickListener(i, j));
				buttons[i][j] = button;
				buttons2[i][j] = button;
				gridPanel.add(button);
			}
		}

		// 게임 타이머 설정 (30초 후 게임 종료)
		gameTimer = new Timer(31000, e -> endGame());
		// 디버그
		// gameTimer = new Timer(5000, e -> endGame());
		

		// 시계 타이머 설정 (1초마다 시간 업데이트)
		clockTimer = new Timer(1000, e -> updateTimer());
		

		randomizeButton();
		moveBlueSquareAfterDelay();

		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					preStartMessage.setVisible(false);
					gameTimer.start();
					clockTimer.start();
					startGame();
				}
			}
		});

		frame.setFocusable(true);
		frame.requestFocusInWindow();
		
		// 배경음악 재생
	    soundManager.loadBGM("bgm1.wav"); // 실제 파일 경로로 대체
	    soundManager.playBGM();
	}

	// 랜덤 딜레이
	private int randomDelayBetween(int min, int max) {
		Random random = new Random();
		return min + random.nextInt(max - min + 1);
	}

	// 일정 시간 뒤 새로고침(난이도 조절 용)
	private void moveBlueSquareAfterDelay() {
		int delay = randomDelayBetween(1000, 2000);
		Timer delayTimer = new Timer(delay, e -> moveBlueSquare());
		delayTimer.setRepeats(true); // false로 바꾸면 반복 안함.
		delayTimer.start();
		moveGoldenSquare();
		moveBlueSquare();
	}

	public void moveBlueSquare() {
		if (!isGameActive) {
			return;
		}

		Random rand = new Random();
		int newRow, newCol;

		do {
			newRow = rand.nextInt(4);
			newCol = rand.nextInt(4);
		} while (newRow == Gold_currentRow && newCol == Gold_currentCol); // 황금 칸이 아닌 새 위치 찾기

		if (currentRow >= 0 && currentCol >= 0) {
			buttons[currentRow][currentCol].setIcon(whitePanelImage); //setBackground(Color.WHITE);
		}

		currentRow = newRow;
		currentCol = newCol;
		buttons[currentRow][currentCol].setIcon(bluePanelImage); //.setBackground(Color.BLUE);
	}

	// 황금 타일(5점 줌)
	private void moveGoldenSquare() {
		if (!isGameActive) {
			return;
		}

		Random rand = new Random();
		int newRow, newCol;

		do {
			newRow = rand.nextInt(4);
			newCol = rand.nextInt(4);
		} while (newRow == currentRow && newCol == currentCol); // 파란 칸이 아닌 새 위치 찾기

		if (Gold_currentRow >= 0 && Gold_currentCol >= 0) {
			buttons2[Gold_currentRow][Gold_currentCol].setIcon(whitePanelImage); //.setBackground(Color.WHITE);
		}

		GoldCheck = rand.nextInt(20);
		Gold_currentRow = newRow;
		Gold_currentCol = newCol;

		if (GoldCheck >= 17)
			buttons2[Gold_currentRow][Gold_currentCol].setIcon(yellowPanelImage); //.setBackground(Color.YELLOW);
	}

	// 점수 업데이트
	private void updateScoreDisplay() {
		scoreLabel.setText("점수: " + score);
	}

	// 버튼 랜덤
	private void randomizeButton() {
		if (!isGameActive) {
			return;
		}

		if (currentRow >= 0 && currentCol >= 0) {
			buttons[currentRow][currentCol].setIcon(whitePanelImage); //.setBackground(Color.WHITE);
		}

		Random rand = new Random();
		currentRow = rand.nextInt(4);
		currentCol = rand.nextInt(4);
		buttons[currentRow][currentCol].setIcon(bluePanelImage); //.setBackground(Color.BLUE);
	}

	// 초당 업데이트
	private void updateTimer() {
		if (isGameActive) {
			time--;
			timerLabel.setText("시간: " + time);
			// 난이도 조절 용
			// moveBlueSquareAfterDelay();
		}
	}

	// 플레이어 점수 비교
	private void compareScoresAndShowResult() {
		String resultMessage = "플레이어 1 점수: " + playerScores[0] + "\n" + "플레이어 2 점수: " + playerScores[1] + "\n"
				+ (playerScores[0] > playerScores[1] ? "플레이어 1 승리!"
						: playerScores[0] < playerScores[1] ? "플레이어 2 승리!" : "무승부!");
		JOptionPane.showMessageDialog(frame, resultMessage);
	}

	// 게임 시작(버그 수정용)
	private void startGame() {
		score = 0; // 점수 초기화
		time = 30; // 시간 초기화
		isGameActive = true; // 게임 활성화 상태로 설정
		currentPlayer = 0; // 첫 번째 플레이어로 설정

		// UI 요소 초기화
		scoreLabel.setText("점수: 0");
		timerLabel.setText("시간: 30");
		for (JButton[] buttonRow : buttons) {
			for (JButton button : buttonRow) {
				button.setEnabled(true);
				button.setIcon(whitePanelImage); //.setBackground(Color.WHITE);
			}
		}

		// 게임 타이머 및 시계 타이머 시작
		gameTimer.start();
		clockTimer.start();

		// 첫 번째 파란 칸 및 황금 칸 초기화
		randomizeButton();

		gridPanel.setVisible(true); // 게임 패널 보이기
        startGameMessage.setVisible(true);
        new Timer(1000, e -> {
            frame.remove(startMessagePanel); // startMessagePanel 제거
            frame.revalidate(); // 레이아웃 재검토
            frame.repaint(); // 프레임 다시 그리기
        }).start(); // 1초 후 메시지 패널 숨김
	}

	private void endGame() {
		isGameActive = false;
		// 현재 플레이어 점수 저장
		playerScores[currentPlayer] = score;

		// 다음 플레이어로 전환
		currentPlayer++;
		if (currentPlayer < 2) {
			// 두 번째 플레이어 게임 시작
			gameTimer.stop();
			clockTimer.stop();
			startGameForNextPlayer();
			
		} else {
			// 두 플레이어의 게임이 모두 끝난 경우, 점수 비교 및 결과 표시
			compareScoresAndShowResult();
			for (JButton[] buttonRow : buttons) {
				for (JButton button : buttonRow) {
					button.setEnabled(false);
				}

				gameTimer.stop();
				clockTimer.stop();

			}
		}
	}

	// 두번째 플레이어 용
	private void startGameForNextPlayer() {
		JOptionPane.showMessageDialog(frame, "플레이어 1 끝! 점수: " + score);
		// JOptionPane.showMessageDialog(frame, "스페이스바를 눌러 게임을 시작하세요.");
		score = 0; // 점수 초기화
		time = 30; // 시간 초기화
		currentPlayer = 1; // 두 번째 플레이어로 설정
		gridPanel.setVisible(false);
		
		// 게임 시작 메시지 재설정 및 가시화
        preStartMessage.setText("스페이스바를 눌러 게임을 시작하세요");
        preStartMessage.setVisible(true);
        startGameMessage.setVisible(false);
        
        startGameMessage.setVisible(true);
        new Timer(1000, e -> {
            frame.remove(startMessagePanel); // startMessagePanel 제거
            frame.revalidate(); // 레이아웃 재검토
            frame.repaint(); // 프레임 다시 그리기
        }).start(); // 1초 후 메시지 패널 숨김

		// UI 요소 초기화
		scoreLabel.setText("점수: 0");
		timerLabel.setText("시간: 30");
		for (JButton[] buttonRow : buttons) {
			for (JButton button : buttonRow) {
				button.setEnabled(true);
				button.setIcon(whitePanelImage); //.setBackground(Color.WHITE);
			}
		}
		
		frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    preStartMessage.setVisible(false);
                 // 게임 타이머 및 시계 타이머 재시작
                    gameTimer.restart();
            		clockTimer.restart();
            		isGameActive = true; // 게임 활성화 상태로 설정
            		gridPanel.setVisible(true); // 게임 패널 보이기
                }
            }
        });
		
		// 파란 칸 및 황금 칸 초기화
		randomizeButton();
		
	}
	
	// 배경을 그리기 위한 커스텀 패널 클래스
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(Image backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

	class ButtonClickListener implements ActionListener {
		SoundManager soundManager1 = new SoundManager();
		private int row, col;

		public ButtonClickListener(int row, int col) {
			this.row = row;
			this.col = col;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isGameActive)
				return;
			
			JButton button = (JButton) e.getSource();
			JButton button2 = (JButton) e.getSource();

			// 점수 오르는 정도
			if (row == currentRow && col == currentCol) {
				score++;
				moveBlueSquare();
				moveGoldenSquare();
				soundManager1.loadEffect("bgm2.wav"); // 실제 파일 경로로 대체
		        soundManager1.playEffect();
			} else if (row == Gold_currentRow && col == Gold_currentCol) {
				score += 5;
				// 황금 칸 이동
				moveBlueSquare();
				moveGoldenSquare();
				soundManager1.loadEffect("bgm2.wav"); // 실제 파일 경로로 대체
		        soundManager1.playEffect();
			} else {
				// 막 클릭 하는 사람이 있을 수 있기 때문에 점수 감소를 넣음
				score--;
			}
			updateScoreDisplay();
		}
	}
}

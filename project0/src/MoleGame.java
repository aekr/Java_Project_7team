package minimi;

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
		// 초기 메시지 표시
		JOptionPane.showMessageDialog(frame, "스페이스바를 눌러 게임을 시작하세요.");
		startGame();
	}

	private void initialize() {
		// 플레이어 스코어
		currentPlayer = 0;
		playerScores[0] = 0;
		playerScores[1] = 0;

		// 기본 UI 설정
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
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

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				JButton button = new JButton();
				button.setBackground(Color.WHITE);
				button.addActionListener(new ButtonClickListener(i, j));
				buttons[i][j] = button;
				buttons2[i][j] = button;
				gridPanel.add(button);
			}
		}

		// 게임 타이머 설정 (30초 후 게임 종료)
		gameTimer = new Timer(30000, e -> endGame());
		gameTimer.start();

		// 시계 타이머 설정 (1초마다 시간 업데이트)
		clockTimer = new Timer(1000, e -> updateTimer());
		clockTimer.start();

		randomizeButton();
		moveBlueSquareAfterDelay();

		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					startGame();
				}
			}
		});
		frame.setFocusable(true); // KeyListener를 활성화하기 위해 필요
	}
	
	//랜덤 딜레이
	private int randomDelayBetween(int min, int max) {
		Random random = new Random();
		return min + random.nextInt(max - min + 1);
	}
	
	//일정 시간 뒤 새로고침(난이도 조절 용)
	private void moveBlueSquareAfterDelay() {
		int delay = randomDelayBetween(1000, 2000);
		Timer delayTimer = new Timer(delay, e -> moveBlueSquare());
		delayTimer.setRepeats(true); //false로 바꾸면 반복 안함.
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
			buttons[currentRow][currentCol].setBackground(Color.WHITE);
		}

		currentRow = newRow;
		currentCol = newCol;
		buttons[currentRow][currentCol].setBackground(Color.BLUE);
	}
	
	//황금 타일(5점 줌)
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
			buttons2[Gold_currentRow][Gold_currentCol].setBackground(Color.WHITE);
		}

		GoldCheck = rand.nextInt(20);
		Gold_currentRow = newRow;
		Gold_currentCol = newCol;

		if (GoldCheck >= 17)
			buttons2[Gold_currentRow][Gold_currentCol].setBackground(Color.YELLOW);
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
			buttons[currentRow][currentCol].setBackground(Color.WHITE);
		}

		Random rand = new Random();
		currentRow = rand.nextInt(4);
		currentCol = rand.nextInt(4);
		buttons[currentRow][currentCol].setBackground(Color.BLUE);
	}

	//초당 업데이트
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
	            button.setBackground(Color.WHITE);
	        }
	    }

	    // 게임 타이머 및 시계 타이머 시작
	    gameTimer.start();
	    clockTimer.start();

	    // 첫 번째 파란 칸 및 황금 칸 초기화
	    randomizeButton();
	}


	private void endGame() {
		isGameActive = false;
		// 현재 플레이어 점수 저장
		playerScores[currentPlayer] = score;

		// 다음 플레이어로 전환
		currentPlayer++;
		if (currentPlayer < 2) {
			// 두 번째 플레이어 게임 시작
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
		JOptionPane.showMessageDialog(frame, "스페이스바를 눌러 게임을 시작하세요.");
		score = 0; // 점수 초기화
		time = 30; // 시간 초기화
		isGameActive = true; // 게임 활성화 상태로 설정
		currentPlayer = 1; // 두 번째 플레이어로 설정

		// UI 요소 초기화
		scoreLabel.setText("점수: 0");
		timerLabel.setText("시간: 30");
		for (JButton[] buttonRow : buttons) {
			for (JButton button : buttonRow) {
				button.setEnabled(true);
				button.setBackground(Color.WHITE);
			}
		}

		// 게임 타이머 및 시계 타이머 재시작
		gameTimer.restart();
		clockTimer.restart();

		// 파란 칸 및 황금 칸 초기화
		randomizeButton();
	}

	class ButtonClickListener implements ActionListener {
		private int row, col;

		public ButtonClickListener(int row, int col) {
			this.row = row;
			this.col = col;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isGameActive)
				return;

			// 점수 오르는 정도
			if (row == currentRow && col == currentCol) {
				score++;
				moveBlueSquare();
				moveGoldenSquare();
			} else if (row == Gold_currentRow && col == Gold_currentCol) {
				score += 5;
				// 황금 칸 이동
				moveBlueSquare();
				moveGoldenSquare();
			} else {
				// 막 클릭 하는 사람이 있을 수 있기 때문에 점수 감소를 넣음
				score--;
			}
			updateScoreDisplay();
		}
	}
}

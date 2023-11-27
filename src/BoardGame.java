import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.Image;
import java.io.*;
import java.awt.event.*;

public class BoardGame {
	private JFrame frame;
	private JPanel boardPanel;
	private JPanel controlPanel;
	private JButton rollDiceButton1P;
	private JLabel playerPosition1P;
	private JLabel playerPosition2P;
	private int currentPosition1P = 0;
	private int currentPosition2P = 0;
	private final int totalCells = 2 * (3 + 4);
	private int star_1p = 0;
	private int star_2p = 0;
	private int coin_1p = 0; // 코인
	private int coin_2p = 0;
	private ImageIcon player1Icon;
	private ImageIcon player2Icon;
	private Image backgroundImage;
	private Clip bgm;
	private int turnCount = 1; // 턴
	private Random random = new Random();
	private boolean p1turn = true;
	private boolean p2turn = true;
	private static BoardGame window;
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				window = new BoardGame();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public BoardGame() {
		initialize();
	}

	class BackgroundPanel extends JPanel {

		public BackgroundPanel(String imagePath) {
			try {
				backgroundImage = ImageIO.read(getClass().getResource(imagePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
			setLayout(new BorderLayout());
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (backgroundImage != null) {
				g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
			}
		}
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1300, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// true로 설정 돼 있으면 false로
		frame.setResizable(false);

		BackgroundPanel backgroundPanel = new BackgroundPanel("main_background.png"); 
		frame.setContentPane(backgroundPanel);

		boardPanel = new JPanel(new GridLayout(4, 5));
		// true로 설정 돼 있으면 false로
		boardPanel.setOpaque(false);
		backgroundPanel.add(boardPanel, BorderLayout.CENTER);

		try {
			Image img = ImageIO.read(getClass().getResource("player_1.png"));
			Image resizedImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			player1Icon = new ImageIcon(resizedImg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Image img = ImageIO.read(getClass().getResource("player_2.png"));
			Image resizedImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			player2Icon = new ImageIcon(resizedImg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		controlPanel = new JPanel(new FlowLayout());
		controlPanel.setOpaque(false);
		backgroundPanel.add(controlPanel, BorderLayout.SOUTH);
		rollDiceButton1P = new JButton("P1 주사위 굴리기");
		rollDiceButton1P.addActionListener(new RollDiceListener(true));
		controlPanel.add(rollDiceButton1P);

		JPanel statusPanel = new JPanel(new FlowLayout());
		statusPanel.setOpaque(false);
		backgroundPanel.add(statusPanel, BorderLayout.NORTH);
		playerPosition1P = new JLabel("P1의 star : " + star_1p
				+ " coins : " + coin_1p);
		playerPosition2P = new JLabel(" P2의 star : " + star_2p
				+ " coins : " + coin_2p +"현재 턴 : " + turnCount);
		statusPanel.add(playerPosition1P);
		statusPanel.add(playerPosition2P);

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				playBgm("main_bgm.wav");
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				stopBgm();
			}
		});
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				playBgm("main_bgm.wav");
			}
		});
		updateBoard();
	}
	private void updateBoard() 
	{
		boardPanel.removeAll();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 5; j++) {
				JPanel cell = new JPanel(new BorderLayout());
				cell.setOpaque(false);
				cell.setPreferredSize(new Dimension(60, 48));
				
				boolean player1At = isPlayerAt(i, j, currentPosition1P);
				boolean player2At = isPlayerAt(i, j, currentPosition2P);

				// 두 플레이어가 같은 셀에 있으면 각각의 서브 패널에 추가
				if (player1At && player2At) {
					JPanel panel1P = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 50));
					panel1P.setOpaque(false);
					panel1P.add(new JLabel(player1Icon));
					cell.add(panel1P, BorderLayout.WEST);

					JPanel panel2P = new JPanel(new FlowLayout(FlowLayout.RIGHT, 50, 50));
					panel2P.setOpaque(false);
					panel2P.add(new JLabel(player2Icon));
					cell.add(panel2P, BorderLayout.EAST);
				} else {
					// 그렇지 않으면 중앙에 위치
					if (player1At) {
						cell.add(new JLabel(player1Icon), BorderLayout.CENTER);
					}
					if (player2At) {
						cell.add(new JLabel(player2Icon), BorderLayout.CENTER);
					}
				}

				boardPanel.add(cell);
			}
		}
		boardPanel.revalidate();
		boardPanel.repaint();
	}
	private boolean isPlayerAt(int row, int col, int position) {
		int targetRow = 0, targetCol = 0;
		if (position < 5) {
			targetRow = 0;
			targetCol = position;
		} else if (position < 7) {
			targetRow = position - 4;
			targetCol = 4;
		} else if (position <= 10) {
			targetRow = 3;
			targetCol = 11 - position;
		} else {
			targetRow = 14 - position;
			targetCol = 0;
		}

		return row == targetRow && col == targetCol;
	}
	public void textUpdate()
	{
		playerPosition1P.setText("P1의 star : " + star_1p
				+ " coins : " + coin_1p);
		playerPosition2P.setText(" P2의 star : " + star_2p
				+ " coins : " + coin_2p +"현재 턴 : " + turnCount);
	}
	public void P1_win()
	{
		coin_1p +=10;
		coin_2p +=7;
		textUpdate();
	}
	public void P2_win()
	{
		coin_2p +=10;
		coin_1p +=7;
		textUpdate();
	}
	public void draw()
	{
		coin_1p +=7;
		coin_2p +=7;
		textUpdate();
	}
	private class RollDiceListener implements ActionListener {
		private boolean is1P;
		private Timer timer;
		private int rollCount;
		boolean plus_move = false;
		public RollDiceListener(boolean is1P) {
			this.is1P = is1P;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			rollDiceButton1P.setEnabled(false);
			Random rand = new Random();
			int roll = rand.nextInt(4) + 1;
			rollCount = roll;
			timer = new Timer(500, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					GameendCheck();
					rollCount--;
					if (is1P) 
					{
						currentPosition1P = (currentPosition1P + 1) % totalCells;
						playerPosition1P.setText("P1의 star : " + star_1p
								+ " coins : " + coin_1p);
						if ((currentPosition1P == 4) && (coin_1p >=20) ) 
						{
							coin_1p-=20;
							star_1p++;
						}
					} else 
					{
						currentPosition2P = (currentPosition2P + 1) % totalCells;
						playerPosition2P.setText(" P2의 star : " + star_2p
								+ " coins : " + coin_2p +"현재 턴 : " + turnCount);
						if ((currentPosition2P == 4) && (coin_2p >=20) ) 
						{
							coin_2p-=20;
							star_2p++;
						}
					}
					updateBoard();
					//1p가 도착한 발판의 효과 발동
					if ((rollCount == 0) && (p1turn) && ((currentPosition1P == 2) || (currentPosition1P == 6) || (currentPosition1P == 11) || (currentPosition1P == 13)))
					{
						p1turn = false;
						coin_1p += 5;
					}
					if ((rollCount == 0) && (p1turn) && ((currentPosition1P == 1) || (currentPosition1P == 5) || (currentPosition1P == 8) || (currentPosition1P == 12)))
					{
						p1turn = false;
						if (coin_1p <= 3)
						{
							coin_1p = 0;
						}else
						{
							coin_1p -= 3;
						}
					}
					if (rollCount == 0 && (currentPosition1P == 3))
					{
						rollCount +=2;
						plus_move = true;
					}
					if (rollCount == 0 && (currentPosition1P == 10))
					{
						rollCount +=3;
						plus_move = true;
					}
					if (rollCount == 0 && (currentPosition1P == 7))
					{
						currentPosition1P = 0;
						plus_move = true;
					}
					if (rollCount == 0 && (currentPosition1P == 9))
					{
						currentPosition1P = 4;
						plus_move = true;
					}
					//2p가 도착한 발판의 효과 발동
					if ((rollCount == 0) && (p2turn) && ((currentPosition2P == 2) || (currentPosition2P == 6) || (currentPosition2P == 11) || (currentPosition2P == 13)))
					{
						p2turn = false;
						coin_2p += 5;
					}
					if ((rollCount == 0) && (p2turn) && ((currentPosition2P == 1) || (currentPosition2P == 5) || (currentPosition2P == 8) || (currentPosition2P == 12)))
					{
						p2turn = false;
						if (coin_2p <= 3)
						{
							coin_2p = 0;
						}else
						{
							coin_2p -= 3;
						}
					}
					if (rollCount == 0 && (currentPosition2P == 3))
					{
						rollCount +=2;
						plus_move = true;
					}
					if (rollCount == 0 && (currentPosition2P == 10))
					{
						rollCount +=3;
						plus_move = true;
					}
					if (rollCount == 0 && (currentPosition2P == 7))
					{
						currentPosition2P = 0;
						plus_move = true;
					}
					if (rollCount == 0 && (currentPosition2P == 9))
					{
						currentPosition2P = 4;
						plus_move = true;
					}
					if (plus_move)
					{
						    updateBoard(); //제어발판을 생각해 이동	
						    plus_move = false;
						if ((currentPosition1P == 4) && (coin_1p >=20))  //스타 발판으로 이동한 경우(1P)
						{
							coin_1p-=20;
							star_1p++;
						}
						else if ((currentPosition2P == 4) && (coin_2p >=20)) //스타 발판으로 이동한 경우(2P) 
						{
							coin_2p-=20;
							star_2p++;
						}
					}
					if (rollCount <= 0) {

						timer.stop();
						rollDiceButton1P.setEnabled(true);
						if (is1P) {

							is1P = false;
							rollDiceButton1P.setText("P2 주사위 굴리기");
							playerPosition1P.setText("P1의 star : " + star_1p
									+ " coins : " + coin_1p);
						} else {

							is1P = true;
							rollDiceButton1P.setText("P1 주사위 굴리기");
							playerPosition2P.setText(" P2의 star :" + star_2p
									+ " coins : " + coin_2p +"현재 턴 : " + turnCount);
							turnCount++;

							frame.setVisible(false);
							stopBgm();
//							int gamenumber = random.nextInt(5)+1;  //게임 추가하는 부분 현재 타자,연타,블럭피하기만 구현됨
							int gamenumber = 1;
							switch(gamenumber) 
							{
							case 1:
								new Tajagame(frame,window);
								p1turn = true;
								p2turn = true;
								break;
							case 2:	
						        TwoPlayerObstacleGame game = new TwoPlayerObstacleGame();
						        game.startGame(frame,window);
								p1turn = true;
								p2turn = true;
						        break;
							case 3:
		                         new Pingpong_Game(frame,window);
		                         p1turn = true;
		                         p2turn = true;
		                         break;
		                     case 4:
		                         new Rapid_Fire_Game(frame,window);
		                         p1turn = true;
		                         p2turn = true;
		                         break;
		                     case 5:
		                         new MoleGame(frame, window);
		                         p1turn = true;
		                         p2turn = true;
		                         break;
		                     }
						}
					}
				}
			});
			timer.start();
		}
	}

	public void playBgm(String filePath) {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
			bgm = AudioSystem.getClip();
			bgm.open(audioInputStream);
			bgm.loop(Clip.LOOP_CONTINUOUSLY);
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

	public void restart() {
		bgm.setMicrosecondPosition(0);
		bgm.start();
	}
	// 게임 종료 로직
	public void GameendCheck() {
		String message1 = String.format("1P의 점수 - 별 : %d, 코인 : %d | 2P의 점수 - 별 : %d, 코인 : %d", star_1p, coin_1p,
				star_2p, coin_2p);
		if (turnCount >= 8) {
			if (star_1p > star_2p) {
				// 1P 승리 메시지 표시
				JOptionPane.showMessageDialog(frame, message1);
				JOptionPane.showMessageDialog(frame, "1P가 승리했습니다! ");
			} else if (star_1p < star_2p) {
				// 2P 승리 메시지 표시
				JOptionPane.showMessageDialog(frame, message1);
				JOptionPane.showMessageDialog(frame, "2P가 승리했습니다! ");
			} else if (star_1p == star_2p && coin_1p > coin_2p) {
				// 코인
				JOptionPane.showMessageDialog(frame, message1);
				JOptionPane.showMessageDialog(frame, "1P가 승리했습니다! ");
			} else if (star_1p == star_2p && coin_1p < coin_2p) {
				JOptionPane.showMessageDialog(frame, message1);
				JOptionPane.showMessageDialog(frame, "2P가 승리했습니다! ");
			} else {
				// 무승부 메시지 표시
				JOptionPane.showMessageDialog(frame, message1);
				JOptionPane.showMessageDialog(frame, "무승부! ");
			}
			// 모든 창 종료
			System.exit(0);
		} else {
			return;
		}
	}
}
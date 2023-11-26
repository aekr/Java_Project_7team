import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

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

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				BoardGame window = new BoardGame();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public BoardGame() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1300, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setResizable(false);
		boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(4, 5));
		frame.getContentPane().add(boardPanel, BorderLayout.CENTER);

		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		rollDiceButton1P = new JButton("1P Roll Dice");
		rollDiceButton1P.addActionListener(new RollDiceListener(true));
		controlPanel.add(rollDiceButton1P);
		frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);

		playerPosition1P = new JLabel("1P is at position: " + currentPosition1P);
		playerPosition2P = new JLabel("2P is at position: " + currentPosition2P);

		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new FlowLayout());
		statusPanel.add(playerPosition1P);
		statusPanel.add(playerPosition2P);
		frame.getContentPane().add(statusPanel, BorderLayout.NORTH);

		updateBoard();
	}

	private void updateBoard() {
		boardPanel.removeAll();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 5; j++) {
				JPanel cell = new JPanel();
				cell.setPreferredSize(new Dimension(60, 48)); // Adjust size as needed
				if (isPlayerAt(i, j, currentPosition1P)) {
					cell.add(new JLabel(new ColorIcon(Color.BLUE)));
				}
				if (isPlayerAt(i, j, currentPosition2P)) {
					cell.add(new JLabel(new ColorIcon(Color.RED)));
				}
				boardPanel.add(cell);
			}
		}
		boardPanel.revalidate();
		boardPanel.repaint();
	}

	private boolean isPlayerAt(int row, int col, int position) {
		// Convert the 1D position to a 2D position on the board
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

	// 제안한 위치 제어들
	private class RollDiceListener implements ActionListener {
		private boolean is1P;
		private Timer timer;
		private int rollCount;
		
		private void updatePlayerPosition(int player) {
		    int newPosition;
		    if (player == 1) {
		        newPosition = currentPosition1P;
		    } else {
		        newPosition = currentPosition2P;
		    }

		    switch (newPosition) {
		        case 2:
		            newPosition = 0; // 처음으로 이동
		            break;
		        case 4:
		            newPosition = 13; // 지정된 위치로 이동
		            break;
		        case 7:
		            newPosition = (newPosition + 3) % totalCells; // 앞으로 3칸 이동
		            break;
		        case 9:
		            newPosition = (newPosition - 2 + totalCells) % totalCells; // 뒤로 2칸 이동
		            break;
		    }

		    if (player == 1) {
		        currentPosition1P = newPosition;
		        playerPosition1P.setText("1P is at position: " + currentPosition1P);
		    } else {
		        currentPosition2P = newPosition;
		        playerPosition2P.setText("2P is at position: " + currentPosition2P);
		    }
		}

		public RollDiceListener(boolean is1P) {
			this.is1P = is1P;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			rollDiceButton1P.setEnabled(false); // 주사위 굴리는 동안 버튼 비활성화
			Random rand = new Random();
			int roll = rand.nextInt(6) + 1;
			rollCount = roll;
			timer = new Timer(500, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (is1P) {
						currentPosition1P = (currentPosition1P + 1) % totalCells;
						playerPosition1P.setText("1P is at position: " + currentPosition1P);
					} else {
						currentPosition2P = (currentPosition2P + 1) % totalCells;
						playerPosition2P.setText("2P is at position: " + currentPosition2P);
					}
					updateBoard();
					rollCount--;
					if (rollCount <= 0) {
						timer.stop();
						rollDiceButton1P.setEnabled(true); // 이동이 끝나면 버튼 다시 활성화
						// 모든 이동이 완료된 후 특별한 칸에 대한 로직 체크
						if (is1P) {
							updatePlayerPosition(1);
							is1P = false;
							rollDiceButton1P.setText("2P Roll Dice");
						} else {
							updatePlayerPosition(2);
							is1P = true;
							rollDiceButton1P.setText("1P Roll Dice");
						}
						updateBoard();
					}
				}
			});
			timer.start();
		}
	}

	private static class ColorIcon implements Icon {
		private final Color color;

		public ColorIcon(Color color) {
			this.color = color;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.setColor(color);
			g.fillOval(x, y, getIconWidth(), getIconHeight());
		}

		@Override
		public int getIconWidth() {
			return 50;
		}

		@Override
		public int getIconHeight() {
			return 50;
		}
	}
}
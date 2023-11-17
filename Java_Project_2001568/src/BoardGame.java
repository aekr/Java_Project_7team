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
    private final int totalCells = 2 * (3+4);
    private int player1_coins;
    private int player2_coins;
    private boolean is1P;
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
        boardPanel.setLayout(new GridLayout(4,5));
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

    private void getCoins()
    {
    	if(is1P)
    	{
    		player1_coins +=5;
    	}else
    	{
    		player2_coins +=5;
    	}
    }
    private void dropCoins()
    {
    	if(is1P)
    	{
    		player1_coins -= 3;
    	}
    	else
    	{
    		player2_coins -= 3;
    	}
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

    
    private class RollDiceListener implements ActionListener {
        private Timer timer;
        private int rollCount;

        public RollDiceListener(boolean is1P) {
        	;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            rollDiceButton1P.setEnabled(false); // 주사위 굴리는 동안 버튼 비활성화
            Random rand = new Random();
            int roll = rand.nextInt(4) + 1;
            rollCount = roll;
            timer = new Timer(1000, new ActionListener() {
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
                        if (is1P) {
                            is1P = false;
                            rollDiceButton1P.setText("2P Roll Dice");
                        } else {
                            is1P = true;
                            rollDiceButton1P.setText("1P Roll Dice");
                        }
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

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
    private final int totalCells = 2 * (3+4);
    private int star_1p=0;
    private int star_2p=0;
    // 코인
    private int coin_1p=0;
    private int coin_2p=0;
    private ImageIcon player1Icon;
    private ImageIcon player2Icon;
    private Image backgroundImage;
    private Clip bgm;
    private int turnCount = 1;
    
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
        
        frame.setResizable(false);

        
        BackgroundPanel backgroundPanel = new BackgroundPanel("pingpong_image.png");
        frame.setContentPane(backgroundPanel);

        boardPanel = new JPanel(new GridLayout(4, 5));
        boardPanel.setOpaque(false); 
        backgroundPanel.add(boardPanel, BorderLayout.CENTER);
        
        try {
            Image img = ImageIO.read(getClass().getResource("pingpong_image.png"));
            Image resizedImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            player1Icon = new ImageIcon(resizedImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Image img = ImageIO.read(getClass().getResource("pingpong_image.png"));
            Image resizedImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            player2Icon = new ImageIcon(resizedImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        

        controlPanel = new JPanel(new FlowLayout());
        controlPanel.setOpaque(false); 
        backgroundPanel.add(controlPanel, BorderLayout.SOUTH);
        rollDiceButton1P = new JButton("1P Roll Dice");
        rollDiceButton1P.addActionListener(new RollDiceListener(true));
        controlPanel.add(rollDiceButton1P);
        
        JPanel statusPanel = new JPanel(new FlowLayout());
        statusPanel.setOpaque(false);  
        backgroundPanel.add(statusPanel, BorderLayout.NORTH);
        playerPosition1P = new JLabel("1P is at position: " + currentPosition1P);
        playerPosition2P = new JLabel("2P is at position: " + currentPosition2P);
        statusPanel.add(playerPosition1P);
        statusPanel.add(playerPosition2P);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                playBgm("rapid_bgm.wav");
            }
            @Override
            public void componentHidden(ComponentEvent e) {
                stopBgm();
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                playBgm("rapid_bgm.wav");
            }
        });
        updateBoard();
    }

    private void updateBoard() {
        boardPanel.removeAll();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                JPanel cell = new JPanel();
                cell.setOpaque(false); 
                cell.setPreferredSize(new Dimension(60, 48));

                if (isPlayerAt(i, j, currentPosition1P)) {
                    cell.add(new JLabel(player1Icon));
                }
                if (isPlayerAt(i, j, currentPosition2P)) {
                    cell.add(new JLabel(player2Icon));
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
    
    private class RollDiceListener implements ActionListener {
        private boolean is1P;
        private Timer timer;
        private int rollCount;

        public RollDiceListener(boolean is1P) {
            this.is1P = is1P;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            rollDiceButton1P.setEnabled(false); 
            Random rand = new Random();
            int roll = rand.nextInt(6) + 1;
            rollCount = roll;
            timer = new Timer(500, new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (is1P) {
                        currentPosition1P = (currentPosition1P + 1) % totalCells;
                        playerPosition1P.setText("1P is at position: " + currentPosition1P+"star:"+star_1p + "turncount:" + turnCount);
                        if(currentPosition1P==4) {
                           star_1p++;
                        }
                    } else {
                        currentPosition2P = (currentPosition2P + 1) % totalCells;
                        playerPosition2P.setText("2P is at position: " + currentPosition2P+"star:"+star_2p + "turncount:" + turnCount);
                        if(currentPosition2P==4) {
                           star_2p++;
                        }
                    }
                    updateBoard();
                    rollCount--;
                    if (rollCount <= 0) {
                    	
                        timer.stop();
                        rollDiceButton1P.setEnabled(true); 
                        if (is1P) {
                        	
                            is1P = false;
                            rollDiceButton1P.setText("2P Roll Dice");
                            playerPosition1P.setText("1P is at position: " + currentPosition1P+"star:"+star_1p + "turncount:" + turnCount);
                        } else {
                        	
                            is1P = true;
                            rollDiceButton1P.setText("1P Roll Dice");
                            turnCount++;
                            playerPosition2P.setText("2P is at position: " + currentPosition2P+"star:"+star_2p + "turncount:" + turnCount);
                            frame.setVisible(false);
                            stopBgm();
                            new Rapid_Fire_Game   (frame);
                            // frame.setVisible(true); 밑에 넣기
                            GameendCheck();
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
            bgm.start();
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
    	String message1 = String.format("1P의 점수 - 별 : %d, 코인 : %d | 2P의 점수 - 별 : %d, 코인 : %d", star_1p, coin_1p , star_2p, coin_2p);
    	if (turnCount >= 3) {
    		if (star_1p > star_2p) {
    		    // 1P 승리 메시지 표시
    			JOptionPane.showMessageDialog(frame, message1);
    			JOptionPane.showMessageDialog(frame, "1P가 승리했습니다! ");
    		} else if (star_1p < star_2p) {
    		    // 2P 승리 메시지 표시
    			JOptionPane.showMessageDialog(frame, message1);
    			JOptionPane.showMessageDialog(frame, "2P가 승리했습니다! ");
    		} else if (star_1p == star_2p && coin_1p > coin_2p){
    			// 코인 
    			JOptionPane.showMessageDialog(frame, message1);
    			JOptionPane.showMessageDialog(frame, "1P가 승리했습니다! ");
    		} else if (star_1p == star_2p && coin_1p < coin_2p){
    			JOptionPane.showMessageDialog(frame, message1);
    			JOptionPane.showMessageDialog(frame, "2P가 승리했습니다! ");
    		}
    		else {
    			// 무승부 메시지 표시
    			JOptionPane.showMessageDialog(frame, message1);
    			JOptionPane.showMessageDialog(frame, "무승부! ");
    		}
    		
    	//모든 창 종료
    	System.exit(0);
    	}
    	else {
    		return;
    	}
    }
}
    
    

package brickbreakergame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.File;

public class GamePlay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private boolean isPaused = false;
    private int score = 0;
    private int lives = 3; // Initialize lives
    private int totalBricks;
    private int level = 1; // Track the current level
    private Timer timer;
    private int delay = 8;
    private int playerX = 310;
    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;
    private Clip backgroundMusic;
    private String musicFilePath = "C:\\Users\\Kaleeswari\\eclipse-workspace\\Brick Breaker Game\\Music\\background_music.wav";
    private long musicPausePosition = 0; // Store the paused position
    private boolean isMusicPlaying = false; // Track if music is currently playing
    private MapGenerator map;

    public GamePlay() {
        initializeGame();
    }

    private void initializeGame() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
        startNewLevel(); // Initialize the first level
    }

    private void startNewLevel() {
        map = new MapGenerator(level + 2, 7); // Initialize with level-based bricks
        totalBricks = map.map.length * map.map[0].length;
        resetBallAndPaddlePosition();
        // Start music only if it's not already playing
        if (!isMusicPlaying) {
            playBackgroundMusic();
        }
    }

    private void resetGame() {
        play = true;
        isPaused = false;
        score = 0;
        lives = 3; // Reset lives
        level = 1; // Reset to level 1
        startNewLevel(); // Start from level 1
    }

    private void playBackgroundMusic() {
        try {
            stopBackgroundMusic(); // Stop any previous music
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(musicFilePath));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
            musicPausePosition = 0; // Reset pause position
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music continuously
            isMusicPlaying = true; // Set the music playing flag
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            musicPausePosition = backgroundMusic.getMicrosecondPosition(); // Save the current position
            backgroundMusic.stop(); // Stop the music
            isMusicPlaying = false; // Update the music playing flag
        }
    }

    private void resumeBackgroundMusic() {
        if (backgroundMusic != null && !backgroundMusic.isRunning()) {
            try {
                backgroundMusic.setMicrosecondPosition(musicPausePosition); // Resume from where paused
                backgroundMusic.start(); // Start the music
                isMusicPlaying = true; // Set the music playing flag
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void playSound(String soundFileName) {
        try {
            String filePath = "C:\\Users\\Kaleeswari\\eclipse-workspace\\Brick Breaker Game\\Music\\" + soundFileName;
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);
        map.draw((Graphics2D) g);
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score: " + score, 500, 30);
        g.drawString("Lives: " + lives, 30, 30); // Display lives
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);

        if (isPaused) {
            g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Paused", 300, 300);
        }

        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD, 30));
            if (level < 3) {
                g.drawString("Level " + level + " Completed!", 220, 300);
                g.setFont(new Font("serif", Font.BOLD, 20));
                g.drawString("Press Enter to Continue", 230, 350);
                stopBackgroundMusic(); // Stop music on level completion
            } else {
                g.drawString("Game Completed!", 250, 300);
                g.setFont(new Font("serif", Font.BOLD, 20));
                g.drawString("Press Enter to Restart", 230, 350);
                stopBackgroundMusic(); // Stop music on game completion
            }
        }

        if (ballposY > 570) {
            lives--; // Decrease life count
            if (lives > 0) {
                play = true;
                resetBallAndPaddlePosition(); // Reset ball and paddle positions
            } else {
                play = false;
                ballXdir = 0;
                ballYdir = 0;
                g.setColor(Color.red);
                g.setFont(new Font("serif", Font.BOLD, 30));
                g.drawString("Game Over, Scores: " + score, 190, 300);
                g.setFont(new Font("serif", Font.BOLD, 20));
                g.drawString("Press Enter to Restart", 230, 350);
                stopBackgroundMusic(); // Stop music on game over
            }
        }

        g.dispose();
    }

    private void resetBallAndPaddlePosition() {
        ballposX = 120;
        ballposY = 350;
        ballXdir = -1;
        ballYdir = -2;
        playerX = 310;
        // Only play music if it's not already playing
        if (!isMusicPlaying) {
            playBackgroundMusic(); // Start music from the beginning
        }
        repaint();
    }

    private void increaseBallSpeed() {
        // Gradual increase in ball speed
        ballXdir = (ballXdir > 0 ? 1 : -1) * (level + 1);
        ballYdir = (ballYdir > 0 ? 1 : -1) * (level + 1);
    }

    public void actionPerformed(ActionEvent e) {
        if (play && !isPaused) {
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
                playSound("paddle_hit.wav");
            }

            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;
                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;
                            playSound("brick_break.wav");

                            if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }

                            break A; // Break out of both loops if a brick is hit
                        }
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;
            if (ballposX < 0) {
                ballXdir = -ballXdir;
            }
            if (ballposY < 0) {
                ballYdir = -ballYdir;
            }
            if (ballposX > 670) {
                ballXdir = -ballXdir;
            }

            repaint();
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                if (lives <= 0) {
                    resetGame(); // Restart the game if all lives are lost
                } else if (totalBricks <= 0) {
                    level++; // Move to the next level
                    startNewLevel(); // Start the new level
                } else {
                    play = true; // Resume the game if paused or after losing a life
                    resumeBackgroundMusic(); // Resume music from the paused position
                }
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_P) {
            isPaused = !isPaused;
            if (isPaused) {
                play = false; // Stop the game when paused
                stopBackgroundMusic(); // Pause background music
            } else {
                play = true; // Resume the game
                resumeBackgroundMusic(); // Resume music from the paused position
            }
            repaint(); // Repaint to show "Paused"
        }
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }
}

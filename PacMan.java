import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PacMan extends JFrame implements KeyListener {

    GameSettings settings;

    GameMap map;

    Block pacman;

    ArrayList<Ghost> ghosts;

    GamePanel gamePanel;

    Timer timer;

    int score;
    int lives;

    int playerMoveCounter;
    int ghostMoveCounter;

    int playerMoveDelay = 4;
    int ghostMoveDelay;

    int requestedDirectionX = 0;
    int requestedDirectionY = 0;

    int powerModeTicks = 0;

    boolean powerMode = false;
    boolean gameOver = false;
    boolean won = false;

    public PacMan(GameSettings settings) {

        this.settings = settings;

        setTitle("Pac-Man Game");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(false);

        setFocusable(true);

        setFocusTraversalKeysEnabled(false);

        addKeyListener(this);

        resetGameState();

        gamePanel = new GamePanel(this);

        add(gamePanel);

        pack();

        setLocationRelativeTo(null);

        setVisible(true);

        requestFocusInWindow();

        // Update the game every 50 milliseconds.

        timer = new Timer(50, e -> {

            updateGame();

            gamePanel.repaint();
        });

        timer.start();
    }

    public void resetGameState() {

        map = new GameMap();

        score = 0;
        lives = 3;

        playerMoveCounter = 0;
        ghostMoveCounter = 0;

        powerModeTicks = 0;
        powerMode = false;

        gameOver = false;
        won = false;

        requestedDirectionX = 0;
        requestedDirectionY = 0;

        // Pac-Man starts at row 15, column 9.

        pacman = new Block(
                9 * GameMap.TILE_SIZE,
                15 * GameMap.TILE_SIZE,
                GameMap.TILE_SIZE,
                GameMap.TILE_SIZE,
                settings.getPlayerColor()
        );

        createGhosts();

        // Difficulty controls ghost movement speed.

        if (settings.difficulty.equals("Easy")) {

            ghostMoveDelay = 8;

        } else if (settings.difficulty.equals("Medium")) {

            ghostMoveDelay = 5;

        } else {

            ghostMoveDelay = 3;
        }
    }

    public void createGhosts() {

        ghosts = new ArrayList<>();

        Color[] ghostColors = {
                Color.RED,
                Color.BLUE,
                Color.PINK,
                Color.ORANGE
        };

        int ghostCount;

        if (settings.difficulty.equals("Easy")) {

            ghostCount = 2;

        } else if (settings.difficulty.equals("Medium")) {

            ghostCount = 3;

        } else {

            ghostCount = 4;
        }

        int[] ghostColumns = {8, 9, 10, 11};

        for (int i = 0; i < ghostCount; i++) {

            Ghost ghost = new Ghost(
                    ghostColumns[i] * GameMap.TILE_SIZE,
                    9 * GameMap.TILE_SIZE,
                    GameMap.TILE_SIZE,
                    GameMap.TILE_SIZE,
                    ghostColors[i]
            );

            ghosts.add(ghost);
        }
    }

    public void updateGame() {

        if (gameOver) {
            return;
        }

        // Update Pac-Man movement.

        playerMoveCounter++;

        if (playerMoveCounter >= playerMoveDelay) {

            playerMoveCounter = 0;

            movePlayer();
        }

        // Update ghost movement.

        ghostMoveCounter++;

        if (ghostMoveCounter >= ghostMoveDelay) {

            ghostMoveCounter = 0;

            for (Ghost ghost : ghosts) {

                ghost.move(map,pacman);
            }
        }

        // Update power mode timer.

        if (powerModeTicks > 0) {

            powerModeTicks--;

            powerMode = true;

        } else {

            powerMode = false;
        }

        // Tell each ghost whether it is scared.

        for (Ghost ghost : ghosts) {

            ghost.scared = powerMode;
        }

        checkCollisions();

        checkWin();
    }

    public void movePlayer() {

        int dx = requestedDirectionX;
        int dy = requestedDirectionY;

        // Try the direction requested by the player first.

        if (dx != 0 || dy != 0) {

            if (tryMovePlayer(dx, dy)) {

                return;
            }
        }

        // If turning was not possible, continue moving
        // in the previous direction.

        if (pacman.directionX != 0
                || pacman.directionY != 0) {

            if (!tryMovePlayer(
                    pacman.directionX,
                    pacman.directionY
            )) {

                pacman.directionX = 0;
                pacman.directionY = 0;
            }
        }
    }

    public boolean tryMovePlayer(int dx, int dy) {

        int currentRow =
                pacman.y / GameMap.TILE_SIZE;

        int currentCol =
                pacman.x / GameMap.TILE_SIZE;

        int nextRow = currentRow + dy;
        int nextCol = currentCol + dx;

        // Wrap around the horizontal tunnel.

        if (currentRow == 10 && nextCol < 0) {

            nextCol = GameMap.COLS - 1;

        } else if (
                currentRow == 10
                        && nextCol >= GameMap.COLS
        ) {

            nextCol = 0;
        }

        // Stop if a wall is present.

        if (map.isWall(nextRow, nextCol)) {

            return false;
        }

        // Update Pac-Man's position.

        pacman.x = nextCol * GameMap.TILE_SIZE;
        pacman.y = nextRow * GameMap.TILE_SIZE;

        pacman.directionX = dx;
        pacman.directionY = dy;

        // Collect anything on the new tile.

        collectItem(nextRow, nextCol);

        return true;
    }

    public void collectItem(int row, int col) {

        char item = map.consumeAt(row, col);

        if (item == '.') {

            score += 10;

        } else if (item == 'o') {

            score += 10;

            // Power mode lasts approximately 5 seconds.
            // Timer updates happen every 50 milliseconds.

            powerModeTicks = 100;

            powerMode = true;

        } else if (item == 'c') {

            // Cherry bonus.

            score += 50;
        }
    }

    public void checkCollisions() {

        for (Ghost ghost : ghosts) {

            if (pacman.getBounds().intersects(
                    ghost.getBounds()
            )) {

                if (powerMode) {

                    // Eat the scared ghost.

                    score += 200;

                    ghost.resetPosition();

                } else {

                    // Lose one life.

                    lives--;

                    if (lives <= 0) {

                        lives = 0;

                        gameOver = true;

                    } else {

                        // Reset Pac-Man.

                        pacman.resetPosition();

                        requestedDirectionX = 0;
                        requestedDirectionY = 0;

                        // Reset all ghosts.

                        for (Ghost g : ghosts) {

                            g.resetPosition();
                        }
                    }

                    // Handle only one collision at a time.

                    break;
                }
            }
        }
    }

    public void checkWin() {

        if (map.getRemainingFood() == 0) {

            won = true;

            gameOver = true;
        }
    }

    public void restartGame() {

        resetGameState();

        gamePanel.repaint();

        requestFocusInWindow();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (gameOver) {

            if (e.getKeyCode() == KeyEvent.VK_R) {

                restartGame();
            }

            return;
        }

        // Arrow keys control Pac-Man.

        if (e.getKeyCode() == KeyEvent.VK_UP) {

            requestedDirectionX = 0;
            requestedDirectionY = -1;

        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {

            requestedDirectionX = 0;
            requestedDirectionY = 1;

        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {

            requestedDirectionX = -1;
            requestedDirectionY = 0;

        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

            requestedDirectionX = 1;
            requestedDirectionY = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
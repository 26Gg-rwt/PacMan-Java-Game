import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    PacMan game;

    public GamePanel(PacMan game) {

        this.game = game;

        setPreferredSize(new Dimension(
                GameMap.WIDTH,
                GameMap.HEIGHT + 45
        ));

        setBackground(Color.BLACK);

        setFocusable(false);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        drawMap(g2);

        drawPacMan(g2);

        drawGhosts(g2);

        drawStatus(g2);

        if (game.gameOver) {

            drawGameOver(g2);
        }
    }

    public void drawMap(Graphics2D g) {

        for (int row = 0; row < GameMap.ROWS; row++) {

            for (int col = 0; col < GameMap.COLS; col++) {

                int x = col * GameMap.TILE_SIZE;
                int y = row * GameMap.TILE_SIZE;

                char tile = game.map.getTile(row, col);

                if (tile == '#') {

                    // Draw walls.

                    g.setColor(new Color(30, 60, 200));

                    g.fillRect(
                            x,
                            y,
                            GameMap.TILE_SIZE,
                            GameMap.TILE_SIZE
                    );

                } else if (tile == '.') {

                    // Draw normal food.

                    g.setColor(Color.WHITE);

                    g.fillOval(
                            x + 13,
                            y + 13,
                            6,
                            6
                    );

                } else if (tile == 'o') {

                    // Draw power pellets.

                    g.setColor(Color.WHITE);

                    g.fillOval(
                            x + 7,
                            y + 7,
                            18,
                            18
                    );

                } else if (tile == 'c') {

                    // Draw cherry.

                    g.setColor(Color.RED);

                    g.fillOval(
                            x + 7,
                            y + 12,
                            12,
                            12
                    );

                    g.fillOval(
                            x + 16,
                            y + 12,
                            12,
                            12
                    );

                    g.setColor(Color.GREEN);

                    g.drawLine(
                            x + 16,
                            y + 12,
                            x + 18,
                            y + 5
                    );
                }
            }
        }
    }

    public void drawPacMan(Graphics2D g) {

        g.setColor(game.pacman.color);

        int x = game.pacman.x;
        int y = game.pacman.y;

        int directionX = game.pacman.directionX;
        int directionY = game.pacman.directionY;

        int startAngle = 30;

        if (directionX < 0) {

            startAngle = 210;

        } else if (directionY < 0) {

            startAngle = 120;

        } else if (directionY > 0) {

            startAngle = 300;
        }

        g.fillArc(
                x,
                y,
                GameMap.TILE_SIZE,
                GameMap.TILE_SIZE,
                startAngle,
                300
        );
    }

    public void drawGhosts(Graphics2D g) {

        for (Ghost ghost : game.ghosts) {

            int x = ghost.x;
            int y = ghost.y;

            if (ghost.scared) {

                g.setColor(Color.CYAN);

            } else {

                g.setColor(ghost.color);
            }

            // Ghost's body.

            g.fillOval(
                    x + 2,
                    y + 2,
                    GameMap.TILE_SIZE - 4,
                    GameMap.TILE_SIZE - 4
            );

            g.fillRect(
                    x + 2,
                    y + 16,
                    GameMap.TILE_SIZE - 4,
                    12
            );

            // Ghost's eyes.

            g.setColor(Color.WHITE);

            g.fillOval(x + 7, y + 9, 7, 9);

            g.fillOval(x + 18, y + 9, 7, 9);

            g.setColor(Color.BLACK);

            g.fillOval(x + 9, y + 12, 3, 4);

            g.fillOval(x + 20, y + 12, 3, 4);
        }
    }

    public void drawStatus(Graphics2D g) {

        int y = GameMap.HEIGHT;

        g.setColor(Color.BLACK);

        g.fillRect(
                0,
                y,
                GameMap.WIDTH,
                45
        );

        g.setColor(Color.WHITE);

        g.setFont(new Font(
                "Arial",
                Font.BOLD,
                14
        ));

        g.drawString(
                "Player: " + game.settings.playerName,
                10,
                y + 20
        );

        g.drawString(
                "Score: " + game.score,
                220,
                y + 20
        );

        g.drawString(
                "Lives: " + game.lives,
                350,
                y + 20
        );

        g.drawString(
                "Level: " + game.settings.difficulty,
                450,
                y + 20
        );
    }

    public void drawGameOver(Graphics2D g) {

        g.setColor(new Color(0, 0, 0, 190));

        g.fillRect(
                0,
                0,
                GameMap.WIDTH,
                GameMap.HEIGHT
        );

        g.setColor(Color.WHITE);

        g.setFont(new Font(
                "Arial",
                Font.BOLD,
                32
        ));

        String message;

        if (game.won) {

            message = "YOU WIN!";

        } else {

            message = "GAME OVER";
        }

        FontMetrics metrics = g.getFontMetrics();

        int x = (
                GameMap.WIDTH
                        - metrics.stringWidth(message)
        ) / 2;

        g.drawString(
                message,
                x,
                GameMap.HEIGHT / 2
        );

        g.setFont(new Font(
                "Arial",
                Font.PLAIN,
                18
        ));

        String restartMessage = "Press R to Restart";

        int restartX = (
                GameMap.WIDTH
                        - g.getFontMetrics().stringWidth(
                                restartMessage
                        )
        ) / 2;

        g.drawString(
                restartMessage,
                restartX,
                GameMap.HEIGHT / 2 + 40
        );
    }
}
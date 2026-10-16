import java.awt.Color;

public class Ghost extends Block {

    boolean scared;

    public Ghost(
            int x,
            int y,
            int width,
            int height,
            Color color
    ) {
        super(x, y, width, height, color);
        scared = false;
    }

    public void move(GameMap map, Block pacman) {

        // Ghost ki current position
        int ghostRow = y / GameMap.TILE_SIZE;
        int ghostCol = x / GameMap.TILE_SIZE;

        // Pac-Man ki current position
        int playerRow = pacman.y / GameMap.TILE_SIZE;
        int playerCol = pacman.x / GameMap.TILE_SIZE;

        // Pac-Man aur ghost ke beech ka difference
        int dx = playerCol - ghostCol;
        int dy = playerRow - ghostRow;

        // Pehle us direction ko try karo
        // jo Pac-Man ke paas le jaati hai.

        int horizontal = Integer.signum(dx);
        int vertical = Integer.signum(dy);

        int[][] directions;

        if (scared) {

            // Scared mode mein opposite direction
            // mein jaane ki koshish karo.

            if (Math.abs(dx) > Math.abs(dy)) {

                directions = new int[][] {
                    {-horizontal, 0},
                    {0, -vertical},
                    {0, vertical},
                    {horizontal, 0}
                };

            } else {

                directions = new int[][] {
                    {0, -vertical},
                    {-horizontal, 0},
                    {horizontal, 0},
                    {0, vertical}
                };
            }

        } else {

            // Normal mode mein Pac-Man ko chase karo.

            if (Math.abs(dx) > Math.abs(dy)) {

                directions = new int[][] {
                    {horizontal, 0},
                    {0, vertical},
                    {0, -vertical},
                    {-horizontal, 0}
                };

            } else {

                directions = new int[][] {
                    {0, vertical},
                    {horizontal, 0},
                    {-horizontal, 0},
                    {0, -vertical}
                };
            }
        }

        // Directions ko ek-ek karke check karo.

        for (int[] direction : directions) {

            int nextRow = ghostRow + direction[1];
            int nextCol = ghostCol + direction[0];

            // Tunnel ke through doosri side par jaana.

            if (ghostRow == 10 && nextCol < 0) {

                nextCol = GameMap.COLS - 1;

            } else if (
                    ghostRow == 10
                    && nextCol >= GameMap.COLS
            ) {

                nextCol = 0;
            }

            // Wall nahi hai toh ghost move karega.

            if (!map.isWall(nextRow, nextCol)) {

                x = nextCol * GameMap.TILE_SIZE;
                y = nextRow * GameMap.TILE_SIZE;

                directionX = direction[0];
                directionY = direction[1];

                return;
            }
        }
    }
}
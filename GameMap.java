public class GameMap {

    public static final int TILE_SIZE = 32;

    public static final int ROWS = 21;
    public static final int COLS = 19;

    public static final int WIDTH = COLS * TILE_SIZE;
    public static final int HEIGHT = ROWS * TILE_SIZE;

    char[][] tiles;

    int remainingFood;

    public GameMap() {

        createMap();
    }

    public void createMap() {

        tiles = new char[ROWS][COLS];

        // Step 1: Create walls around the maze.
        // Fill the inside with food.

        for (int row = 0; row < ROWS; row++) {

            for (int col = 0; col < COLS; col++) {

                if (row == 0 || row == ROWS - 1
                        || col == 0 || col == COLS - 1) {

                    tiles[row][col] = '#';

                } else {

                    tiles[row][col] = '.';
                }
            }
        }

        // Step 2: Create internal walls.

        int[] wallRows = {4, 8, 12, 16};

        for (int row : wallRows) {

            for (int col = 3; col <= 7; col++) {
                tiles[row][col] = '#';
            }

            for (int col = 11; col <= 15; col++) {
                tiles[row][col] = '#';
            }
        }

        // Step 3: Create the horizontal tunnel.

        for (int col = 0; col < COLS; col++) {

            tiles[10][col] = '.';
        }

        // Step 4: Add power pellets.

        tiles[1][1] = 'o';
        tiles[1][COLS - 2] = 'o';

        tiles[ROWS - 2][1] = 'o';
        tiles[ROWS - 2][COLS - 2] = 'o';

        // Step 5: Clear Pac-Man's starting position.

        tiles[15][9] = ' ';

        // Step 6: Clear ghost starting positions.

        tiles[9][8] = ' ';
        tiles[9][9] = ' ';
        tiles[9][10] = ' ';
        tiles[9][11] = ' ';

        // Step 7: Add a cherry bonus.

        tiles[10][9] = 'c';

        // Step 8: Count all collectible food.

        countFood();
    }

    public void countFood() {

        remainingFood = 0;

        for (int row = 0; row < ROWS; row++) {

            for (int col = 0; col < COLS; col++) {

                if (tiles[row][col] == '.'
                        || tiles[row][col] == 'o') {

                    remainingFood++;
                }
            }
        }
    }

    public boolean isWall(int row, int col) {

        if (row < 0 || row >= ROWS
                || col < 0 || col >= COLS) {

            return true;
        }

        return tiles[row][col] == '#';
    }

    public char getTile(int row, int col) {

        if (row < 0 || row >= ROWS
                || col < 0 || col >= COLS) {

            return '#';
        }

        return tiles[row][col];
    }

    public char consumeAt(int row, int col) {

        char item = getTile(row, col);

        if (item == '.' || item == 'o') {

            remainingFood--;
        }

        if (item == '.' || item == 'o' || item == 'c') {

            tiles[row][col] = ' ';
        }

        return item;
    }

    public int getRemainingFood() {

        return remainingFood;
    }
}
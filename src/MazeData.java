public class MazeData {

    public static final char WALL = '#';
    public static final char ROAD = ' ';

    // row, col
    private int m, n;
    public char[][] maze;

    private int entranceX, entranceY;
    private int exitX, exitY;

    public MazeData(int m, int n) {

        if (m % 2 == 0 || n % 2 == 0) {
            throw new IllegalArgumentException("row and col must be odd.");
        }

        this.m = m;
        this.n = n;
        this.entranceX = 0;
        this.entranceY = 1;
        this.exitX = m - 1;
        this.exitY = n - 2;

        this.maze = new char[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if ( i % 2 == 1 && j % 2 == 1) {
                    maze[i][j] = ROAD;
                } else {
                    maze[i][j] = WALL;
                }
            }
        }

        maze[entranceX][entranceY] = ROAD;
        maze[exitX][exitY] = ROAD;
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public int getEntranceX() {
        return entranceX;
    }

    public int getEntranceY() {
        return entranceY;
    }

    public int getExitX() {
        return exitX;
    }

    public int getExitY() {
        return exitY;
    }
}

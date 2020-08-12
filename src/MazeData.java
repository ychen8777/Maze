public class MazeData {

    public static final char WALL = '#';
    public static final char ROAD = ' ';

    // row, col
    private int m, n;
    public char[][] maze;

    private int entranceRow, entranceCol;
    private int exitRow, exitCol;

    public MazeData(int m, int n) {

        if (m % 2 == 0 || n % 2 == 0) {
            throw new IllegalArgumentException("row and col must be odd.");
        }

        this.m = m;
        this.n = n;
        this.entranceRow = 1;
        this.entranceCol = 0;
        this.exitRow = m - 2;
        this.exitCol = n - 1;

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

        maze[entranceRow][entranceCol] = ROAD;
        maze[exitRow][exitCol] = ROAD;
    }

    public boolean inArea(int row, int col) {
        return row >=0 && row < this.m && col >= 0 && col < this.n;
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public int getEntranceRow() {
        return entranceRow;
    }

    public int getEntranceCol() {
        return entranceCol;
    }

    public int getExitRow() {
        return exitRow;
    }

    public int getExitCol() {
        return exitCol;
    }
}

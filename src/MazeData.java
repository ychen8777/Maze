public class MazeData {

    public static final char WALL = '#';
    public static final char ROAD = ' ';

    // row, col
    private int m, n;
    public char[][] maze;
    public boolean[][] visited;
    public boolean[][] inMist;

    private int entranceRow, entranceCol;
    private int exitRow, exitCol;

    private Player player;

    public MazeData(int m, int n, String level) {

        if (m % 2 == 0 || n % 2 == 0) {
            throw new IllegalArgumentException("row and col must be odd.");
        }

        this.m = m;
        this.n = n;

        this.exitRow = m - 2;
        this.exitCol = n - 1;

        this.maze = new char[m][n];
        this.visited = new boolean[m][n];
        this.inMist = new boolean[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if ( i % 2 == 1 && j % 2 == 1) {
                    maze[i][j] = ROAD;
                } else {
                    maze[i][j] = WALL;
                }
                visited[i][j] = false;
                inMist[i][j] = true;
            }
        }

        // determine starting point
        if (level.equals("Easy")) {
            this.entranceRow = 1;
            this.entranceCol = 0;
        } else {
            this.entranceRow = 0;
            this.entranceCol = 0;
            while (maze[entranceRow][entranceCol] != ROAD) {
                double area = Math.random();
                if (area < 0.45) {
                    this.entranceRow = (int) (Math.random() * m / 2);
                    this.entranceCol = (int) (Math.random() * n / 2);
                } else if (area < 0.667) {
                    this.entranceRow = (int) (Math.random() * m / 3);
                    this.entranceCol = (int) (n/2 + Math.random() * n / 3);
                } else {
                    this.entranceRow = (int) (m/2 + Math.random() * m / 3);
                    this.entranceCol = (int) (Math.random() * n / 3);
                }
            }
        }

        maze[entranceRow][entranceCol] = ROAD;
        maze[exitRow][exitCol] = ROAD;

        this.player = new Player(new Position(entranceRow, entranceCol));
    }

    public MazeData(int m, int n) {
        this(m, n, "Easy");
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

    public Player getPlayer() {
        return this.player;
    }
}

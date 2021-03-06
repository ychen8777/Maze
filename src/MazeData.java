public class MazeData {

    public static final char WALL = '#';
    public static final char ROAD = ' ';

    // row, col
    private int m, n;
    public char[][] maze;
    public boolean[][] visited;
    public boolean[][] inMist;
    public boolean[][] DFSVisited;
    public boolean[][] inDFSPath;
    public boolean[][] BFSVisited;
    public boolean[][] inBFSPath;
    public boolean[][] inWallPath;


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
        this.inDFSPath = new boolean[m][n];
        this.DFSVisited = new boolean[m][n];
        this.inBFSPath = new boolean[m][n];
        this.BFSVisited = new boolean[m][n];
        this.inWallPath = new boolean[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
//                if ( i % 2 == 1 && j % 2 == 1) {
//                    maze[i][j] = ROAD;
//                } else {
//                    maze[i][j] = WALL;
//                }
                if ( i % 4 == 0 || j % 4 == 0) {
                    maze[i][j] = WALL;
                } else {
                    maze[i][j] = ROAD;
                }

                visited[i][j] = false;
                inMist[i][j] = true;
                inDFSPath[i][j] = false;
                DFSVisited[i][j] = false;
                inBFSPath[i][j] = false;
                BFSVisited[i][j] = false;
                inWallPath[i][j] = false;
            }
        }

        // determine starting point
        if (level.equals("Easy")) {
            this.entranceRow = 1;
            this.entranceCol = 0;
        } else {
            this.entranceRow = 0;
            this.entranceCol = 0;
            while (maze[entranceRow][entranceCol] != ROAD ||
                entranceRow % 4 != 2 ||
                entranceCol % 4 != 2) {
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
            this.entranceRow--;
            this.entranceCol--;
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

    // check if player reaches exit
    public boolean playerFinish() {
        return player.getPosition().getRow() == this.exitRow && player.getPosition().getCol() == this.exitCol;
    }

    // make the whole maze visible
    public void openMaze() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                inMist[i][j] = false;
            }
        }
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

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.Delayed;

public class MazeVisualizer {

    private MazeData data;
    private MazeFrame window;
    private PlayerControl playerControl;
    private SolveController solveController;


    private int[][] direction = {{-1,0}, {0, 1}, {1, 0}, {0,-1}};
    private static int DELAY = 5;

    // control panel
    private JButton generateMazeButton;
    private JButton DFSButton;
    private JButton BFSButton;
    private JButton wallButton;
    private JComboBox levelBox;
    private JComboBox mapBox;


    public MazeVisualizer() {
        // default setup when running
        this.data = new MazeData(49, 49);
        this.window = new MazeFrame("Maze Demo");
        this.generateMazeButton = this.window.getGenerateMazeButton();
        this.DFSButton = this.window.getDFSButton();
        this.BFSButton = this.window.getBSFButton();
        this.wallButton = this.window.getWallButton();
        this.levelBox = this.window.getLevelBox();
        this.mapBox = this.window.getMapBox();

        this.window.addKeyListener(new MazeKeyListener());

        this.solveController = new SolveController(this.data);

    }

    public MazeData getData() {
        return data;
    }

    public MazeFrame getWindow() {
        return window;
    }

    public void run() {
//        new Thread(() -> {
        this.window.render(this.data);

        // generate Maze
        this.generateMazeButton.addActionListener((e) -> {
            String level = this.window.getDifficulty();
            String mapOption = this.window.getMapOption();
            generateMaze(level, mapOption);
        });

        // solve maze by DFS
        this.DFSButton.addActionListener((e)->{
            showMaze();
            solveController.refreshDFS();
            new Thread(()-> {
                solveController.solveDFS(data.getEntranceRow(),data.getEntranceCol());
            }).start();
            this.window.requestFocus();
        });

        // solve maze by BFS
        this.BFSButton.addActionListener((e)-> {
            showMaze();
            solveController.refreshBFS();
            new Thread(()-> {
                solveController.solveBFS();
            }).start();
            this.window.requestFocus();
        });

        // solve maze by wall follower
        this.wallButton.addActionListener((e)-> {
            showMaze();
            solveController.refreshWall();
            new Thread(()-> {
                solveController.wallFollower(data.getEntranceRow(), data.getEntranceCol(), "right");
            }).start();
            this.window.requestFocus();
        });

//        }).start();
    }



    // to generate new maze after generateMazeButton is clicked
    public void generateMaze(String level, String mapOption) {
        int col = this.window.getInputWidth();
        int row = this.window.getInputHeight();
        this.data = new MazeData(4*row+1, 4*col+1, level);
        //System.out.println("row: " + col + ", height: " + row);
        this.window.resizeWindow((4*col+1)*this.window.getBlockSize()+16, (4*row+1)*this.window.getBlockSize()+120+26);
        this.window.render(this.data);
        this.solveController.stopAll();

        new Thread(() -> {
            generateHelper(level, mapOption);
            //new solveController(data).wallFollower(data.getEntranceRow(), data.getEntranceCol(), "right");
        }).start();


        // link player with playerControl
        this.playerControl = new PlayerControl(this.data);
        this.window.requestFocus();

        //this.window.render(this.data);
    }

    public void generateHelper(String level, String mapOption) {

        setToRoad(-1, -1);

        //MazeQueue<Position> queue = new OrderedQueue<>();
        //MazeQueue<Position> queue = new RandomQueueEasy<>();
        MazeQueue<Position> queue = setupQueue(level);
        Position start = setupStart(level);
        playerControl.openAround(this.data.getEntranceRow(), this.data.getEntranceCol());

        this.data.visited[start.getRow()][start.getCol()] = true;
        queue.add(start);

        while (!queue.isEmpty()) {
            Position cur = queue.remove();

//            if (cur.getRow() == this.data.getExitRow() && cur.getCol() == this.data.getExitCol()) {
//                return;
//            }

            if (mapOption == "on") {
                for (int i = -2; i <= 2; i++) {
                    for (int j = -2; j <= 2; j++) {
                        int newRow = cur.getRow() + i;
                        int newCol = cur.getCol() + j;
                        if (this.data.inArea(newRow, newCol)) {
                            this.data.inMist[newRow][newCol] = false;
                        }
                    }
                }
            }

            for (int i = 0; i < 4; i++) {
                int newRow = cur.getRow() + this.direction[i][0] * 4;
                int newCol = cur.getCol() + this.direction[i][1] * 4;

                if (this.data.inArea(newRow, newCol)
                    && !this.data.visited[newRow][newCol]
                    && this.data.maze[newRow][newCol] == MazeData.ROAD) {

                    queue.add(new Position(newRow, newCol));
                    this.data.visited[newRow][newCol] = true;

                    // break the wall between cur and (newRow, newCol)
                    breakWall(cur.getRow(), cur.getCol(), i);


//                     int rand = (int) (Math.random() * 3) - 1;
//
//                     if (this.direction[i][0] == 0) {
//                         setToRoad(cur.getRow() + this.direction[i][0] + rand, cur.getCol() + this.direction[i][1]*2);
//                     } else {
//                         setToRoad(cur.getRow() + this.direction[i][0] * 2, cur.getCol() + this.direction[i][1] + rand);
//                     }

                }
            }
        }

        playerControl.openAround(this.data.getExitRow(), this.data.getExitCol());

        setToRoad(-1, -1);
    }

    // break the wall between two cells
    public void breakWall(int row, int col, int i) {
        double numWalls = Math.random();
        if (numWalls < 0.4) {
            // break one cell
            int rand = (int) (Math.random() * 3) - 1;

            if (this.direction[i][0] == 0) {
                setToRoad(row + this.direction[i][0] + rand, col + this.direction[i][1]*2);
            } else {
                setToRoad(row + this.direction[i][0] * 2, col + this.direction[i][1] + rand);
            }
        } else if (numWalls < 0.8) {
            // break two cells
            double rand = Math.random();
            if (rand < 0.5) {
                if (this.direction[i][0] == 0) {
                    setToRoad(row + this.direction[i][0], col + this.direction[i][1]*2);
                    setToRoad(row + this.direction[i][0]-1, col + this.direction[i][1]*2);
                } else {
                    setToRoad(row + this.direction[i][0] * 2, col + this.direction[i][1]);
                    setToRoad(row + this.direction[i][0] * 2, col + this.direction[i][1]-1);
                }
            } else {
                if (this.direction[i][0] == 0) {
                    setToRoad(row + this.direction[i][0], col + this.direction[i][1]*2);
                    setToRoad(row + this.direction[i][0]+1, col + this.direction[i][1]*2);
                } else {
                    setToRoad(row + this.direction[i][0] * 2, col + this.direction[i][1]);
                    setToRoad(row + this.direction[i][0] * 2, col + this.direction[i][1] + 1);
                }
            }
        } else {
            // break all 3 cells
            if (this.direction[i][0] == 0) {
                setToRoad(row + this.direction[i][0]+1, col + this.direction[i][1]*2);
                setToRoad(row + this.direction[i][0], col + this.direction[i][1]*2);
                setToRoad(row + this.direction[i][0]-1, col + this.direction[i][1]*2);
            } else {
                setToRoad(row + this.direction[i][0] * 2, col + this.direction[i][1]);
                setToRoad(row + this.direction[i][0] * 2, col + this.direction[i][1] - 1);
                setToRoad(row + this.direction[i][0] * 2, col + this.direction[i][1] + 1);
            }
        }

    }

    //  Listener for player control by keyboard
    private class MazeKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent event) {
            //System.out.println("key detected");
            int keyCode = event.getKeyCode();
            switch (keyCode) {
                case KeyEvent.VK_UP:
                    playerControl.moveUp();
                    break;
                case KeyEvent.VK_DOWN:
                    playerControl.moveDown();
                    break;
                case KeyEvent.VK_LEFT:
                    playerControl.moveLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    playerControl.moveRight();
                    break;
            }
            window.render(data);

            // if the player reaches exit, show success window
            if (data.playerFinish()) {
                window.showSuccess();
            }
        }
    }

    //
    private class SolveController{
        private MazeData mazeData;
        private boolean DFSSwitch;
        private boolean BFSSwitch;
        private boolean wallSwitch;

        public SolveController(MazeData data) {
            this.mazeData = data;
        }

        public void setMazeData(MazeData data) {
            this.mazeData=data;
        }

        public void stopAll() {
            this.DFSSwitch = false;
            this.BFSSwitch = false;
            this.wallSwitch = false;
        }

        public void refreshDFS() {
            this.DFSSwitch = true;
        }

        public void refreshBFS() {
            this.BFSSwitch = true;
        }

        public void refreshWall() {
            this.wallSwitch = true;
        }

        public void refreshAll() {
            this.DFSSwitch = true;
            this.BFSSwitch = true;
            this.wallSwitch = true;
        }

        public boolean solveDFS(int row, int col){
            if (!data.inArea(row, col)) {
                throw new IllegalArgumentException(String.format("input out of maze bound for inArea({}, {})", row, col));
            }

            if (!this.DFSSwitch) {
                return false;
            }

            data.DFSVisited[row][col] = true;
            setPathData(row, col, "dfs", true);

            if (row == data.getExitRow() && col == data.getExitCol()) {
                return true;
            }

            for (int i = 0; i < 4; i++) {
                int nextRow = row + direction[i][0];
                int nextCol = col + direction[i][1];
                if (data.inArea(nextRow,nextCol) &&
                    !data.DFSVisited[nextRow][nextCol] &&
                    data.maze[nextRow][nextCol] == data.ROAD) {
                    if (solveDFS(nextRow, nextCol)) {
                        return true;
                    }
                }
            }

            setPathData(row, col, "dfs", false);
            return false;
        }

        public void solveBFS() {
            Queue<Position> queue = new LinkedList<>();
            queue.add(new Position(data.getEntranceRow(),data.getEntranceCol()));
            solveBFSHelp(queue);
        }


        public boolean solveBFSHelp(Queue<Position> queue) {
            if (queue.isEmpty() || !this.BFSSwitch) {
                return false;
            }

            Position curPos = queue.remove();
            int curRow = curPos.getRow();
            int curCol = curPos.getCol();
            pathTo(curPos, 1);
            window.render(data);
            MazeVisHelper.pause(DELAY*6);
            //setPathData(curRow, curCol, "bfs", true);

            if (curRow == data.getExitRow() && curCol == data.getExitCol()) {
                return true;
            }

            for (int i = 0; i < 4; i++) {
                int newRow = curRow + direction[i][0];
                int newCol = curCol + direction[i][1];
                if (data.inArea(newRow, newCol) &&
                        !data.BFSVisited[newRow][newCol] &&
                        data.maze[newRow][newCol] == data.ROAD) {
                    queue.add(new Position(newRow, newCol, curPos));
                    data.BFSVisited[newRow][newCol] = true;
                }
            }

            //setPathData(curRow, curCol, "bfs", false);
            pathTo(curPos, 0);
            return solveBFSHelp(queue);
        }

        public void pathTo(Position curPos, int adding) {
            Stack<Position> stack = new Stack<>();
            while(curPos != null) {
                stack.add(curPos);
                curPos = curPos.getPrevPos();
            }
            while(!stack.isEmpty()) {
                curPos = stack.pop();
                int curRow = curPos.getRow();
                int curCol = curPos.getCol();
                if (adding == 1) {
                    setPathData(curRow, curCol, "bfs", true);
                } else {
                    setPathData(curRow, curCol, "bfs", false);
                }
            }
        }
        public void wallFollower(int row, int col, String prevDirection) {
            setPathData(row, col, "wall", true);

            if (!wallSwitch) {
                return;
            }

            if (row == data.getExitRow() && col == data.getExitCol()) {
                return;
            }
            if (prevDirection == "up") {
                if (wallGoLeft(row, col)){}
                else if (wallGoUp(row, col)) {}
                else if (wallGoRight(row, col)) {}
                else {
                    wallGoDown(row, col);
                }
//                if (data.inArea(row, col-1) &&
//                        data.maze[row][col-1] == data.ROAD) {
//                    wallFollower(row, col-1, "left");
//                } else if (data.inArea(row-1, col) &&
//                        data.maze[row-1][col] == data.ROAD) {
//                    wallFollower(row-1, col, "up");
//                } else if (data.inArea(row, col+1) &&
//                        data.maze[row][col+1] == data.ROAD) {
//                    wallFollower(row, col+1, "right");
//                } else {
//                    wallFollower(row+1, col, "down");
//                }
            } else if (prevDirection == "right") {
                if (wallGoUp(row, col)) {
                } else if (wallGoRight(row, col)) {
                } else if (wallGoDown(row, col)) {
                } else {
                    wallGoLeft(row, col);
                }

                //if (data.inArea(row-1, col) &&
//                        data.maze[row-1][col] == data.ROAD) {
//                    wallFollower(row - 1, col, "up");
//                } else if (data.inArea(row, col+1) &&
//                        data.maze[row][col+1] == data.ROAD) {
//                    wallFollower(row, col + 1, "right");
//                } else if (data.inArea(row+1, col) &&
//                        data.maze[row+1][col] == data.ROAD) {
//                    wallFollower(row+1, col , "down");
//                } else {
//                    wallFollower(row, col-1, "left");

            } else if (prevDirection == "down") {
                if (wallGoRight(row, col)) {
                } else if (wallGoDown(row, col)) {
                } else if (wallGoLeft(row, col)) {
                } else {
                    wallGoUp(row, col);
                }
            } else {
                if (wallGoDown(row, col)) {
                } else if (wallGoLeft(row, col)) {
                } else if (wallGoUp(row, col)) {
                } else {
                    wallGoRight(row, col);
                }
            }
        }

        public boolean wallGoUp(int row, int col) {
            if (data.inArea(row-1, col) &&
                    data.maze[row-1][col] == data.ROAD) {
                wallFollower(row - 1, col, "up");
                return true;
            } else {
                return false;
            }
        }

        public boolean wallGoLeft(int row, int col) {
            if (data.inArea(row, col-1) &&
                    data.maze[row][col-1] == data.ROAD) {
                wallFollower(row, col-1, "left");
                return true;
            } else {
                return false;
            }
        }

        public boolean wallGoDown(int row, int col) {
            if (data.inArea(row+1, col) &&
                    data.maze[row+1][col] == data.ROAD) {
                wallFollower(row+1, col, "down");
                return true;
            } else {
                return false;
            }
        }

        public boolean wallGoRight(int row, int col) {
            if (data.inArea(row, col+1) &&
                    data.maze[row][col+1] == data.ROAD) {
                wallFollower(row, col+1, "right");
                return true;
            } else {
                return false;
            }
        }


        public void setPathData(int row, int col, String method, boolean inPath){
            if (method == "dfs") {
                data.inDFSPath[row][col] = inPath;
                window.render(data);
                MazeVisHelper.pause(DELAY*2);
            } else if (method == "bfs") {
                data.inBFSPath[row][col] = inPath;
                //window.render(data);
                //MazeVisHelper.pause(DELAY/3);
            } else {
                data.inWallPath[row][col] = inPath;
                window.render(data);
                MazeVisHelper.pause(DELAY*5);

            }

        }

    }



    public void setToRoad(int row, int col) {
        if (this.data.inArea(row, col)) {
            this.data.maze[row][col] = MazeData.ROAD;
        }

        this.window.render((this.data));
        MazeVisHelper.pause(DELAY);

    }

    //determine which MazeQueue to use based on difficulty level
    public MazeQueue<Position> setupQueue(String level) {
        if (level.equals("Easy")) {
            return new RandomQueueEasy<Position>();
        } else {
            return new RandomQueueHard<Position>();
        }
    }

    //determine starting point based on difficulty level
    public Position setupStart(String level) {
        if (level.equals("Easy")) {
            return new Position(this.data.getEntranceRow() + 1, this.data.getEntranceCol() + 2);
        } else {
            return new Position(this.data.getEntranceRow() + 1, this.data.getEntranceCol() + 1);
        }
    }

    // make the whole maze visible
    public void showMaze() {
        if (this.window.getMapOption() == "off") {
            this.mapBox.setSelectedItem("on");
            this.data.openMaze();
        }
    }

}

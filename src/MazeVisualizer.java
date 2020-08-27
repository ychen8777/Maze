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

    private int[][] direction = {{-1,0}, {0, 1}, {1, 0}, {0,-1}};
    private static int DELAY = 5;

    // control panel
    private JButton generateMazeButton;
    private JButton DFSButton;
    private JButton BFSButton;
    private JComboBox levelBox;
    private JComboBox mapBox;


    public MazeVisualizer() {
        // default setup when running
        this.data = new MazeData(49, 49);
        this.window = new MazeFrame("Maze Demo");
        this.generateMazeButton = this.window.getGenerateMazeButton();
        this.DFSButton = this.window.getDFSButton();
        this.BFSButton = this.window.getBSFButton();
        this.levelBox = this.window.getLevelBox();
        this.mapBox = this.window.getMapBox();

        this.window.addKeyListener(new MazeKeyListener());

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
            new Thread(()-> {
                new solveController(data).solveDFS(data.getEntranceRow(),data.getEntranceCol());
            }).start();
            this.window.requestFocus();
        });

        // solve maze by BFS
        this.BFSButton.addActionListener((e)-> {
            showMaze();
            new Thread(()-> {
                new solveController(data).solveBFS();
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
    private class solveController{
        private MazeData mazeData;

        public solveController(MazeData data) {
            this.mazeData = data;
        }

        public boolean solveDFS(int row, int col){
            if (!data.inArea(row, col)) {
                throw new IllegalArgumentException(String.format("input out of maze bound for inArea({}, {})", row, col));
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
            if (queue.isEmpty()) {
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

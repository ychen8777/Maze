import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
        this.data = new MazeData(45, 45);
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
            this.generateMazeButton.addActionListener((e) -> {
                String level = this.window.getDifficulty();
                generateMaze(level);
            });
//        }).start();
    }



    // to generate new maze after generateMazeButton is clicked
    public void generateMaze(String level) {
        int col = this.window.getInputWidth();
        int row = this.window.getInputHeight();
        this.data = new MazeData(row, col, level);
        //System.out.println("row: " + col + ", height: " + row);
        this.window.resizeWindow(col*this.window.getBlockSize()+16, row*this.window.getBlockSize()+120+20);
        this.window.render(this.data);

        new Thread(() -> {
            generateHelper(level);
        }).start();

        // link player with playerControl
        this.playerControl = new PlayerControl(this.data);
        this.window.requestFocus();

        //this.window.render(this.data);
    }

    public void generateHelper(String level) {

        setToRoad(-1, -1);

        //MazeQueue<Position> queue = new OrderedQueue<>();
        //MazeQueue<Position> queue = new RandomQueueEasy<>();
        MazeQueue<Position> queue = setupQueue(level);
        Position start = setupStart(level);

        this.data.visited[start.getRow()][start.getCol()] = true;
        queue.add(start);

        while (!queue.isEmpty()) {
            Position cur = queue.remove();

//            if (cur.getRow() == this.data.getExitRow() && cur.getCol() == this.data.getExitCol()) {
//                return;
//            }

            for (int i = 0; i < 4; i++) {
                int newRow = cur.getRow() + this.direction[i][0] * 2;
                int newCol = cur.getCol() + this.direction[i][1] * 2;

                if (this.data.inArea(newRow, newCol)
                    && !this.data.visited[newRow][newCol]
                    && this.data.maze[newRow][newCol] == MazeData.ROAD) {

                    queue.add(new Position(newRow, newCol));
                    this.data.visited[newRow][newCol] = true;
                    // break the wall between cur and (newRow, newCol)
                    setToRoad(cur.getRow() + this.direction[i][0], cur.getCol() + this.direction[i][1]);
                }
            }
        }

        setToRoad(-1, -1);
    }

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
            return new Position(this.data.getEntranceRow(), this.data.getEntranceCol() + 1);
        } else {
            return new Position(this.data.getEntranceRow(), this.data.getEntranceCol());
        }
    }

}

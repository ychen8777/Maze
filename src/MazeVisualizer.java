import javax.swing.*;

public class MazeVisualizer {

    private MazeData data;
    private MazeFrame window;
    private JButton generateMazeButton;
    private JButton DFSButton;
    private JButton BFSButton;
    private int[][] direction = {{-1,0}, {0, 1}, {1, 0}, {0,-1}};
    private static int DELAY = 5;


    public MazeVisualizer() {
        // default setup when running
        this.data = new MazeData(75, 101);
        this.window = new MazeFrame("Maze Demo");
        this.generateMazeButton = this.window.getGenerateMazeButton();
        this.DFSButton = this.window.getDFSButton();
        this.BFSButton = this.window.getBSFButton();
    }

    public MazeData getData() {
        return data;
    }

    public MazeFrame getWindow() {
        return window;
    }

    public void run() {
        new Thread(() -> {
            this.window.render(this.data);
            this.generateMazeButton.addActionListener((e) -> {
                generateMaze();
            });
        }).start();
    }



    // to generate new maze after generateMazeButton is clicked
    public void generateMaze() {
        int col = this.window.getInputWidth();
        int row = this.window.getInputHeight();
        this.data = new MazeData(row, col);
        //System.out.println("row: " + col + ", height: " + row);
        this.window.resizeWindow(col*this.window.getBlockSize()+16, row*this.window.getBlockSize()+120+20);
        this.window.render(this.data);
        generateHelper();
        this.window.render(this.data);

    }

    public void generateHelper() {

        setToRoad(-1, -1);

        MazeQueue<Position> queue = new OrderedQueue<>();
        Position start = new Position(this.data.getEntranceRow(), this.data.getEntranceCol() + 1);
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


    public void setToRoad(int row, int col) {
        if (this.data.inArea(row, col)) {
            this.data.maze[row][col] = MazeData.ROAD;
        }

        //this.window.render((this.data));
        //MazeVisHelper.pause(DELAY);

    }

}

import javax.swing.*;

public class MazeVisualizer {

    private MazeData data;
    private MazeFrame window;
    private JButton generateMazeButton;
    private JButton DFSButton;
    private JButton BFSButton;

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

    public void generateMaze() {
        int col = this.window.getInputWidth();
        int row = this.window.getInputHeight();
        this.data = new MazeData(row, col);
        //System.out.println("row: " + col + ", height: " + row);
        this.window.resizeWindow(col*this.window.getBlockSize()+16, row*this.window.getBlockSize()+120+20);
        this.window.render(this.data);

    }

}

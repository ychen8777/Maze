import javax.swing.*;

public class MazeVisualizer {

    private MazeData data;
    private MazeFrame window;

    public MazeVisualizer() {
        // default setup when running
        this.data = new MazeData(75, 101);
        this.window = new MazeFrame("Maze Demo");

        JButton generateMazeButton = this.window.getGenerateMazeButton();
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
        }).start();
    }
}

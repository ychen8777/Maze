import javax.swing.*;

public class MazeFrame extends JFrame {

    private int mazeHeight;
    private int mazeWidth;

    public MazeFrame(String title, int mazeWidth, int mazeHeight) {
        super(title);
        this.mazeHeight = mazeHeight;
        this.mazeWidth = mazeWidth;


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

    }

    public MazeFrame(String title) {
        this(title, 1024, 768 );
    }

}

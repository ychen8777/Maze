import javax.swing.*;

public class MazeFrame extends JFrame {

    private int mazeHeight;
    private int mazeWidth;

    public MazeFrame(String title, int mazeWidth, int mazeHeight) {
        super(title);
        this.mazeHeight = mazeHeight;
        this.mazeWidth = mazeWidth;

        MazeControl mazeControl = new MazeControl();
        setContentPane(mazeControl);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

    }

    public MazeFrame(String title) {
        this(title, 1024, 768 );
    }

    private class MazeControl extends JPanel {
        private JLabel widthLabel = new JLabel("Width: ");
        private JTextField widthField = new JTextField("640");
        private JLabel heightLabel = new JLabel("Height:");
        private JTextField heightField = new JTextField("480");
        private JButton generateMazeButton = new JButton("Generate Maze");
        private JButton DFSButton = new JButton("Solve by DFS");
        private JButton BSFButton = new JButton("Solve by BFS");

        public MazeControl(){
            this.add(widthLabel);
            this.add(widthField);
            this.add(heightLabel);
            this.add(heightField);
            this.add(generateMazeButton);
            this.add(DFSButton);
            this.add(BSFButton);


        }
    }

}

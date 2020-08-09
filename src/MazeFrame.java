import javax.swing.*;

public class MazeFrame extends JFrame {

    private int mazeHeight;
    private int mazeWidth;
    private MazeControl mazeControl;

    public MazeFrame(String title, int mazeWidth, int mazeHeight) {
        super(title);
        this.mazeHeight = mazeHeight;
        this.mazeWidth = mazeWidth;

        this.mazeControl = new MazeControl();
        setContentPane(mazeControl);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

    }

    public MazeFrame(String title) {
        this(title, 1024, 768 );
    }

    public int getInputWidth() {
        return Integer.parseInt(this.mazeControl.getWidthInput());
    }

    public int getInputHeight() {
        return Integer.parseInt(this.mazeControl.getHeightInput());
    }

    public JButton getGenerateMazeButton() {
        return this.mazeControl.getGenerateMazeButton();
    }

    public JButton getDFSButton() {
        return this.mazeControl.getDFSButton();
    }

    public JButton getBSFButton() {
        return this.mazeControl.getBSFButton();
    }




    private class MazeControl extends JPanel {
        private JLabel widthLabel = new JLabel("Width: ");
        private JTextField widthField = new JTextField("103");
        private JLabel heightLabel = new JLabel("Height:");
        private JTextField heightField = new JTextField("77");
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

        public String getWidthInput() {
            return this.widthField.getText();
        }

        public String getHeightInput() {
            return this.heightField.getText();
        }
        public JButton getGenerateMazeButton() {
            return this.generateMazeButton;
        }

        public JButton getDFSButton() {
            return this.DFSButton;
        }

        public JButton getBSFButton() {
            return this.BSFButton;
        }
    }

}

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MazeFrame extends JFrame {

    private int blockSize = 9;
    private int windowHeight;
    private int windowWidth;
    private MazeControl mazeControl;
    private MazeCanvas mazeCanvas;
    private MazeData mazeData;

    public MazeFrame(String title, int windowWidth, int windowHeight) {
        super(title);
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
        this.setSize(windowWidth, windowHeight);

        this.mazeControl = new MazeControl();
        add(mazeControl, BorderLayout.NORTH);
        this.mazeCanvas = new MazeCanvas();
        // JButton testB = new JButton("Test");
        // mazeCanvas.add(testB);
        add(mazeCanvas, BorderLayout.SOUTH);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        //System.out.println(mazeControl.getHeight());

    }

    public MazeFrame(String title) {
        this(title, 9*101, 75*9+40);
    }

    public void render(MazeData data) {
        this.mazeData = data;
        repaint();
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
        private JTextField widthField = new JTextField("101");
        private JLabel heightLabel = new JLabel("Height:");
        private JTextField heightField = new JTextField("75");
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

    private class MazeCanvas extends JPanel {


        public MazeCanvas() {
            //double buffer
            super(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D)g;

            for (int i = 0; i < mazeData.getM(); i ++) {
                for (int j = 0; j < mazeData.getN(); j ++) {
                    if (mazeData.maze[i][j] == MazeData.WALL) {
                        // set wall color to light blue
                        MazeVisHelper.setColor(g2d, MazeVisHelper.LightBlue);
                    } else { // set wall color to white
                        MazeVisHelper.setColor(g2d, MazeVisHelper.White);
                    }
                    // draw the rectangle
                    MazeVisHelper.fillRectangle(g2d, i*blockSize, j*blockSize, blockSize, blockSize);
                }
            }
        }

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(windowWidth, windowHeight - 40);
        }

    }

}

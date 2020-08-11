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

        // control panel width 351, height 101
        this.mazeControl = new MazeControl();
        add(mazeControl, BorderLayout.NORTH);
        this.mazeCanvas = new MazeCanvas();
        add(mazeCanvas, BorderLayout.SOUTH);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
//        System.out.println("Control height:");
//        System.out.println(mazeControl.getHeight());
//        System.out.println("Control width:");
//        System.out.println(mazeControl.getWidth());

    }

    public MazeFrame(String title) {
        this(title, 9*101, 75*9+101);
    }

    public void render(MazeData data) {
        this.mazeData = data;
        repaint();
    }

    public void resizeWindow(int width, int height){
        if (width <= 351 && height <= 101) {
            this.windowWidth = 351;
            this.windowHeight = 130;
        }
        else if (width <= 351) {
            this.windowWidth = 351;
            this.windowHeight = height;
        } else if (height < 101) {
            this.windowWidth = width;
            this.windowHeight = 130;
        } else {
            this.windowWidth = width;
            this.windowHeight = height;
        }
        this.setSize(windowWidth, windowHeight);
        this.remove(mazeControl);
        this.remove(mazeCanvas);
        this.add(mazeControl, BorderLayout.NORTH);
        this.add(mazeCanvas, BorderLayout.SOUTH);
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

    public int getBlockSize() {
        return blockSize;
    }

    private class MazeControl extends JPanel {
        private JLabel widthLabel = new JLabel("Width: ");
        private JTextField widthField = new JTextField("101");
        private JLabel heightLabel = new JLabel("Height:");
        private JTextField heightField = new JTextField("75");
        private JButton generateMazeButton = new JButton("Generate Maze");
        private JButton DFSButton = new JButton("Solve by DFS");
        private JButton BSFButton = new JButton("Solve by BFS");
        private JLabel levelText = new JLabel("Difficulty: ");
        private JComboBox levelBox;
        private JLabel mapText = new JLabel("Open Map: ");
        private JComboBox mapBox;


        public MazeControl(){
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JPanel control1 = new JPanel();
            control1.add(widthLabel);
            widthField.setColumns(4);
            widthField.setHorizontalAlignment(JTextField.CENTER);
            control1.add(widthField);
            control1.add(heightLabel);
            heightField.setColumns(4);
            heightField.setHorizontalAlignment(JTextField.CENTER);
            control1.add(heightField);
            this.add(control1);

            JPanel control3 = new JPanel();
            control3.add(generateMazeButton);
            control3.add(DFSButton);
            control3.add(BSFButton);
            this.add(control3);

            JPanel control2 = new JPanel();
            String[] levels = {"Easy", "Hard"};
            this.levelBox = new JComboBox(levels);
            String[] mapChoice = {"on", "off"};
            this.mapBox = new JComboBox(mapChoice);
            control2.add(levelText);
            control2.add(levelBox);
            control2.add(mapText);
            control2.add(mapBox);
            this.add(control2);

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
                    MazeVisHelper.fillRectangle(g2d, j*blockSize, i*blockSize, blockSize, blockSize);

                }
            }
        }

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(windowWidth, windowHeight-101);
        }

    }

}

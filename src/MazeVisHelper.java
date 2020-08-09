import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MazeVisHelper {

    private MazeVisHelper() {} // no instance

    public static final Color Red = new Color(0xF44336);
    public static final Color Blue = new Color(0x2196F3);
    public static final Color LightBlue = new Color(0x03A9F4);
    public static final Color Green = new Color(0x4CAF50);
    public static final Color Black = new Color(0x000000);
    public static final Color White = new Color(0xFFFFFF);

    public static void fillRectangle(Graphics2D g, int x, int y, int width, int height) {
        Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
        g.draw(rect);
    }

    public static void setColor(Graphics2D g, Color color) {
        g.setColor(color);
    }




}

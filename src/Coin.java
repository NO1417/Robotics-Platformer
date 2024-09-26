import java.awt.*;

public class Coin extends Rect {
    public Coin(int x, int y, int width, int height, Color color) {
        super(x, y, width, height);
    }

    public void render(Graphics g, int cameraX, int cameraY) {
        g.setColor(this.color);
        g.fillOval(x - cameraX, y - cameraY, width, height);
    }
}

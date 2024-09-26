import java.awt.*;

public class Player extends Rect {
    public Velocity2d velocity;

    public Player(int x, int y, int width, int height, double vx, double vy, Color color) {
        super(x, y, width, height, color);
        this.velocity = new Velocity2d(vx, vy);
    }
    

    
}

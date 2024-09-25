import java.awt.*;

public class Rect {
    public int x;
    public int y;
    public int width;
    public int height;
    public Color color;

    public Rect(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public boolean collidesWithRect(Rect rect) {
        return (this.x < rect.x) && (this.x+this.width > rect.x) && (this.y < rect.y) && (this.y+this.height > rect.y);
    }

    public void setLocation(int newX, int newY) {
        x = newX;
        y = newY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

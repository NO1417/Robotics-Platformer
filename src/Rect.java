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
        int RectAX1 = rect.x;
        int RectAX2 = rect.x + rect.width;
        int RectAY1 = rect.y;
        int RectAY2 = rect.y + rect.height;

        int RectBX1 = this.x;
        int RectBX2 = this.x + this.width;
        int RectBY1 = this.y;
        int RectBY2 = this.y + this.height;
        return (RectAX1 < RectBX2 && RectAX2 > RectBX1 &&
                RectAY1 < RectBY2 && RectAY2 > RectBY1);
    }

    public boolean containsRect(Rect rect) {
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

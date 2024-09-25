public class Rect {
    public int x;
    public int y;
    public int width;
    public int height;

    public Rect(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean collidesWithRect(Rect rect) {
        return (this.x < rect.x) && (this.x+this.width > rect.x) && (this.y < rect.y) && (this.y+this.height > rect.y);
    }
}

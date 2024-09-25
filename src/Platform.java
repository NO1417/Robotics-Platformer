public class Platform extends Rect {

    public Platform(int x, int y, int width, int height) {
        super(x,y,width,height);
    }

    public boolean collidesWithRect(Rect rect) {
        return (this.x < rect.x) && (this.x+this.width > rect.x) && (this.y < rect.y) && (this.y+this.height > rect.y);
    }
}

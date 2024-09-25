public class FollowingFrame extends JFrame {
    private final Player player;

    public FollowingFrame(Player player) {
        this.player = player;
        setTitle("Platformer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
    }

    private void updatePosition() {
        int x = player.getX();
        int y = player.getY();

        setLocation(x, y);
    }

    
}
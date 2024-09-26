import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Platformer extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private int playerX, playerY;
    private int cameraX, cameraY;
    private int playerWidth, playerHeight;
    private int velocityX, velocityY;
    private boolean onGround;
    private final int gravity = 1;
    private final int moveSpeed = 5;
    private final int jumpStrength = 15;
    private final int worldWidth = 2000;
    private final int worldHeight = 600;
    private static final int frameWidth = 800;
    private static final int frameHeight = 600;

    public JFrame frame;

    private Player player;

    public ArrayList<Rect> platformList = new ArrayList<>();
    public ArrayList<Rect> renderList = new ArrayList<>();

    public Platformer(JFrame frame) {
        timer = new Timer(20, this);
        timer.start();
        playerX = 50;
        playerY = 500;
        playerWidth = 50;
        playerHeight = 50;
        velocityX = 0;
        velocityY = 0;
        onGround = true;

        this.frame = frame;

        this.player = new Player(100, 500, 50, 50, 0.0, 0.0, Color.RED);

        Rect obstacle = new Rect(600, 500, 100, 50, Color.GREEN);
        this.addObstacle(obstacle);

        Platform floor = new Platform(0, worldHeight-50, worldWidth, 50, Color.GREEN);
        this.addObstacle(floor);

        setFocusable(true);
        addKeyListener(this);
    }

    public void addObstacle(Rect obj) {
        this.platformList.add(obj);
        this.renderList.add(obj);
    }


    public void addRenderableObject(Rect obj) {
        this.renderList.add(obj);
    }

    public int getWinWidth() {
        return frame.getWidth();
    }

    public int getWinHeight() {
        return frame.getHeight();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.CYAN);
        g.fillRect(0, 0, worldWidth, worldHeight); // Background

        for ( Rect rect : this.renderList ) {
            g.setColor(rect.color);
            g.fillRect(rect.x - cameraX, rect.y - cameraY, rect.width, rect.height);
        }

        g.setColor(player.color);
        g.fillRect(playerX - cameraX, playerY - cameraY, player.width, player.height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    private void update() {
        playerX += velocityX;
        playerY += velocityY;

        // Camera logic
        cameraX = playerX - frameWidth / 2;
        cameraY = playerY - frameHeight / 2;

        if (cameraX < 0) cameraX = 0;
        if (cameraY < 0) cameraY = 0;
        if (cameraX > worldWidth - frameWidth) cameraX = worldWidth - frameWidth;
        if (cameraY > worldHeight - frameHeight) cameraY = worldHeight - frameHeight;

        boolean platformCollision = false;
        
        for (Rect platform : platformList) {
            if (playerX + playerWidth > platform.x && playerX < platform.x + platform.width) {
                if (playerY + playerHeight <= platform.y && playerY + playerHeight + velocityY >= platform.y) {
                    playerY = platform.y - playerHeight;
                    velocityY = 0;
                    onGround = true;
                    platformCollision = true;
                    break;
                }
            }
        }

        if (!platformCollision) {
            if (playerY + playerHeight >= 550) { // Ground collision
                playerY = 500;
                velocityY = 0;
                onGround = true;
    
            } else {
                velocityY += gravity;
                onGround = false;
            }
        }
        
        if (playerX + playerWidth > worldWidth) {
            playerX = worldWidth - playerWidth; // Bind to right edge
            velocityX = 0;
        }

        if (playerX < 0) {
            playerX = 0;
        }
        
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            velocityX = -moveSpeed;
        }

        if (key == KeyEvent.VK_RIGHT) {
            velocityX = moveSpeed;
        }

        if (key == KeyEvent.VK_SPACE && onGround) {
            velocityY = -jumpStrength;
            onGround = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            velocityX = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main() {
        JFrame frame = new JFrame("Simple Platformer");
        frame.setSize(frameWidth,frameHeight);
        frame.setLocationRelativeTo(null);

        Platformer game = new Platformer(frame);
        frame.add(game);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}

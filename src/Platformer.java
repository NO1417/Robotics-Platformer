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
    private final int worldHeight = 1000;
    private final int frameWidth = 800;
    private final int frameHeight = 600;

    public JFrame frame;

    private Player player;

    public ArrayList<Rect> platformList = new ArrayList();
    public ArrayList<Rect> renderList = new ArrayList();


    public Platformer(JFrame frame) {
        timer = new Timer(20, this);
        timer.start();
        playerX = 50;
        playerY = 450;
        playerWidth = 50;
        playerHeight = 50;
        velocityX = 0;
        velocityY = 0;
        onGround = true;

        this.frame = frame;

        this.player = new Player(50, 450, 50, 50, 0.0, 0.0, Color.RED);
        this.addRenderableObject(player);

        //Rect obstacle = new Rect(600, 500, 100, 50, Color.GREEN);

        Platform floor = new Platform(0, getWinHeight()-50, getWinWidth(), 50, Color.GREEN);
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
        
        cameraX = playerX - frameWidth / 2;
        cameraY = playerY - frameHeight / 2;

        if (cameraX < 0) cameraX = 0;
        if (cameraY < 0) cameraY = 0;
        if (cameraX > worldWidth - frameWidth) cameraX = worldWidth - frameWidth;
        if (cameray > worldHeight - frameHeight) cameraY = worldHeight - frameHeight;
        
        g.setColor(Color.CYAN);
        g.fillRect(0 - cameraX, 0 - cameraY, worldWidth, worldHeight); // Background

        for ( Rect rect : this.renderList ) {
            g.setColor(rect.color);
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
        
        /*
        g.setColor(Color.GREEN);
        g.fillRect(0, 500, getWidth(), 100); // Ground
        g.fillRect(300, 480, 60, 20);
        g.setColor(Color.RED);
        g.fillRect(playerX, playerY, playerWidth, playerHeight); // Player
        */
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    private void update() {
        playerX += velocityX;
        playerY += velocityY;

        if (playerY + playerHeight >= 350) { // Ground collision
            playerY = 350 - playerHeight;
            velocityY = 0;
            onGround = true;
        
        } else {
            velocityY += gravity;
            onGround = false;
        }

        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // if (key == KeyEvent.VK_LEFT) {
        //     velocityX = -moveSpeed;
        // }

        // if (key == KeyEvent.VK_RIGHT) {
        //     velocityX = moveSpeed;
        // }

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
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);

        Platformer game = new Platformer(frame);
        frame.add(game);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}

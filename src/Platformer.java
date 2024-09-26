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
    private final int moveSpeed = 8;
    private final int jumpStrength = 15;
    private final int worldWidth = 2000;
    private final int worldHeight = 600;
    private static final int frameWidth = 800;
    private static final int frameHeight = 600;
    private int lives = 3;
    private boolean gameOver = false;

    public JFrame frame;

    private Player player;

    public ArrayList<Rect> platformList = new ArrayList<>();
    public ArrayList<Rect> renderList = new ArrayList<>();
    public ArrayList<Rect> killBlockList = new ArrayList<>();

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

        this.player = new Player(100, 500, 50, 50, 0.0, 0.0, Color.BLACK);

        Rect platform1 = new Rect(600, 500, 100, 50, Color.GREEN);
        this.addObstacle(platform1);

        Platform floor = new Platform(0, worldHeight-50, worldWidth, 50, Color.GREEN);
        this.addObstacle(floor);

        KillBlock killBlock = new KillBlock(500, 500, 100, 50, Color.RED);
        this.addKillBlock(killBlock);

        setFocusable(true);
        addKeyListener(this);
    }

    public void addObstacle(Rect obj) {
        this.platformList.add(obj);
        this.renderList.add(obj);
    }

    public void addKillBlock(KillBlock obj) {
        this.killBlockList.add(obj);
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

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Lives: " + lives, 10, 30);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("GAME OVER", frameWidth/2 - 150, frameHeight/2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    private void update() {
        if (lives <= 0) {
            gameOver = true;
            timer.stop();
            triggerGameOver();
            return;
        }
        
        playerX += velocityX;
        playerY += velocityY;

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

        for (Rect killBlock : killBlockList) {
            if (playerX + playerWidth > killBlock.x && playerX < killBlock.x + killBlock.width) {
                if (playerY + playerHeight + velocityY >= killBlock.y) {
                    playerX = 100;
                    lives--;
                    break;
                }
            }
        }
        
        if (playerX + playerWidth > worldWidth) {
            playerX = worldWidth - playerWidth; // Bind to right edge
            velocityX = 0;
        }

        if (playerX < 0) {
            playerX = 0;
        }

        cameraX = playerX - frameWidth / 2;
        cameraY = playerY - frameHeight / 2;

        if (cameraX < 0) cameraX = 0;
        if (cameraY < 0) cameraY = 0;
        if (cameraX > worldWidth - frameWidth) cameraX = worldWidth - frameWidth;
        if (cameraY > worldHeight - frameHeight) cameraY = worldHeight - frameHeight;

        repaint();

    }

    public void triggerGameOver() {
        Timer exitTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        exitTimer.setRepeats(false);
        exitTimer.start();
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

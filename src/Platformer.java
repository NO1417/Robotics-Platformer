import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

public class Platformer extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private int cameraX, cameraY;
    private boolean onGround;
    private final double gravity = 1.2;
    private final int moveSpeed = 8;
    private final int jumpStrength = 22;
    private final int worldWidth = 800;
    private final int worldHeight = 600;
    private static final int frameWidth = 800;
    private static final int frameHeight = 600;
    private int lives = 3;
    private boolean gameOver = false;
    private int score = 0;

    public JFrame frame;

    private Player player;

    public ArrayList<Rect> platformList = new ArrayList<>();
    public ArrayList<Rect> renderList = new ArrayList<>();
    public ArrayList<Rect> killBlockList = new ArrayList<>();
    public ArrayList<Coin> coinList = new ArrayList<>();

    private int platformHeight = 25;
    private int platformSpawnY = 525;
    private int platformWidth = 300;

    public Platformer(JFrame frame) {
        //timer = new Timer(20, this);
        //timer.start();
        onGround = true;

        this.frame = frame;

        this.player = new Player(200, 500, 50, 50, 0.0, 0.0, Color.MAGENTA);
        this.addRenderableObject(this.player);
        
        Platform floor = new Platform(0, worldHeight-50, 600, 50, Color.BLUE);
        this.addPlatform(floor);


        setFocusable(true);
        addKeyListener(this);
    }

    public void addPlatform(Rect obj) {
        this.platformList.add(obj);
        this.renderList.add(obj);
    }

    public void addKillBlock(KillBlock obj) {
        this.killBlockList.add(obj);
        this.renderList.add(obj);
    }

    public void addCoin(Coin obj) {
        this.coinList.add(obj);
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

        g.setColor(Color.decode("#242733"));
        g.fillRect(0, 0, worldWidth, worldHeight); // Background

        for ( Rect rect : this.renderList ) {
            g.setColor(rect.color);
            g.fillRect(rect.x - cameraX,rect.y - cameraY, rect.width, rect.height);
        }
        g.setColor(player.color);
        g.fillRect(player.x - cameraX, player.y - cameraY, player.width, player.height);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Lives: " + lives, 10, 30);

        g.setColor(Color.GREEN);
        g.drawString("Green = safe", 10, 60);

        g.setColor(Color.RED);
        g.drawString("Red = BAD", 10, 90);

        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 130, 30);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("GAME OVER", frameWidth/2 - 150, frameHeight/2);
        }
    }

    public void draw() {
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    private void generatePlatform() {
        this.addPlatform(new Platform((player.x+cameraX)+(worldWidth/2), platformSpawnY, platformWidth, platformHeight, Color.blue));

        if (randInt(1,2) == 1) {
            this.addCoin(new Coin((player.x+cameraX)+(worldWidth/2)+(randInt(0,platformWidth-platformHeight)), platformSpawnY-( platformHeight*3), platformHeight, platformHeight, Color.YELLOW));
        }

        int m = randInt(-7,4);
        int temp = platformSpawnY - m*platformHeight;
        if (temp > worldHeight- (platformHeight*5)  || temp < platformHeight*3) {
            m *= -1;
            temp = platformSpawnY - m*platformHeight;
        }

        platformSpawnY = temp;





    }

    private int randInt(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    private void update() {
        player.velocity.x = 8;

        if (lives <= 0) {
            gameOver = true;
            //timer.stop();
            triggerGameOver();
            return;
        }
        
        player.x += player.velocity.x;
        player.y += player.velocity.y;

        checkCoinCollisions();

        boolean platformCollision = false;
        
        for (Rect platform : platformList) {
            if (player.x + player.width > platform.x && player.x < platform.x + platform.width) {
                if (player.y + player.height <= platform.y && player.y + player.height + player.velocity.y >= platform.y) {
                    player.y = platform.y - player.height;
                    player.velocity.y = 0;
                    onGround = true;
                    platformCollision = true;
                    break;
                }
            }
        }

        if (!platformCollision) {
            player.velocity.y += gravity;
            onGround = false;
        }

        if (player.y > worldHeight) {
            lives = 0;
        }

        for (Rect killBlock : killBlockList) {
            if (player.x + player.width > killBlock.x && player.x < killBlock.x + killBlock.width) {
                if (player.y + player.height + player.velocity.y >= killBlock.y) {
                    player.x = 100;
                    lives--;
                    break;
                }
            }
        }

        if (player.x < 0) {
            player.x = 0;
        }

        cameraX = player.x - frameWidth / 2;
        cameraY = player.y - frameHeight / 2;

        if (cameraX < 0) cameraX = 0;
        if (cameraY < 0) cameraY = 0;
        if (cameraY > worldHeight - frameHeight) cameraY = worldHeight - frameHeight;

        repaint();

    }

    public void checkCoinCollisions() {
        ArrayList<Coin> collectedCoins = new ArrayList<>();

        for (Coin coin: coinList) {
            if (coin.collidesWithRect(player)) {
                score += 100;
                collectedCoins.add(coin);
            }
        }
        for (Coin coin: collectedCoins) {
            coinList.remove(coin);
            renderList.remove(coin);
        }
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
        /*
        if (key == KeyEvent.VK_LEFT) {
            player.velocity.x = -moveSpeed;
        }

        if (key == KeyEvent.VK_RIGHT) {
            player.velocity.x = moveSpeed;
        }

         */

        if (key == KeyEvent.VK_SPACE && onGround) {
            player.velocity.y = -jumpStrength;
            onGround = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            player.velocity.x = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public void display_loop() throws InterruptedException, IOException {
        int fps = 100;
        int clock_timer = 0;
        while (true) {
            Thread.sleep((1000/fps));

            if (clock_timer % 2 == 0) {
                update();
            }

            if (clock_timer % 54 == 0) {
                generatePlatform();
            }

            draw();
            clock_timer++;
        }
    }

    public static void main() throws IOException, InterruptedException {
        JFrame frame = new JFrame("Simple Platformer");
        frame.setSize(frameWidth,frameHeight);
        frame.setLocationRelativeTo(null);

        Platformer game = new Platformer(frame);
        frame.add(game);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        game.display_loop();


    }
}

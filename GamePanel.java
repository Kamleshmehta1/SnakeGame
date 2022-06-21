import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

    int SCREEN_WIDTH = 600;
    int SCREEN_HEIGHT = 600;
    int UNIT_SIZE = 25;
    int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 120;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    private Image body;
    private Image headL;
    private Image headR;
    private Image headU;
    private Image headD;
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdaptor());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void loadImages() {
        ImageIcon iid = new ImageIcon("assets/snakeimage.png");
        body = iid.getImage();

        ImageIcon iiLeft = new ImageIcon("assets/leftmouth.png");
        headL = iiLeft.getImage();

        ImageIcon iiRight = new ImageIcon("assets/rightmouth.png");
        headR = iiRight.getImage();

        ImageIcon iiUp = new ImageIcon("assets/upmouth.png");
        headU = iiUp.getImage();

        ImageIcon iiDown = new ImageIcon("assets/downmouth.png");
        headD = iiDown.getImage();
    }

    public void restart() {
        bodyParts = 6;
        applesEaten = 0;
        direction = 'R';
        SCREEN_WIDTH = 600;
        SCREEN_HEIGHT = 600;
        UNIT_SIZE = 25;
        GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
        running = true;
        timer.start();
        repaint();
    }

    public void draw(Graphics g) {

        if (running) {
            loadImages();
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0 && direction == 'L') {
                    g.drawImage(headL, x[i], y[i], this);
                } else if (i == 0 && direction == 'R') {
                    g.drawImage(headR, x[i], y[i], this);
                } else if (i == 0 && direction == 'U') {
                    g.drawImage(headU, x[i], y[i], this);
                } else if (i == 0 && direction == 'D') {
                    g.drawImage(headD, x[i], y[i], this);
                } else {
                    g.drawImage(body, x[i], y[i], this);
                }
            }
        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

        for (int i = bodyParts - 1; i > 0; i--) {
            if (x[0] == appleX && y[0] == appleY) {
                newApple();
            }
        }
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void CheckApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void CheckCollision() {

        // checking head collision

        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // check if head touches left border

        if (x[0] < 0) {
            x[0] = SCREEN_WIDTH;
        }

        // check if head touches right border

        if (x[0] > SCREEN_WIDTH) {
            x[0] = 0;
        }

        // check if head touches top border

        if (y[0] < 0) {
            y[0] = SCREEN_HEIGHT;
        }
        // check if head touches bottom border

        if (y[0] > SCREEN_HEIGHT) {
            y[0] = 0;
        }

        else if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {

        // Score Texts

        g.setColor(Color.red);
        g.setFont(new Font("INK Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2,
                g.getFont().getSize());

        // Game Over Texts
        g.setColor(Color.red);
        g.setFont(new Font("INK Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        g.setColor(Color.green);
        g.setFont(new Font("INK Free", Font.BOLD, 30));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press SPACE to restart", (SCREEN_WIDTH - metrics3.stringWidth("Press SPACE to restart")) / 2,
                SCREEN_HEIGHT - (SCREEN_HEIGHT / 4));
    }

    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            CheckApple();
            CheckCollision();
        }
        repaint();
    }

    public class MyKeyAdaptor extends KeyAdapter {
        // over-riding

        public void keyPressed(KeyEvent e) {

            if (!running && e.getKeyCode() == KeyEvent.VK_SPACE) {
                restart();
            }

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int TOTAL_HEIGHT = 700;
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 70;

    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 3;
    int applesEaten;
    int appleX, appleY;
    char direction = 'E';
    boolean running = false;
    Timer timer;
    Random random;
    JFrame frame;
    JButton tryAgain;

    GamePanel(JFrame frame) {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, TOTAL_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        this.frame = frame;

        tryAgain = new JButton("Try Again");
        tryAgain.setVisible(true);
        tryAgain.setBounds(100, 100, tryAgain.getWidth(), tryAgain.getHeight());
        tryAgain.addActionListener(e -> restartGame());
        tryAgain.setFocusable(false);

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

    public void draw(Graphics g) {

        if (running) {

            g.setColor(Color.WHITE);
            g.drawLine(0, 600, SCREEN_WIDTH, 600);

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            g.setColor(Color.white);
            g.setFont(new Font("Consolas", Font.BOLD, 35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,650);

        } else {
            gameOver(g);
        }
    }

    public void newApple() {

        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;

    }

    public void move(){
        for(int i = bodyParts;i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case 'N':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'S':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'W':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'E':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }


    public void checkApple() {

        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }

    }

    public void checkCollision() {
        // Checks if 'head' collides with the body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }

        // Checks if head touches left border
        if (x[0] < 0) {
            running = false;
        }

        // Checks if head touches right border
        if (x[0] >= SCREEN_WIDTH) {
            running = false;
        }

        // Checks if head touches upper border
        if (y[0] < 0) {
            running = false;
        }

        // Checks if head touches lower border
        if (y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }

    }

    public void gameOver(Graphics g) {

        g.setColor(Color.red);
        g.setFont(new Font("Consolas", Font.BOLD, 45));
        FontMetrics metricsScore = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metricsScore.stringWidth("Score: " + applesEaten)) / 2,350);

        g.setColor(Color.red);
        g.setFont(new Font("Consolas", Font.BOLD, 75));
        FontMetrics metricsOver = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metricsOver.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        g.setColor(Color.red);
        g.setFont(new Font("Consolas", Font.ITALIC, 35));
        FontMetrics metricsRestart = getFontMetrics(g.getFont());
        g.drawString("Press SPACE to restart", (SCREEN_WIDTH - metricsRestart.stringWidth("Press SPACE to restart")) / 2, 650);

        tryAgain.setVisible(true);
    }

    public void restartGame() {
        if (frame != null) {
            frame.dispose(); // Dispose of the current frame
        }

        frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel newGamePanel = new GamePanel(frame); // Create a new GamePanel

        frame.getContentPane().add(newGamePanel); // Add the new GamePanel to the frame
        frame.setSize(SCREEN_WIDTH, TOTAL_HEIGHT);

        frame.setVisible(true); // Display the new frame
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
           move();
           checkApple();
           checkCollision();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    if (direction != 'E') {
                        direction = 'W';
                    }
                    break;
                case KeyEvent.VK_D:
                    if (direction != 'W') {
                        direction = 'E';
                    }
                    break;
                case KeyEvent.VK_W:
                    if (direction != 'S') {
                        direction = 'N';
                    }
                    break;
                case KeyEvent.VK_S:
                    if (direction != 'N') {
                        direction = 'S';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (!running) {
                        restartGame();
                    }
                    break;
            }
        }
    }

}

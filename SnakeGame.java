import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private final int WIDTH = 720;
    private final int HEIGHT = 480;
    private final int UNIT_SIZE = 20;
    private final int DELAY = 100;

    private LinkedList<Point> snake = new LinkedList<>();
    private Point food;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private int score = 0;

    public SnakeGame() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        startGame();
    }

    public void startGame() {
        snake.clear();
        snake.add(new Point(120, 60));
        spawnFood();
        direction = 'R';
        score = 0;
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void spawnFood() {
        Random rand = new Random();
        int x = rand.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        int y = rand.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        food = new Point(x, y);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // Desenha a comida
            g.setColor(Color.RED);
            g.fillRect(food.x, food.y, UNIT_SIZE, UNIT_SIZE);

            // Desenha a cobrinha
            for (Point p : snake) {
                g.setColor(Color.GREEN);
                g.fillRect(p.x, p.y, UNIT_SIZE, UNIT_SIZE);
            }

            // Desenha o score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.PLAIN, 20));
            g.drawString("Score: " + score, 10, 20);
        } else {
            gameOver(g);
        }
    }

    public void move() {
        Point head = snake.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case 'U': newHead.y -= UNIT_SIZE; break;
            case 'D': newHead.y += UNIT_SIZE; break;
            case 'L': newHead.x -= UNIT_SIZE; break;
            case 'R': newHead.x += UNIT_SIZE; break;
        }

        // Warp (atravessa a borda)
        if (newHead.x < 0) newHead.x = WIDTH - UNIT_SIZE;
        else if (newHead.x >= WIDTH) newHead.x = 0;
        if (newHead.y < 0) newHead.y = HEIGHT - UNIT_SIZE;
        else if (newHead.y >= HEIGHT) newHead.y = 0;

        // Verifica colisão com o próprio corpo
        if (snake.contains(newHead)) {
            running = false;
            timer.stop();
            return;
        }

        snake.addFirst(newHead);

        if (newHead.equals(food)) {
            score++;
            spawnFood();
        } else {
            snake.removeLast();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Consolas", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over!", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U'; break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D'; break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L'; break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R'; break;
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame gamePanel = new SnakeGame();
        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

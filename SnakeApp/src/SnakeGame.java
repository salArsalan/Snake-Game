import javax.swing.*;

public class SnakeGame {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            GamePanel gamePanel = new GamePanel(frame);
            frame.add(gamePanel);

            frame.setSize(GamePanel.SCREEN_WIDTH, GamePanel.TOTAL_HEIGHT);
            frame.setVisible(true);

            frame.setLocationRelativeTo(null);
        });
    }

}

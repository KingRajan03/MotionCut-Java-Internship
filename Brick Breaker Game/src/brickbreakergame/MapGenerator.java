package brickbreakergame;
import java.awt.*;

public class MapGenerator {
    public int map[][];
    public int brickWidth;
    public int brickHeight;

    public MapGenerator(int row, int col) {
        map = new int[row][col];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = 1; // 1 indicates the brick is present
            }
        }

        brickWidth = 540 / col;
        brickHeight = 150 / row;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    g.setColor(Color.white);
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }

    // New method to generate levels
    public void generateLevel(int level) {
        // Clear current map
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = 0;
            }
        }

        // Generate new level layout based on level
        if (level == 1) {
            // Simple layout
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 7; j++) {
                    map[i][j] = 1;
                }
            }
        } else if (level == 2) {
            // More complex layout
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    if ((i + j) % 2 == 0) {
                        map[i][j] = 1;
                    }
                }
            }
        } else if (level == 3) {
            // Even more complex layout
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    if (i == j || i + j == 6) {
                        map[i][j] = 1;
                    }
                }
            }
        }
    }
}

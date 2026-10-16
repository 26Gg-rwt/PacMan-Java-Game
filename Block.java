import java.awt.Color;
import java.awt.Rectangle;

public class Block {

    int x;
    int y;

    int width;
    int height;

    int startX;
    int startY;

    int directionX;
    int directionY;

    int velocityX;
    int velocityY;

    Color color;

    public Block(
            int x,
            int y,
            int width,
            int height,
            Color color
    ) {

        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.color = color;

        startX = x;
        startY = y;
    }

    public Rectangle getBounds() {

        return new Rectangle(
                x,
                y,
                width,
                height
        );
    }

    public void resetPosition() {

        x = startX;
        y = startY;

        directionX = 0;
        directionY = 0;
    }
}
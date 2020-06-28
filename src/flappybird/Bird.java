package flappybird;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

/**
 * Code for the bird character in Flappy Bird.
 * 
 * @author Jiaxuan Wang
 */
public class Bird {

    private final int width = 55;
    private final int height = 38;
    private final Image[] yellowImages = new Image[] { 
                Resources.BIRD_YELLOW_WING_MID, 
                Resources.BIRD_YELLOW_WING_DOWN,
                Resources.BIRD_YELLOW_WING_MID, 
                Resources.BIRD_YELLOW_WING_UP };

    private final Image[] blueImages = new Image[] { 
                Resources.BIRD_BLUE_WING_MID, 
                Resources.BIRD_BLUE_WING_DOWN,
                Resources.BIRD_BLUE_WING_MID, 
                Resources.BIRD_BLUE_WING_UP };

    private final Image[] redImages = new Image[] { 
                Resources.BIRD_RED_WING_MID, 
                Resources.BIRD_RED_WING_DOWN,
                Resources.BIRD_RED_WING_MID, 
                Resources.BIRD_RED_WING_UP };

    private double x;
    private double y;
    private double yVel;
    private int imageIndex;
    private Color color;

    /**
     * Constructs a new {@code Bird} object hovering in the middle of the window, in
     * the ready phase.
     */
    public Bird() {
        reset();
    }

    // Accessors
    // -------------------------------------------------------
    public int getX() { return (int) x; }
    public int getY() { return (int) y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Rectangle getBounds() { return new Rectangle((int) x, (int) y, width, height); }

    // Modifiers
    // -------------------------------------------------------
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setColor(Color color) { this.color = color; }

    /**
     * Returns whether this {@code Bird} object's boundaries intersect the specified
     * boundaries.
     * 
     * @param bounds a {@code Rectangle} specifying the boundaries to check
     * @return {@code true} if the boundaries intersect; {@code false} otherwise
     */
    public boolean intersects(Rectangle bounds) {
        return getBounds().intersects(bounds);
    }

    /**
     * Updates this {@code Bird}'s vertical velocity to make it fly upward. Note
     * that the velocity is negative because downward is positive.
     */
    public void fly() {
        yVel = -10.5;
    }

    /**
     * Resets this {@code Bird} to its original state, hovering in the middle of the
     * window in the ready phase.
     */
    public void reset() {
        x = 120;
        y = 310;
        yVel = 0;
        imageIndex = 0;
    }

    /**
     * Updates the location of this {@code Bird} based on its velocity, taking into
     * account the upper and lower bounds of the frame.
     * 
     * @param screenBounds a {@code Rectangle} specifying the boundaries
     */
    public void update(Rectangle screenBounds) {
        yVel += 0.7;
        y += yVel;

        if (y > screenBounds.getMaxY() - height) {
            y = (int) screenBounds.getMaxY() - height;
            yVel = 0;
        } else if (y < screenBounds.getMinY()) {
            y = (int) screenBounds.getMinY();
            yVel = 0;
        }
    }

    /**
     * Updates the image index in order to animate the bird flapping its wings.
     */
    public void animate() {
        imageIndex = (imageIndex + 1) % yellowImages.length;
    }

    /**
     * Draws this {@code Bird} object to the screen.
     * 
     * @param g  the {@code Graphics} object to be drawn on
     * @param io the {@code ImageObserver} to be notified
     */
    public void draw(Graphics g, ImageObserver io) {
        if (color.equals(Color.BLUE)) {
            g.drawImage(blueImages[imageIndex], (int) x, (int) y, width, height, io);
        } else if (color.equals(Color.RED)) {
            g.drawImage(redImages[imageIndex], (int) x, (int) y, width, height, io);
        } else {
            g.drawImage(yellowImages[imageIndex], (int) x, (int) y, width, height, io);
        }
    }

}

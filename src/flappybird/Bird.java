package flappybird;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

/**
 * Code for the bird character in Flappy Bird.
 * 
 * @author Jiaxuan Wang
 */
public class Bird {

	// original: 397 * 281
	private final int width = 52;
	private final int height = 37;

	private double x;
	private double y;
	private double yVel;

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
	public int getWidth() {	return width; }
	public int getHeight() { return height; }
	public Rectangle getBounds() { return new Rectangle((int) x, (int) y, width, height); }

	// Modifiers
	// -------------------------------------------------------
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }

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
		x = 140;
		y = 310;
		yVel = 0;
	}

	/**
	 * Updates the location of this {@code Bird} based on its velocity, taking into
	 * account the upper and lower bounds of the frame.
	 * 
	 * @param screenBounds a {@code Rectangle} specifying the boundaries
	 */
	public void animate(Rectangle screenBounds) {
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
	 * Draws this {@code Bird} object to the screen.
	 * 
	 * @param g  the {@code Graphics} object to be drawn on
	 * @param io the {@code ImageObserver} to be notified
	 */
	public void draw(Graphics g, ImageObserver io) {
		g.drawImage(Resources.BIRD_IMAGE, (int) x, (int) y, width, height, io);
	}

}

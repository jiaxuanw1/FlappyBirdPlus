package flappybird;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

/**
 * Code for the fireballs that Mario throws.
 * 
 * @author Jiaxuan Wang
 */
public class Fireball {

	private final int width = 24;
	private final int height = 27;

	private double x;
	private double y;
	private final double xVel;
	private final double yVel;

	/**
	 * Constructs a new {@code Fireball} object with the specified position and
	 * velocity.
	 * 
	 * @param x    the x position
	 * @param y    the y position
	 * @param xVel the horizontal velocity
	 * @param yVel the vertical velocity
	 */
	public Fireball(double x, double y, double xVel, double yVel) {
		this.x = x;
		this.y = y;
		this.xVel = xVel;
		this.yVel = yVel;
	}

	// Accessors
	// -------------------------------------------------------
	public int getX() { return (int) x; }
	public int getY() { return (int) y; }
	public Rectangle getBounds() { return new Rectangle((int) x, (int) y, width, height); }

	/**
	 * Updates this {@code Fireball} object's position based on its horizontal and
	 * vertical velocities.
	 */
	public void update() {
		x += xVel;
		y += yVel;
	}

	/**
	 * Draws this {@code Fireball} object to the screen.
	 * 
	 * @param g  the {@code Graphics} object to be drawn on
	 * @param io the {@code ImageObserver} to be notified
	 */
	public void draw(Graphics g, ImageObserver io) {
		g.drawImage(Resources.FIREBALL_IMAGE, (int) x, (int) y, width, height, io);
	}

}

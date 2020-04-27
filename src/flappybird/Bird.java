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

	// original picture: 397 * 281
	private final int width = 51;
	private final int height = 36;

	private int x;
	private int y;
	private int yVel;

	public Bird() {
		reset();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean intersects(Rectangle bounds) {
		return getBounds().intersects(bounds);
	}

	/**
	 * Updates the bird's vertical velocity to make it fly upward. Note that the
	 * velocity is negative because downward is positive.
	 */
	public void fly() {
		yVel = -10; // CHANGE THIS
	}

	/**
	 * Resets the bird to its original state in the ready phase.
	 */
	public void reset() {
		x = 80;
		y = 250;
		yVel = 0;
	}

	/**
	 * Updates the location of the bird based on its velocity, taking into account
	 * the upper and lower bounds of the frame.
	 * 
	 * @param screenBounds object with the bounds of the window
	 */
	public void animate(Rectangle screenBounds) {
		yVel++;
		y += yVel;

		if (y > screenBounds.getMaxY() - height) {
			y = (int) screenBounds.getMaxY() - height;
			yVel = 0;
		} else if (y < screenBounds.getMinY()) {
			yVel = 1;
		}
	}

	/**
	 * Draws the object to the screen.
	 * 
	 * @param g  the {@code Graphics} object to be drawn on
	 * @param io the {@code ImageObserver} to be notified
	 */
	public void draw(Graphics g, ImageObserver io) {
		g.drawImage(FlappyBirdGame.BIRD_IMAGE, x, y, width, height, io);
	}

}

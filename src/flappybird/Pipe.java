package flappybird;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

/**
 * Code for the green pipes in Flappy Bird.
 * 
 * @author Jiaxuan Wang
 */
public class Pipe {

	// original: 188 * 2100
	private final int width = 94;
	private final int height = 1050;

	private int x;
	private final int y;
	private final int shift;
	private final int xVel;

	public Pipe(Rectangle screenBounds, int xVel) {
		this.xVel = xVel;
		shift = (int) (Math.random() * (screenBounds.getMaxY() - 300)) - 370;
		x = (int) screenBounds.getMaxX() + 200;
		// int testYVal = (int) ((screenBounds.getMaxY() - 300) - 370);
		y = shift;
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

	public Rectangle getUpperBound() {
		return new Rectangle(x, y, width, 445);
	}

	public Rectangle getLowerBound() {
		return new Rectangle(x, shift + 605, width, 445);
	}

	public void animate() {
		x += xVel;
	}

	/**
	 * Draws the object to the screen.
	 * 
	 * @param g  the {@code Graphics} object to be drawn on
	 * @param io the {@code ImageObserver} to be notified
	 */
	public void draw(Graphics g, ImageObserver io) {
		g.drawImage(FlappyBirdGame.PIPE_IMAGE, x, y, width, height, io);
	}

}
package flappybird;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

public class Pipe {

	private final int width = 0; // CHANGE THIS
	private final int height = 0; // CHANGE THIS

	private int x;
	private final int y;
	private final int shift;
	private final int xVel;

	public Pipe(int maxY, int xVel) {
		this.xVel = xVel;
		shift = (int) Math.random() * (maxY - 20); // need to test numbers for the 20
		y = shift;
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
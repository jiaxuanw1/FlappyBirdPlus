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

	private final int width = 94;
	private final int height = 1050;

	private int x;
	private final int y;
	private int previousX;
	private final int shift;
	private final int xVel;

	/**
	 * Constructs a new {@code Pipe} object with a random position for its gap
	 * within the specified boundaries.
	 * 
	 * @param screenBounds a {@code Rectangle} specifying the boundaries
	 * @param xVel         the velocity at which the game scrolls
	 */
	public Pipe(Rectangle screenBounds, int xVel) {
		this.xVel = xVel;
		shift = (int) (Math.random() * (screenBounds.getMaxY() - 260)) - 400;
		x = (int) screenBounds.getMaxX() + 200;
		previousX = x;
		y = shift;
	}

	//Accessors
	//-------------------------------------------------------
	public int getX() { return x; }
	public int getY() { return y; }
	public int getPreviousX() { return previousX; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public Rectangle getUpperBound() { return new Rectangle(x, y, width, 445); }
	public Rectangle getLowerBound() { return new Rectangle(x, shift + 605, width, 445); }

	/**
	 * Updates the position of this {@code Pipe} based on the horizontal velocity.
	 */
	public void update() {
		previousX = x;
		x += xVel;
	}

	/**
	 * Draws this {@code Pipe} object to the screen.
	 * 
	 * @param g  the {@code Graphics} object to be drawn on
	 * @param io the {@code ImageObserver} to be notified
	 */
	public void draw(Graphics g, ImageObserver io) {
		g.drawImage(Resources.PIPE_IMAGE, x, y, width, height, io);
	}

}
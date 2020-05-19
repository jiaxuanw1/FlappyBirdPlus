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
	private int xVel;

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

	/**
	 * Constructs a new {@code Pipe} object with a specified vertical shift within
	 * the specified boundaries. Note that when applying the shift, positive is
	 * downward.
	 * 
	 * @param screenBounds a {@code Rectangle} specifying the boundaries
	 * @param xVel         the velocity at which the game scrolls
	 * @param shift        the vertical shift to apply
	 */
	public Pipe(Rectangle screenBounds, int xVel, int shift) {
		this.xVel = xVel;
		this.shift = shift;
		x = (int) screenBounds.getMaxX() + 200;
		previousX = x;
		y = shift;
	}

	//Accessors
	//-------------------------------------------------------
	public int getX() { return x; }
	public int getY() { return y; }
	public int getPreviousX() { return previousX; }
	public int getXVel() { return xVel; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public Rectangle getUpperBound() { return new Rectangle(x, y, width, 445); }
	public Rectangle getLowerBound() { return new Rectangle(x, shift + 605, width, 445); }

	// Modifiers
	// -------------------------------------------------------
	public void setXVel(int xVel) { this.xVel = xVel; }

	/**
	 * Determines whether another {@code Pipe} object is equal to this {@code Pipe}.
	 * <p>
	 * Returns {@code true} if the two {@code Pipe} objects have the same x and y
	 * values.
	 * 
	 * @param p the {@code Pipe} to test for equality with this {@code Pipe}
	 * @return {@code true} if the objects are equal; {@code false} otherwise
	 */
	public boolean equals(Pipe p) {
		return x == p.getX() && y == p.getY();
	}

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
	 * @param dark whether to draw the dark graphics
	 */
	public void draw(Graphics g, ImageObserver io, boolean dark) {
		if (dark) {
			g.drawImage(Resources.ALT_PIPE_IMAGE, x, y, width, height, io);
		} else {
			g.drawImage(Resources.PIPE_IMAGE, x, y, width, height, io);
		}
	}

}
package flappybird;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import javax.sound.sampled.Clip;

/**
 * Code for the character of Mario, who makes his appearance in this version of
 * Flappy Bird every 50 points scored by the player.
 * 
 * @author Jiaxuan Wang
 */
public class Mario {

	// Modes
	// -------------------------------------------------------
	private static final int ENTERING = 1;
	private static final int STANDING = 2;
	private static final int THROWING = 3;
	private static final int JUMPING = 4;
	private static final int FINAL_JUMPING = 5;

	// Constants
	// -------------------------------------------------------
	private final int width = 41;
	private final int height = 74;

	// Instance Variables
	// -------------------------------------------------------
	private double x;
	private double y;
	private double xVel;
	private double yVel;
	private int mode;
	private Pipe pipe;

	/**
	 * Constructs a {@code Mario} object belonging to the specified {@code Pipe}.
	 * 
	 * @param pipe the {@code Pipe} that houses this {@code Mario} object
	 */
	public Mario(Pipe pipe) {
		this.pipe = pipe;
		x = pipe.getX() + pipe.getWidth() / 2 - width / 2;
	}

	// Accessors
	// -------------------------------------------------------
	public int getX() { return (int) x; }
	public int getY() { return (int) y; }
	public Rectangle getBounds() { return new Rectangle((int) x, (int) y, width, height); }

	/**
	 * Makes {@code Mario} come out of the bottom pipe.
	 */
	public void start() {
		mode = ENTERING;
		x = pipe.getX() + pipe.getWidth() / 2 - width / 2;
		y = pipe.getLowerBound().getMinY();
		yVel = -1;
		playSound(Resources.MARIO_PIPE_SOUND);
	}

	/**
	 * Changes {@code Mario}'s pose to his regular standing pose.
	 */
	public void stand() {
		mode = STANDING;
	}

	/**
	 * Changes {@code Mario}'s pose to his throwing pose and plays the fireball
	 * throwing sound.
	 */
	public void animateThrow() {
		mode = THROWING;
		playSound(Resources.MARIO_FIREBALL_SOUND);
	}

	/**
	 * Updates {@code Mario}'s vertical velocity to make him jump upward. Note that
	 * the velocity is negative because downward is positive.
	 */
	public void jump() {
		mode = JUMPING;
		yVel = -7;
		playSound(Resources.MARIO_JUMP_SOUND);
	}

	/**
	 * Updates {@code Mario}'s horizontal and vertical velocities to make him jump
	 * out at the bird for his final jump.
	 */
	public void finalJump() {
		mode = FINAL_JUMPING;
		xVel = -5;
		yVel = -7;
		playSound(Resources.MARIO_JUMP_SOUND);
	}

	/**
	 * Returns whether this {@code Mario} has completed his final jump.
	 * 
	 * @return {@code true} if this {@code Mario} has finished his final jump;
	 *         {@code false} otherwise
	 */
	public boolean isFinished() {
		return y > FlappyBirdGame.GROUND_LEVEL;
	}

	/**
	 * Updates the velocity and position of this {@code Mario} object.
	 */
	public void update() {
		x += xVel;
		y += yVel;

		switch (mode) {
			case ENTERING:
				if (y <= pipe.getLowerBound().getMinY() - height) {
					jump();
				}
				break;
			case JUMPING:
				if (y >= pipe.getLowerBound().getMinY() - height) {
					yVel = 0;
					mode = STANDING;
				} else {
					yVel += 0.4;
				}
				break;
			case FINAL_JUMPING:
				yVel += 0.4;
				break;
		}

	}

	/**
	 * Draws this {@code Mario} object to the screen.
	 * 
	 * @param g  the {@code Graphics} object to be drawn on
	 * @param io the {@code ImageObserver} to be notified
	 */
	public void draw(Graphics g, ImageObserver io) {
		switch (mode) {
			case THROWING:
				g.drawImage(Resources.MARIO_THROWING, (int) x, (int) y, width, height, io);
				break;
			case JUMPING:
			case FINAL_JUMPING:
				g.drawImage(Resources.MARIO_JUMPING, (int) x, (int) y, width, height, io);
				break;
			default:
				g.drawImage(Resources.MARIO_STANDING, (int) x, (int) y, width, height, io);
		}
	}

	/**
	 * Plays the audio from a {@code Clip} object.
	 * 
	 * @param clip the {@code Clip} to play
	 */
	public void playSound(Clip clip) {
		clip.setFramePosition(0);
		clip.start();
	}

}

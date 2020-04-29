package flappybird;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;

import arcade.AnimationPanel;

/**
 * The {@code FlappyBirdGame} class contains the code for handling interactions
 * and drawing the graphical elements of the game Flappy Bird.
 * 
 * @author Jiaxuan Wang
 */
public class FlappyBirdGame extends AnimationPanel {

	// Constants
	// -------------------------------------------------------
	private static final int FRAME_WIDTH = 500;
	private static final int FRAME_HEIGHT = 700;
	private static final int X_VELOCITY = -3; // must be negative to move left
	private static final int GROUND_LEVEL = 577;

	public static final int READY = 0;
	public static final int PLAYING = 1;
	public static final int CRASHED = 2;

	// Instance Variables
	// -------------------------------------------------------
	private int score;
	private int highScore;
	private int mode;
	private int groundX;
	private double backdropX;
	private final Rectangle bounds;

	private Bird bird = new Bird();
	private List<Pipe> pipes = new ArrayList<Pipe>();

	// Constructor
	// -------------------------------------------------------
	public FlappyBirdGame() {
		super("Flappy Bird Plus", FRAME_WIDTH + 15, FRAME_HEIGHT + 30);
		Resources.load();
		score = 0;
		highScore = 0;
		mode = READY;
		groundX = 0;
		backdropX = 0;
		bounds = new Rectangle(0, 0, FRAME_WIDTH, GROUND_LEVEL);
		pipes.add(new Pipe(bounds, X_VELOCITY));
	}

	// The renderFrame method is the one which is called each time a frame is drawn.
	// -------------------------------------------------------
	protected Graphics renderFrame(Graphics g) {
		// Draw moving backdrop image
		if (mode != CRASHED) {
			backdropX = (backdropX < -240) ? 0 : backdropX + X_VELOCITY / 8.0d;
		}
		g.drawImage(Resources.BACKDROP_IMAGE, (int) backdropX, 0, 960, 700, this);

		// Detect when the bird hits the ground
		if (bird.getY() + bird.getHeight() >= GROUND_LEVEL && mode == PLAYING) {
			crash();
		}

		for (Pipe pipe : pipes) {
			// Draw the pipes (draw these after the backdrop)
			if (mode == PLAYING) {
				pipe.animate();
			}
			pipe.draw(g, this);

			// Increment the score when the bird passes between a pair of pipes
			if (pipe.getPreviousX() > bird.getX() && pipe.getX() <= bird.getX() && mode == PLAYING) {
				score++;
				playSound(Resources.SCORE_SOUND);
			}

			// Detect when the bird crashes into a pipe
			if ((bird.intersects(pipe.getUpperBound()) || bird.intersects(pipe.getLowerBound())) && mode == PLAYING) {
				crash();
			}
		}

		// Add new pipe when previous pipe is far enough
		Pipe lastPipe = pipes.get(pipes.size() - 1);
		if (lastPipe.getX() < FRAME_WIDTH - 80 && mode == PLAYING) {
			pipes.add(new Pipe(bounds, X_VELOCITY));
		}

		// Remove pipes that have gone off-screen
		Pipe firstPipe = pipes.get(0);
		if (firstPipe.getX() < -firstPipe.getWidth()) {
			pipes.remove(firstPipe);
		}

		// Draw the moving ground (draw this after the pipes)
		if (mode != CRASHED) {
			groundX = (groundX < -23) ? 0 : groundX + X_VELOCITY;
		}
		g.drawImage(Resources.GROUND_IMAGE, groundX, GROUND_LEVEL, this);

		// Draw the bird (draw this after pipes and ground)
		if (mode != READY) {
			bird.animate(bounds);
		}
		bird.draw(g, this);

		g.setColor(Color.WHITE);
		g.setFont(Resources.FONT);
		FontMetrics metrics = g.getFontMetrics();

		if (mode == CRASHED) {
			// Draw the Game Over screen
			g.drawImage(Resources.GAME_OVER_SCREEN, 50, 120, 400, 400, this);

			// Draw the score
			int scoreLen = (int) metrics.getStringBounds(Integer.toString(score), g).getWidth();
			int scoreStart = 415 - scoreLen;
			g.drawString(Integer.toString(score), scoreStart, 325);

			// Draw the high score
			int highScoreLen = (int) metrics.getStringBounds(Integer.toString(highScore), g).getWidth();
			int highScoreStart = 415 - highScoreLen;
			g.drawString(Integer.toString(highScore), highScoreStart, 400);

			// Draw the medal
			if (highScore >= 40) {
				g.drawImage(Resources.PLATINUM_MEDAL, 85, 300, 93, 90, this);
			} else if (highScore >= 30) {
				g.drawImage(Resources.GOLD_MEDAL, 90, 300, 93, 91, this);
			} else if (highScore >= 20) {
				g.drawImage(Resources.SILVER_MEDAL, 90, 300, 88, 95, this);
			} else if (highScore >= 10) {
				g.drawImage(Resources.BRONZE_MEDAL, 88, 300, 93, 91, this);
			}
		} else {
			// Draw the score
			int scoreLen = (int) metrics.getStringBounds(Integer.toString(score), g).getWidth();
			int start = FRAME_WIDTH / 2 - scoreLen / 2;
			g.drawString(Integer.toString(score), start, 55);

			// Draw the Get Ready screen
			if (mode == READY) {
				g.drawImage(Resources.READY_IMAGE, 84, 160, 330, 87, this);
			}
		}

		return g;
	}
	// --end of renderFrame method--

	public void crash() {
		mode = CRASHED;
		highScore = (score > highScore) ? score : highScore;
		playSound(Resources.HIT_SOUND);
		playSound(Resources.DIE_SOUND);
	}

	public void restart() {
		score = 0;
		mode = READY;
		bird.reset();
		pipes.clear();
		pipes.add(new Pipe(bounds, X_VELOCITY));
		playSound(Resources.SWOOSH_SOUND);
	}

	// -------------------------------------------------------
	// Respond to Mouse Events
	// -------------------------------------------------------
	public void mouseClicked(MouseEvent e) {
		if (mode == CRASHED) {
			restart();
		}
	}

	// -------------------------------------------------------
	// Respond to Keyboard Events
	// -------------------------------------------------------
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();

		// Make the bird fly up when spacebar is pressed
		if (c == ' ' && mode != CRASHED) {
			bird.fly();
			mode = PLAYING;
			playSound(Resources.FLY_SOUND);
		}
	}

	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {

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

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}// --end of ArcadeDemo class--

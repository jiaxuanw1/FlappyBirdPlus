package flappybird;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.Clip;

import arcade.AnimationPanel;

/**
 * The {@code FlappyBirdGame} class contains the code for game events and
 * drawing the graphical elements of the game Flappy Bird, with a few extra
 * features.
 * 
 * @author Jiaxuan Wang
 */
public class FlappyBirdGame extends AnimationPanel {

	// Modes
	// -------------------------------------------------------
	private static final int READY = 0;
	private static final int PLAYING = 1;
	private static final int CRASHED = 2;
	private static final int MARIO = 3;

	// Constants
	// -------------------------------------------------------
	private static final int FRAME_WIDTH = 500;
	private static final int FRAME_HEIGHT = 700;
	private static final int X_VELOCITY = -3; // must be negative to move left
	public static final int GROUND_LEVEL = 577;

	// Instance Variables
	// -------------------------------------------------------
	private int score;
	private int highScore;
	private int mode;
	private int groundX;
	private double backdropX;
	private boolean newHighScore;
	private boolean newGraphicsEnabled;
	private boolean dark;
	private StringBuilder keySequence;

	private final Rectangle bounds;
	private final Rectangle restartButton;

	private Resources resources;
	private Bird bird;
	private List<Pipe> pipes;
	private Mario mario;
	private Pipe marioPipe;
	private List<Fireball> fireballs;
	private int marioStartFrame;

	// Constructor
	// -------------------------------------------------------
	public FlappyBirdGame() {
		super("Flappy Bird Plus", FRAME_WIDTH + 15, FRAME_HEIGHT + 30);
		resources = new Resources();
		resources.load();
		highScore = resources.readHighScore();
		score = 0;
		mode = READY;
		groundX = 0;
		backdropX = 0;
		newHighScore = false;
		newGraphicsEnabled = true;
		dark = Math.random() < 0.5 ? true : false;
		keySequence = new StringBuilder();

		bounds = new Rectangle(0, 0, FRAME_WIDTH, GROUND_LEVEL);
		restartButton = new Rectangle(185, 450, 140, 40);

		bird = new Bird();
		bird.setColor(randomBirdColor());
		pipes = new ArrayList<Pipe>();
		pipes.add(new Pipe(bounds, X_VELOCITY));
		marioPipe = new Pipe(bounds, X_VELOCITY, -330);
		mario = new Mario(marioPipe);
		fireballs = new ArrayList<Fireball>();
		marioStartFrame = -9999;
	}

	// The renderFrame method is the one which is called each time a frame is drawn.
	// -------------------------------------------------------
	protected Graphics renderFrame(Graphics g) {
		// Draw moving backdrop image
		if (newGraphicsEnabled && dark) {
			if (mode != CRASHED) {
				backdropX = (backdropX < -320) ? 0 : backdropX + X_VELOCITY / 8.0d;
			}
			g.drawImage(Resources.DARK_BACKDROP_IMAGE, (int) backdropX, 0, this);
		} else {
			if (mode != CRASHED) {
				backdropX = (backdropX < -240) ? 0 : backdropX + X_VELOCITY / 8.0d;
			}
			g.drawImage(Resources.BACKDROP_IMAGE, (int) backdropX, 0, this);
		}

		// Detect when the bird hits the ground
		if (bird.getY() + bird.getHeight() >= GROUND_LEVEL && (mode == PLAYING || mode == MARIO)) {
			crash();
		}

		if (mode == MARIO) {
			int frameDifference = frameNumber - marioStartFrame;
			final int f = 160;
			final int j = f + 60;
			List<Integer> fireballFrames = Arrays.asList(f, f + 10, f + 20, j + 60, j + 70, j + 80);
			List<Integer> standingFrames = Arrays.asList(f + 5, f + 15, f + 25, j + 55, j + 65, j + 75);
			if (fireballFrames.contains(frameDifference)) {
				fireball();
				mario.animateThrow();
			} else if (standingFrames.contains(frameDifference)) {
				mario.stand();
			} else if (frameDifference == j) {
				mario.jump();
			} else if (frameDifference == j + 130) {
				mario.finalJump();
			}

			// Fireballs
			for (Fireball fireball : fireballs) {
				if (bird.intersects(fireball.getBounds())) {
					crash();
				}
				fireball.update();
				fireball.draw(g, this);
			}

			// If Mario lands on the bird, Mario jumps off it and game ends
			if (bird.intersects(mario.getBounds())) {
				mario.jump();
				crash();
			}

			mario.update();
			mario.draw(g, this);

			if (mario.isFinished()) {
				mode = PLAYING;
			}
		}

		for (Pipe pipe : pipes) {
			// Draw the pipes (draw these after the backdrop)
			if (mode == PLAYING || mode == MARIO) {
				pipe.update();
			}
			pipe.draw(g, this, newGraphicsEnabled);

			// Increment the score when the bird passes between a pair of pipes
			if (pipe.getPreviousX() > bird.getX() && pipe.getX() <= bird.getX() && (mode == PLAYING || mode == MARIO)) {
				score++;
				playSound(Resources.SCORE_SOUND);
				if ((score + 3) % 50 == 0) {
					mode = MARIO;
				}
			}

			// Detect when the bird crashes into a pipe
			if ((bird.intersects(pipe.getUpperBound()) || bird.intersects(pipe.getLowerBound()))
					&& (mode == PLAYING || mode == MARIO)) {
				crash();
			}

			// Have Mario appear out of the pipe
			if (pipe.equals(marioPipe)) {
				if (mode == MARIO && pipe.getXVel() != 0 && pipe.getX() < FRAME_WIDTH - 185) {
					pipe.setXVel(0);
					mario.start();
					marioStartFrame = frameNumber;
				} else if (mode == PLAYING && pipe.getXVel() == 0) {
					pipe.setXVel(X_VELOCITY);
				}
			}
		}

		// Add new pipe when previous pipe is far enough
		Pipe lastPipe = pipes.get(pipes.size() - 1);
		if (mode == PLAYING && lastPipe.getX() < FRAME_WIDTH - 80) {
			pipes.add(new Pipe(bounds, X_VELOCITY));
		} else if (mode == MARIO && lastPipe.getX() < FRAME_WIDTH - 200) {
			pipes.add(marioPipe);
		}

		// Remove pipes that have gone off-screen
		Pipe firstPipe = pipes.get(0);
		if (firstPipe.getX() < -firstPipe.getWidth()) {
			pipes.remove(firstPipe);
		}

		// Draw the moving ground (draw this after the pipes)
		if (newGraphicsEnabled) {
			if (mode != CRASHED) {
				groundX = (groundX < -33) ? 0 : groundX + X_VELOCITY;
			}
			g.drawImage(Resources.NEW_GROUND_IMAGE, groundX, GROUND_LEVEL, this);
		} else {
			if (mode != CRASHED) {
				groundX = (groundX < -23) ? 0 : groundX + X_VELOCITY;
			}
			g.drawImage(Resources.GROUND_IMAGE, groundX, GROUND_LEVEL, this);
		}

		// Draw and animate the bird (do this after pipes and ground)
		if (!newGraphicsEnabled) {
			bird.setColor(Color.YELLOW);
		}
		switch (mode) {
			case READY:
				if (frameNumber % 7 == 0) {
					bird.animate();
				}
				break;
			case MARIO:
			case PLAYING:
				if (frameNumber % 7 == 0) {
					bird.animate();
				}
				bird.update(bounds);
				break;
			case CRASHED:
				bird.update(bounds);
				break;
		}
		bird.draw(g, this);

//		g.setColor(Color.BLACK);
//		g.drawString("ArcadeEngine 2008", 10, 12);
//		g.drawString("frame=" + mouseX, 200, 12);
//		g.drawString("frame=" + mouseY, 200, 26);

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

			// Draw the "new" label if it's a new high score
			if (newHighScore) {
				g.drawImage(Resources.NEW_BEST_IMAGE, 295, 332, 53, 26, this);
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
		if (score > highScore) {
			newHighScore = true;
			highScore = score;
			resources.writeHighScore(score);
		}
		playSound(Resources.DIE_SOUND);
		playSound(Resources.HIT_SOUND);
	}

	public void fireball() {
		double dx = bird.getX() - mario.getX();
		double dy = bird.getY() - mario.getY();
		double dir = Math.atan2(dy, dx);
		int vel = 10;
		fireballs.add(new Fireball(mario.getX(), mario.getY(), vel * Math.cos(dir), vel * Math.sin(dir)));
		mario.animateThrow();
	}

	public void restart() {
		score = 0;
		mode = READY;
		newHighScore = false;
		bird.reset();
		bird.setColor(randomBirdColor());
		pipes.clear();
		pipes.add(new Pipe(bounds, X_VELOCITY));
		playSound(Resources.SWOOSH_SOUND);
	}

	public Color randomBirdColor() {
		int random = (int) (Math.random() * 3);
		switch (random) {
			case 0:
				return Color.YELLOW;
			case 1:
				return Color.BLUE;
			default:
				return Color.RED;
		}
	}

	// -------------------------------------------------------
	// Respond to Mouse Events
	// -------------------------------------------------------
	public void mouseClicked(MouseEvent e) {
		Point p = e.getPoint();

		if (restartButton.contains(p) && mode == CRASHED) {
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
			playSound(Resources.FLY_SOUND);
			if (mode == READY) {
				mode = PLAYING;
			}
		}
		// Restart the sequence when j is typed
		else if (c == 'j') {
			keySequence = new StringBuilder("j");
		}
		// Add the typed letter to the sequence
		else {
			keySequence.append(c);
		}

		// Toggle old/new graphics
		if (keySequence.toString().equals("jiaxuan")) {
			newGraphicsEnabled = !newGraphicsEnabled;
			keySequence.setLength(0);
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

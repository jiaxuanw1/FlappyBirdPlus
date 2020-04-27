package flappybird;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.applet.AudioClip;
import java.util.ArrayList;
import java.util.List;

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
	private static final int GROUND_LEVEL = 583;

	public static final int READY = 0;
	public static final int PLAYING = 1;
	public static final int CRASHED = 2;

	// Instance Variables
	// -------------------------------------------------------
	private int score;
	private int highScore;
	private int mode;
	private int groundX;
	private final Rectangle bounds;

	private Bird bird = new Bird();
	private List<Pipe> pipes = new ArrayList<Pipe>();

	// Constructor
	// -------------------------------------------------------
	public FlappyBirdGame() { // Enter the name and width and height.
		super("Flappy Bird", FRAME_WIDTH + 15, FRAME_HEIGHT + 30);
		Resources.load();
		score = 0;
		highScore = 0;
		mode = READY;
		groundX = 0;
		bounds = new Rectangle(0, 0, FRAME_WIDTH, GROUND_LEVEL);
		pipes.add(new Pipe(bounds, X_VELOCITY));
	}

	// The renderFrame method is the one which is called each time a frame is drawn.
	// -------------------------------------------------------
	protected Graphics renderFrame(Graphics g) {
		// Draw backdrop image
		g.drawImage(Resources.BACKDROP_IMAGE, 0, 0, FRAME_WIDTH, FRAME_HEIGHT, this);

		// Detect when the bird hits the ground
		if (bird.getY() + bird.getHeight() >= GROUND_LEVEL) {
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
			}

			// Detect when the bird crashes into a pipe
			if (bird.intersects(pipe.getUpperBound()) || bird.intersects(pipe.getLowerBound())) {
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

		// Draw the game over screen
		if (mode == CRASHED) {
			g.drawImage(Resources.GAME_OVER_SCREEN, 50, 120, 400, 400, this);
		}

//		// General Text (Draw this last to make sure it's on top.)
//		g.setColor(Color.BLACK);
//		g.drawString("ArcadeEngine 2008", 10, 12);
//		g.drawString("mouseX=" + mouseX, 200, 12);
//		g.drawString("mouseY=" + mouseY, 200, 26);

		// Draw the score
		g.setColor(Color.WHITE);
		g.setFont(Resources.FONT);
		int scoreLen = (int) g.getFontMetrics().getStringBounds(Integer.toString(score), g).getWidth();
		if (mode == CRASHED) {
			int scoreStart = 415 - scoreLen;
			g.drawString(Integer.toString(score), scoreStart, 325);
			// Draw the high score
			int highScoreLen = (int) g.getFontMetrics().getStringBounds(Integer.toString(highScore), g).getWidth();
			int highScoreStart = 415 - highScoreLen;
			g.drawString(Integer.toString(highScore), highScoreStart, 400);
		} else {
			int start = FRAME_WIDTH / 2 - scoreLen / 2;
			g.drawString(Integer.toString(score), start, 50);
		}

		return g;
	}
	// --end of renderFrame method--

	public void crash() {
		mode = CRASHED;
		highScore = (score > highScore) ? score : highScore;
	}

	public void restart() {
		score = 0;
		mode = READY;
		bird.reset();
		pipes.clear();
		pipes.add(new Pipe(bounds, X_VELOCITY));
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
		}
	}

	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {

	}

	// -------------------------------------------------------
	// Initialize Sounds
	// -------------------------------------------------------
//-----------------------------------------------------------------------
	/*
	 * Music section... To add music clips to the program, do four things. 1. Make a
	 * declaration of the AudioClip by name ... AudioClip clipname; 2. Actually
	 * make/get the .wav file and store it in the same directory as the code. 3. Add
	 * a line into the initMusic() function to load the clip. 4. Use the play(),
	 * stop() and loop() functions as needed in your code.
	 * //-----------------------------------------------------------------------
	 */
	AudioClip themeMusic;
	AudioClip bellSound;

	public void initMusic() {
		themeMusic = loadClip("under.wav");
		bellSound = loadClip("ding.wav");
	}

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}// --end of ArcadeDemo class--

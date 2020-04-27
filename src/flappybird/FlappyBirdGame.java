package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.applet.AudioClip;
import java.util.ArrayList;
import java.util.List;

import arcade.AnimationPanel;

/**
 * Class FlappyBirdGame This class contains code for Flappy Bird.
 * 
 * Adapted from the AppletAE demo from years past.
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

	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	public static Image BIRD_IMAGE = toolkit.getImage("bird.png");
	public static Image PIPE_IMAGE = toolkit.getImage("pipes.png");
	public static Image BACKDROP_IMAGE = toolkit.getImage("backdrop.jpg");
	public static Image GROUND_IMAGE = toolkit.getImage("ground.jpg");

	// Instance Variables
	// -------------------------------------------------------
	private int score;
	private int highScore;
	private int mode;
	private int groundX;
	private int prevBirdX;
	private Rectangle bounds;

	Bird bird = new Bird();
	List<Pipe> pipes = new ArrayList<Pipe>();

	// Constructor
	// -------------------------------------------------------
	public FlappyBirdGame() { // Enter the name and width and height.
		super("Flappy Bird", FRAME_WIDTH, FRAME_HEIGHT);
		score = 0;
		highScore = 0;
		mode = READY;
		groundX = 0;
		prevBirdX = bird.getX();
		bounds = new Rectangle(0, 0, FRAME_WIDTH, GROUND_LEVEL);
		pipes.add(new Pipe(bounds, X_VELOCITY));
	}

	// The renderFrame method is the one which is called each time a frame is drawn.
	// -------------------------------------------------------
	protected Graphics renderFrame(Graphics g) {
		// Draw backdrop image
		g.drawImage(BACKDROP_IMAGE, 0, 0, FRAME_WIDTH, FRAME_HEIGHT, this);

		// Detect when the bird hits the ground
		if (bird.getY() + bird.getHeight() >= GROUND_LEVEL) {
			crash();
		}

		// Draw the pipes (draw these after the backdrop)
		for (Pipe pipe : pipes) {
			if (mode == PLAYING) {
				pipe.animate();
			}
			pipe.draw(g, this);

			// Increment the score when the bird passes between a pair of pipes
			if (prevBirdX < pipe.getX() && bird.getX() >= pipe.getX()) {
				score++;
			}

			// Detect when the bird crashes into a pipe
			if (bird.intersects(pipe.getUpperBound()) || bird.intersects(pipe.getLowerBound())) {
				crash();
			}
		}

		// Add new pipe
		Pipe lastPipe = pipes.get(pipes.size() - 1);
		if (lastPipe.getX() < FRAME_WIDTH - 100 && mode == PLAYING) {
			pipes.add(new Pipe(bounds, X_VELOCITY));
		}

		Pipe firstPipe = pipes.get(0);
		if (firstPipe.getX() < -firstPipe.getWidth()) {
			pipes.remove(firstPipe);
		}

		// Draw the moving ground (draw this after the pipes)
		if (mode != CRASHED) {
			groundX = (groundX < -23) ? 0 : groundX + X_VELOCITY;
		}
		g.drawImage(GROUND_IMAGE, groundX, GROUND_LEVEL, this);

		// Draw the bird
		if (mode != READY) {
			bird.animate(bounds);
		}
		bird.draw(g, this);

		// General Text (Draw this last to make sure it's on top.)
		g.setColor(Color.BLACK);
		g.drawString("ArcadeEngine 2008", 10, 12);
		g.drawString("mouseX=" + mouseX, 200, 12);
		g.drawString("mouseY=" + mouseY, 200, 26);

		prevBirdX = bird.getX();

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
		pipes = new ArrayList<Pipe>();
		pipes.add(new Pipe(bounds, X_VELOCITY));
	}

	// -------------------------------------------------------
	// Respond to Mouse Events
	// -------------------------------------------------------
	public void mouseClicked(MouseEvent e) {

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

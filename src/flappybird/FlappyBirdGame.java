package flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
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

    private static final double NUM_SCALE = 3.4;
    private static final int BIG_NUM_OVERLAP = 5;
    private static final int SMALL_NUM_OVERLAP = -2; // negative for more spacing

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

    private boolean buttonPressed;

    private final Rectangle bounds;
    private Rectangle restartButton;
    private final Image[] BIG_NUMS;
    private final Image[] SMALL_NUMS;

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
        dark = Math.random() < 0.5;
        keySequence = new StringBuilder();

        bounds = new Rectangle(0, 0, FRAME_WIDTH, GROUND_LEVEL);
        restartButton = new Rectangle(158, 475, 184, 103);

        bird = new Bird();
        bird.setColor(randomBirdColor());
        pipes = new ArrayList<Pipe>();
        pipes.add(new Pipe(bounds, X_VELOCITY));
        marioPipe = new Pipe(bounds, X_VELOCITY, -330);
        mario = new Mario(marioPipe);
        fireballs = new ArrayList<Fireball>();
        marioStartFrame = -9999;

        BIG_NUMS = new Image[] { 
                Resources.BIG_ZERO, 
                Resources.BIG_ONE, 
                Resources.BIG_TWO, 
                Resources.BIG_THREE,
                Resources.BIG_FOUR, 
                Resources.BIG_FIVE, 
                Resources.BIG_SIX, 
                Resources.BIG_SEVEN, 
                Resources.BIG_EIGHT,
                Resources.BIG_NINE };

        SMALL_NUMS = new Image[] { 
                Resources.SMALL_ZERO, 
                Resources.SMALL_ONE, 
                Resources.SMALL_TWO,
                Resources.SMALL_THREE, 
                Resources.SMALL_FOUR, 
                Resources.SMALL_FIVE, 
                Resources.SMALL_SIX,
                Resources.SMALL_SEVEN, 
                Resources.SMALL_EIGHT, 
                Resources.SMALL_NINE };
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
            List<Integer> standingFrames = Arrays.asList(f + 5, f + 15, f + 25, j + 65, j + 75, j + 85);
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
                groundX = (groundX < -30) ? 0 : groundX + X_VELOCITY;
            }
            g.drawImage(Resources.NEW_GROUND_IMAGE, groundX, GROUND_LEVEL, this);
        } else {
            if (mode != CRASHED) {
                groundX = (groundX < -20) ? 0 : groundX + X_VELOCITY;
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
            if (newGraphicsEnabled) {
                g.drawImage(Resources.NEW_GAME_OVER_TEXT, 80, 115, 340, 74, this);
                g.drawImage(Resources.NEW_GAME_OVER_MIDDLE, 50, 230, 400, 202, this);
                if (buttonPressed) {
                    g.drawImage(Resources.REPLAY_BUTTON_IMAGE, 158, 478, 184, 103, this);
                } else {
                    g.drawImage(Resources.REPLAY_BUTTON_IMAGE, 158, 475, 184, 103, this);
                }
            } else {
                g.drawImage(Resources.GAME_OVER_TEXT, 84, 130, 333, 67, this);
                g.drawImage(Resources.GAME_OVER_MIDDLE, 50, 232, 400, 202, this);
                if (buttonPressed) {
                    g.drawImage(Resources.OK_BUTTON_IMAGE, 179, 503, 142, 50, this);
                } else {
                    g.drawImage(Resources.OK_BUTTON_IMAGE, 179, 500, 142, 50, this);
                }
            }

            // Draw the score
            int scoreStart = 412 - getScoreImgLen(score, SMALL_NUMS, SMALL_NUM_OVERLAP);
            drawScore(score, scoreStart, 290, SMALL_NUMS, SMALL_NUM_OVERLAP, g, this);

            // Draw the high score
            int highScoreStart = 412 - getScoreImgLen(highScore, SMALL_NUMS, SMALL_NUM_OVERLAP);
            drawScore(highScore, highScoreStart, 365, SMALL_NUMS, SMALL_NUM_OVERLAP, g, this);

            // Draw the medal
            int medalX = 96;
            int medalY = 305;
            if (highScore >= 40) {
                g.drawImage(Resources.PLATINUM_MEDAL, medalX, medalY, 77, 77, this);
            } else if (highScore >= 30) {
                g.drawImage(Resources.GOLD_MEDAL, medalX, medalY, 77, 77, this);
            } else if (highScore >= 20) {
                g.drawImage(Resources.SILVER_MEDAL, medalX, medalY, 77, 77, this);
            } else if (highScore >= 10) {
                g.drawImage(Resources.BRONZE_MEDAL, medalX, medalY, 77, 77, this);
            }

            // Draw the "new" label if it's a new high score
            if (newHighScore) {
                g.drawImage(Resources.NEW_BEST_IMAGE, 290, 333, 57, 25, this);
            }
        } else {
            // Draw the score
            if (newGraphicsEnabled) {
                int start = FRAME_WIDTH / 2 - getScoreImgLen(score, BIG_NUMS, BIG_NUM_OVERLAP) / 2;
                drawScore(score, start, 55, BIG_NUMS, BIG_NUM_OVERLAP, g, this);
            } else {
                int start = FRAME_WIDTH / 2 - getScoreImgLen(score, SMALL_NUMS, SMALL_NUM_OVERLAP) / 2;
                drawScore(score, start, 40, SMALL_NUMS, SMALL_NUM_OVERLAP, g, this);
            }

            // Draw the Get Ready text
            if (mode == READY) {
                if (newGraphicsEnabled) {
                    g.drawImage(Resources.NEW_READY_TEXT, 88, 160, 325, 88, this);
                } else {
                    g.drawImage(Resources.READY_TEXT, 100, 145, 300, 76, this);
                }
            }
        }

        return g;
    }
    // --end of renderFrame method--

    /**
     * Returns the width, in pixels, of a number drawn with images for each of its
     * digits and a specified overlap between each digit.
     * <p>
     * An {@code Image} array must be specified that contains an image for each
     * digit, in the order 0-9.
     * <p>
     * When specifying the overlap, a larger number creates more overlap. This means
     * that a negative overlap adds space between each digit.
     * 
     * @param score     the number to get the length of
     * @param numImages array containing an image for each digit
     * @param overlap   the amount to overlap, in pixels
     * @return the total width of the drawn score
     */
    public int getScoreImgLen(int score, Image[] numImages, int overlap) {
        int scoreLen = 0;
        String[] scoreDigits = Integer.toString(score).split("");

        Image[] digitImages = new Image[scoreDigits.length];
        for (int i = 0; i < digitImages.length; i++) {
            int digit = Integer.parseInt(scoreDigits[i]);
            digitImages[i] = numImages[digit];
            scoreLen += digitImages[i].getWidth(this);
        }

        scoreLen *= NUM_SCALE;
        scoreLen -= (scoreDigits.length - 1) * overlap;
        return scoreLen;
    }

    /**
     * Draws a number to the screen with images for each of its digits and a
     * specified overlap between each digit.
     * <p>
     * An {@code Image} array must be specified that contains an image for each
     * digit, in the order 0-9.
     * <p>
     * When specifying the overlap, a larger number creates more overlap. This means
     * that a negative overlap adds space between each digit.
     * 
     * @param score     the number to draw
     * @param x         the <i>x</i> coordinate
     * @param y         the <i>y</i> coordinate
     * @param numImages array containing an image for each digit
     * @param overlap   the amount to overlap, in pixels
     * @param g         the {@code Graphics} object to be drawn on
     * @param io        the {@code ImageObserver} object to be notified
     */
    public void drawScore(int score, int x, int y, Image[] numImages, int overlap, Graphics g, ImageObserver io) {
        String[] scoreDigits = Integer.toString(score).split("");

        Image[] digitImages = new Image[scoreDigits.length];
        for (int i = 0; i < digitImages.length; i++) {
            int digit = Integer.parseInt(scoreDigits[i]);
            digitImages[i] = numImages[digit];
        }

        int digitX = x;
        for (Image img : digitImages) {
            g.drawImage(img, digitX, y, (int) (NUM_SCALE * img.getWidth(io)), (int) (NUM_SCALE * img.getHeight(io)),
                    io);
            digitX += NUM_SCALE * img.getWidth(io) - overlap;
        }
    }

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
        dark = Math.random() < 0.5;
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

    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();

        buttonPressed = restartButton.contains(p);
    }

    public void mouseReleased(MouseEvent e) {
        buttonPressed = false;
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
        } else {
            // Restart the sequence when j or r is typed
            if (c == 'j' || c == 'r') {
                keySequence.setLength(0);
            }
            // Add the typed letter to the sequence
            keySequence.append(c);
        }

        // Toggle old/new graphics
        if (keySequence.toString().equals("jiaxuan")) {
            newGraphicsEnabled = !newGraphicsEnabled;
            keySequence.setLength(0);

            if (newGraphicsEnabled) {
                restartButton = new Rectangle(158, 475, 184, 103);
            } else {
                restartButton = new Rectangle(179, 500, 142, 50);
            }
        }
        // Reset the high score
        else if (keySequence.toString().equals("reset")) {
            highScore = 0;
            resources.writeHighScore(0);
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

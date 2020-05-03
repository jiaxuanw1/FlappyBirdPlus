package flappybird;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * {@code Resources} class for loading the required images and font.
 */
public class Resources {

	// Resources
	// -------------------------------------------------------
	public static Image BIRD_WING_UP;
	public static Image BIRD_WING_MID;
	public static Image BIRD_WING_DOWN;

	public static Image PIPE_IMAGE;
	public static Image BACKDROP_IMAGE;
	public static Image GROUND_IMAGE;
	public static Image READY_IMAGE;

	public static Image GAME_OVER_SCREEN;
	public static Image NEW_HIGH_SCORE_IMAGE;
	public static Image BRONZE_MEDAL;
	public static Image SILVER_MEDAL;
	public static Image GOLD_MEDAL;
	public static Image PLATINUM_MEDAL;

	public static Clip DIE_SOUND;
	public static Clip HIT_SOUND;
	public static Clip SCORE_SOUND;
	public static Clip SWOOSH_SOUND;
	public static Clip FLY_SOUND;

	public static Font FONT;

	/**
	 * Loads all of the resources for use in the project.
	 */
	public static void load() {
		try {
			BIRD_WING_UP = ImageIO.read(new File("src/resources/images/bird1.png"));
			BIRD_WING_MID = ImageIO.read(new File("src/resources/images/bird2.png"));
			BIRD_WING_DOWN = ImageIO.read(new File("src/resources/images/bird3.png"));

			PIPE_IMAGE = ImageIO.read(new File("src/resources/images/pipes.png"));
			BACKDROP_IMAGE = ImageIO.read(new File("src/resources/images/backdrop.png"));
			GROUND_IMAGE = ImageIO.read(new File("src/resources/images/ground.jpg"));
			READY_IMAGE = ImageIO.read(new File("src/resources/images/get_ready.png"));

			GAME_OVER_SCREEN = ImageIO.read(new File("src/resources/images/game_over.png"));
			NEW_HIGH_SCORE_IMAGE = ImageIO.read(new File("src/resources/images/new_highscore.jpg"));
			BRONZE_MEDAL = ImageIO.read(new File("src/resources/images/bronze_medal.jpg"));
			SILVER_MEDAL = ImageIO.read(new File("src/resources/images/silver_medal.jpg"));
			GOLD_MEDAL = ImageIO.read(new File("src/resources/images/gold_medal.jpg"));
			PLATINUM_MEDAL = ImageIO.read(new File("src/resources/images/platinum_medal.jpg"));

			DIE_SOUND = loadAudioClip(new File("src/resources/sounds/sfx_die.wav"), -15.0f);
			HIT_SOUND = loadAudioClip(new File("src/resources/sounds/sfx_hit.wav"), -15.0f);
			SCORE_SOUND = loadAudioClip(new File("src/resources/sounds/sfx_point.wav"), -15.0f);
			SWOOSH_SOUND = loadAudioClip(new File("src/resources/sounds/sfx_swooshing.wav"), -15.0f);
			FLY_SOUND = loadAudioClip(new File("src/resources/sounds/sfx_wing.wav"), -15.0f);

			FONT = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/04B_19__.TTF")).deriveFont(40.0f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(FONT);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Reads the high score saved in the text file.
	 * 
	 * @return the high score
	 */
	public static int readHighScore() {
		File file = new File("src/resources/high_score.txt");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String highScore = reader.readLine();
			reader.close();
			return Integer.parseInt(highScore);
		} catch (FileNotFoundException e) {
			try {
				file.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write('0');
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return 0;
		} catch (NumberFormatException e) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
				writer.write('0');
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * Writes the new high score to the text file.
	 * 
	 * @param score the new high score
	 */
	public static void writeHighScore(int score) {
		File file = new File("src/resources/high_score.txt");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(Integer.toString(score));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a {@code Clip} containing the audio data from a {@code File}.
	 * 
	 * @param file the {@code File} in which the audio is stored
	 * @param gain the audio gain to apply, in decibels
	 * @return a {@code Clip} with the audio data
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws LineUnavailableException
	 */
	private static Clip loadAudioClip(File file, float gain)
			throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		AudioInputStream ais = AudioSystem.getAudioInputStream(file);
		Clip clip = AudioSystem.getClip();
		clip.open(ais);
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(gain);
		return clip;
	}

}

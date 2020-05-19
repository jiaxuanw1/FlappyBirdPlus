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
import java.io.InputStream;
import java.net.URL;

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

	private ClassLoader classLoader = getClass().getClassLoader();

	// Resources
	// -------------------------------------------------------
	public static Image BIRD_YELLOW_WING_MID;
	public static Image BIRD_YELLOW_WING_DOWN;
	public static Image BIRD_YELLOW_WING_UP;
	public static Image BIRD_BLUE_WING_MID;
	public static Image BIRD_BLUE_WING_DOWN;
	public static Image BIRD_BLUE_WING_UP;
	public static Image BIRD_RED_WING_MID;
	public static Image BIRD_RED_WING_DOWN;
	public static Image BIRD_RED_WING_UP;

	public static Image PIPE_IMAGE;
	public static Image BACKDROP_IMAGE;
	public static Image GROUND_IMAGE;
	public static Image READY_IMAGE;
	public static Image GAME_OVER_SCREEN;
	public static Image NEW_BEST_IMAGE;

	public static Image DARK_BACKDROP_IMAGE;
	public static Image NEW_PIPE_IMAGE;
	public static Image NEW_GROUND_IMAGE;

	public static Image BRONZE_MEDAL;
	public static Image SILVER_MEDAL;
	public static Image GOLD_MEDAL;
	public static Image PLATINUM_MEDAL;

	public static Image MARIO_STANDING;
	public static Image MARIO_JUMPING;
	public static Image MARIO_THROWING;
	public static Image FIREBALL_IMAGE;

	public static Clip DIE_SOUND;
	public static Clip HIT_SOUND;
	public static Clip SCORE_SOUND;
	public static Clip SWOOSH_SOUND;
	public static Clip FLY_SOUND;

	public static Clip MARIO_FIREBALL_SOUND;
	public static Clip MARIO_JUMP_SOUND;
	public static Clip MARIO_PIPE_SOUND;

	public static Font FONT;

	/**
	 * Loads all of the resources for use in the project.
	 */
	public void load() {
		try {
			BIRD_YELLOW_WING_MID = ImageIO.read(classLoader.getResource("images/bird_yellow_1.png"));
			BIRD_YELLOW_WING_DOWN = ImageIO.read(classLoader.getResource("images/bird_yellow_2.png"));
			BIRD_YELLOW_WING_UP = ImageIO.read(classLoader.getResource("images/bird_yellow_3.png"));
			BIRD_BLUE_WING_MID = ImageIO.read(classLoader.getResource("images/bird_blue_1.png"));
			BIRD_BLUE_WING_DOWN = ImageIO.read(classLoader.getResource("images/bird_blue_2.png"));
			BIRD_BLUE_WING_UP = ImageIO.read(classLoader.getResource("images/bird_blue_3.png"));
			BIRD_RED_WING_MID = ImageIO.read(classLoader.getResource("images/bird_red_1.png"));
			BIRD_RED_WING_DOWN = ImageIO.read(classLoader.getResource("images/bird_red_2.png"));
			BIRD_RED_WING_UP = ImageIO.read(classLoader.getResource("images/bird_red_3.png"));

			PIPE_IMAGE = ImageIO.read(classLoader.getResource("images/pipes.png"));
			BACKDROP_IMAGE = ImageIO.read(classLoader.getResource("images/backdrop.png"));
			GROUND_IMAGE = ImageIO.read(classLoader.getResource("images/ground.jpg"));
			READY_IMAGE = ImageIO.read(classLoader.getResource("images/get_ready.png"));
			GAME_OVER_SCREEN = ImageIO.read(classLoader.getResource("images/game_over.png"));
			NEW_BEST_IMAGE = ImageIO.read(classLoader.getResource("images/new_highscore.jpg"));

			DARK_BACKDROP_IMAGE = ImageIO.read(classLoader.getResource("images/new_backdrop.png"));
			NEW_PIPE_IMAGE = ImageIO.read(classLoader.getResource("images/new_pipes.png"));
			NEW_GROUND_IMAGE = ImageIO.read(classLoader.getResource("images/new_ground.png"));

			BRONZE_MEDAL = ImageIO.read(classLoader.getResource("images/bronze_medal.jpg"));
			SILVER_MEDAL = ImageIO.read(classLoader.getResource("images/silver_medal.jpg"));
			GOLD_MEDAL = ImageIO.read(classLoader.getResource("images/gold_medal.jpg"));
			PLATINUM_MEDAL = ImageIO.read(classLoader.getResource("images/platinum_medal.jpg"));

			MARIO_STANDING = ImageIO.read(classLoader.getResource("images/mario_standing.png"));
			MARIO_JUMPING = ImageIO.read(classLoader.getResource("images/mario_jumping.png"));
			MARIO_THROWING = ImageIO.read(classLoader.getResource("images/mario_throwing.png"));
			FIREBALL_IMAGE = ImageIO.read(classLoader.getResource("images/fireball.png"));

			DIE_SOUND = loadAudioClip(classLoader.getResource("sounds/sfx_die.wav"), -15.0f);
			HIT_SOUND = loadAudioClip(classLoader.getResource("sounds/sfx_hit.wav"), -15.0f);
			SCORE_SOUND = loadAudioClip(classLoader.getResource("sounds/sfx_point.wav"), -15.0f);
			SWOOSH_SOUND = loadAudioClip(classLoader.getResource("sounds/sfx_swooshing.wav"), -15.0f);
			FLY_SOUND = loadAudioClip(classLoader.getResource("sounds/sfx_wing.wav"), -15.0f);

			MARIO_FIREBALL_SOUND = loadAudioClip(classLoader.getResource("sounds/smb3_fireball.wav"), -15.0f);
			MARIO_JUMP_SOUND = loadAudioClip(classLoader.getResource("sounds/smb3_jump.wav"), -15.0f);
			MARIO_PIPE_SOUND = loadAudioClip(classLoader.getResource("sounds/smb3_pipe.wav"), -15.0f);

			InputStream is = getClass().getResourceAsStream("/fonts/04B_19__.TTF");
			FONT = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(40.0f);
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
	public int readHighScore() {
		File file = new File("high_score.txt");
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
	public void writeHighScore(int score) {
		File file = new File("high_score.txt");
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
	 * @param url  the {@code URL} that points to the audio file
	 * @param gain the audio gain to apply, in decibels
	 * @return a {@code Clip} with the audio data
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws LineUnavailableException
	 */
	private static Clip loadAudioClip(URL url, float gain)
			throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		AudioInputStream ais = AudioSystem.getAudioInputStream(url);
		Clip clip = AudioSystem.getClip();
		clip.open(ais);
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(gain);
		return clip;
	}

}

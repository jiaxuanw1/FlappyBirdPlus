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
	public static Image READY_TEXT;
	public static Image GAME_OVER_SCREEN;
	public static Image GAME_OVER_TEXT;
	public static Image GAME_OVER_MIDDLE;
	public static Image OK_BUTTON_IMAGE;
	public static Image NEW_BEST_IMAGE;

	public static Image DARK_BACKDROP_IMAGE;
	public static Image NEW_PIPE_IMAGE;
	public static Image NEW_GROUND_IMAGE;
	public static Image NEW_READY_TEXT;
	public static Image NEW_GAME_OVER_TEXT;
	public static Image NEW_GAME_OVER_MIDDLE;
	public static Image REPLAY_BUTTON_IMAGE;

	public static Image BRONZE_MEDAL;
	public static Image SILVER_MEDAL;
	public static Image GOLD_MEDAL;
	public static Image PLATINUM_MEDAL;

	public static Image BIG_ZERO;
	public static Image BIG_ONE;
	public static Image BIG_TWO;
	public static Image BIG_THREE;
	public static Image BIG_FOUR;
	public static Image BIG_FIVE;
	public static Image BIG_SIX;
	public static Image BIG_SEVEN;
	public static Image BIG_EIGHT;
	public static Image BIG_NINE;

	public static Image SMALL_ZERO;
	public static Image SMALL_ONE;
	public static Image SMALL_TWO;
	public static Image SMALL_THREE;
	public static Image SMALL_FOUR;
	public static Image SMALL_FIVE;
	public static Image SMALL_SIX;
	public static Image SMALL_SEVEN;
	public static Image SMALL_EIGHT;
	public static Image SMALL_NINE;

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
			BIRD_YELLOW_WING_MID = ImageIO.read(getResource("images/bird_yellow_1.png"));
			BIRD_YELLOW_WING_DOWN = ImageIO.read(getResource("images/bird_yellow_2.png"));
			BIRD_YELLOW_WING_UP = ImageIO.read(getResource("images/bird_yellow_3.png"));
			BIRD_BLUE_WING_MID = ImageIO.read(getResource("images/bird_blue_1.png"));
			BIRD_BLUE_WING_DOWN = ImageIO.read(getResource("images/bird_blue_2.png"));
			BIRD_BLUE_WING_UP = ImageIO.read(getResource("images/bird_blue_3.png"));
			BIRD_RED_WING_MID = ImageIO.read(getResource("images/bird_red_1.png"));
			BIRD_RED_WING_DOWN = ImageIO.read(getResource("images/bird_red_2.png"));
			BIRD_RED_WING_UP = ImageIO.read(getResource("images/bird_red_3.png"));

			PIPE_IMAGE = ImageIO.read(getResource("images/pipes.png"));
			BACKDROP_IMAGE = ImageIO.read(getResource("images/backdrop.png"));
			GROUND_IMAGE = ImageIO.read(getResource("images/ground.jpg"));
			READY_TEXT = ImageIO.read(getResource("images/get_ready.png"));
			GAME_OVER_SCREEN = ImageIO.read(getResource("images/game_over.png"));
			GAME_OVER_TEXT = ImageIO.read(getResource("images/game_over_text.png"));
			GAME_OVER_MIDDLE = ImageIO.read(getResource("images/game_end_middle.png"));
			OK_BUTTON_IMAGE = ImageIO.read(getResource("images/ok_button.png"));
			NEW_BEST_IMAGE = ImageIO.read(getResource("images/new_best.png"));

			DARK_BACKDROP_IMAGE = ImageIO.read(getResource("images/new_backdrop.png"));
			NEW_PIPE_IMAGE = ImageIO.read(getResource("images/new_pipes.png"));
			NEW_GROUND_IMAGE = ImageIO.read(getResource("images/new_ground.png"));
			NEW_READY_TEXT = ImageIO.read(getResource("images/new_get_ready.png"));
			NEW_GAME_OVER_TEXT = ImageIO.read(getResource("images/new_game_over_text.png"));
			NEW_GAME_OVER_MIDDLE = ImageIO.read(getResource("images/new_game_end_middle.png"));
			REPLAY_BUTTON_IMAGE = ImageIO.read(getResource("images/replay_button.png"));

			BRONZE_MEDAL = ImageIO.read(getResource("images/bronze_medal.png"));
			SILVER_MEDAL = ImageIO.read(getResource("images/silver_medal.png"));
			GOLD_MEDAL = ImageIO.read(getResource("images/gold_medal.png"));
			PLATINUM_MEDAL = ImageIO.read(getResource("images/platinum_medal.png"));

			BIG_ZERO = ImageIO.read(getResource("images/font_big_0.png"));
			BIG_ONE = ImageIO.read(getResource("images/font_big_1.png"));
			BIG_TWO = ImageIO.read(getResource("images/font_big_2.png"));
			BIG_THREE = ImageIO.read(getResource("images/font_big_3.png"));
			BIG_FOUR = ImageIO.read(getResource("images/font_big_4.png"));
			BIG_FIVE = ImageIO.read(getResource("images/font_big_5.png"));
			BIG_SIX = ImageIO.read(getResource("images/font_big_6.png"));
			BIG_SEVEN = ImageIO.read(getResource("images/font_big_7.png"));
			BIG_EIGHT = ImageIO.read(getResource("images/font_big_8.png"));
			BIG_NINE = ImageIO.read(getResource("images/font_big_9.png"));

			SMALL_ZERO = ImageIO.read(getResource("images/font_small_0.png"));
			SMALL_ONE = ImageIO.read(getResource("images/font_small_1.png"));
			SMALL_TWO = ImageIO.read(getResource("images/font_small_2.png"));
			SMALL_THREE = ImageIO.read(getResource("images/font_small_3.png"));
			SMALL_FOUR = ImageIO.read(getResource("images/font_small_4.png"));
			SMALL_FIVE = ImageIO.read(getResource("images/font_small_5.png"));
			SMALL_SIX = ImageIO.read(getResource("images/font_small_6.png"));
			SMALL_SEVEN = ImageIO.read(getResource("images/font_small_7.png"));
			SMALL_EIGHT = ImageIO.read(getResource("images/font_small_8.png"));
			SMALL_NINE = ImageIO.read(getResource("images/font_small_9.png"));

			MARIO_STANDING = ImageIO.read(getResource("images/mario_standing.png"));
			MARIO_JUMPING = ImageIO.read(getResource("images/mario_jumping.png"));
			MARIO_THROWING = ImageIO.read(getResource("images/mario_throwing.png"));
			FIREBALL_IMAGE = ImageIO.read(getResource("images/fireball.png"));

			DIE_SOUND = loadAudioClip(getResource("sounds/sfx_die.wav"), -15.0f);
			HIT_SOUND = loadAudioClip(getResource("sounds/sfx_hit.wav"), -15.0f);
			SCORE_SOUND = loadAudioClip(getResource("sounds/sfx_point.wav"), -15.0f);
			SWOOSH_SOUND = loadAudioClip(getResource("sounds/sfx_swooshing.wav"), -15.0f);
			FLY_SOUND = loadAudioClip(getResource("sounds/sfx_wing.wav"), -15.0f);

			MARIO_FIREBALL_SOUND = loadAudioClip(getResource("sounds/smb3_fireball.wav"), -15.0f);
			MARIO_JUMP_SOUND = loadAudioClip(getResource("sounds/smb3_jump.wav"), -15.0f);
			MARIO_PIPE_SOUND = loadAudioClip(getResource("sounds/smb3_pipe.wav"), -15.0f);

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
	 * Returns a {@code URL} object for reading the resource with the given name.
	 * 
	 * @param name the resource name
	 * @return {@code URL} object that points to the resource
	 */
	private URL getResource(String name) {
		return getClass().getClassLoader().getResource(name);
	}

	/**
	 * Returns a {@code Clip} object containing the audio data from a file
	 * referenced by a {@code URL}.
	 * 
	 * @param url  {@code URL} object that points to the audio file
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

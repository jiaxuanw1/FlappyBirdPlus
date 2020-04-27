package flappybird;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * {@code Resources} class for loading the required images and font.
 */
public class Resources {

	public static Image BIRD_IMAGE;
	public static Image PIPE_IMAGE;
	public static Image BACKDROP_IMAGE;
	public static Image GROUND_IMAGE;

	public static Image GAME_OVER_SCREEN;
	public static Image BRONZE_MEDAL;
	public static Image SILVER_MEDAL;
	public static Image GOLD_MEDAL;
	public static Image PLATINUM_MEDAL;

	public static Font FONT;

	public static void load() {
		try {
			BIRD_IMAGE = ImageIO.read(new File("src/resources/images/bird.png"));
			PIPE_IMAGE = ImageIO.read(new File("src/resources/images/pipes.png"));
			BACKDROP_IMAGE = ImageIO.read(new File("src/resources/images/backdrop.jpg"));
			GROUND_IMAGE = ImageIO.read(new File("src/resources/images/ground.jpg"));

			GAME_OVER_SCREEN = ImageIO.read(new File("src/resources/images/game_over.png"));
			BRONZE_MEDAL = ImageIO.read(new File("src/resources/images/bronze_medal.jpg"));
			SILVER_MEDAL = ImageIO.read(new File("src/resources/images/silver_medal.jpg"));
			GOLD_MEDAL = ImageIO.read(new File("src/resources/images/gold_medal.jpg"));
			PLATINUM_MEDAL = ImageIO.read(new File("src/resources/images/platinum_medal.jpg"));

			FONT = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/04B_19__.TTF")).deriveFont(40f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(FONT);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}

	}

}

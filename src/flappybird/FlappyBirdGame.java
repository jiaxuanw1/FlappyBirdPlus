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
 * Class FlappyBirdGame
 * This class contains code for Flappy Bird.
 * 
 * Adapted from the AppletAE demo from years past. 
 */
public class FlappyBirdGame extends AnimationPanel 
{

    //Constants
    //-------------------------------------------------------
	private static final int FRAME_WIDTH = 500;
	private static final int FRAME_HEIGHT = 700;
	private static final int X_VELOCITY = -5; // must be negative to move left
	private static final int GROUND_LEVEL = 588;
	
	public static final int READY = 0;
	public static final int PLAYING = 1;
	public static final int CRASHED = 2;
	
	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	public static Image BIRD_IMAGE = toolkit.getImage("bird.png");
	public static Image PIPE_IMAGE = toolkit.getImage("pipe"); //CHANGE THIS!!!!
	public static Image BACKDROP = toolkit.getImage("backdrop.jpg");
	public static Image GROUND = toolkit.getImage("ground.jpg");
		
    //Instance Variables
    //-------------------------------------------------------
	private int highScore;
	private int mode;
	private int groundX;
	
	Bird bird = new Bird();
	List<Pipe> pipes = new ArrayList<Pipe>();

    //Constructor
    //-------------------------------------------------------
    public FlappyBirdGame()
    {   //Enter the name and width and height.  
        super("ArcadeDemo", FRAME_WIDTH, FRAME_HEIGHT);
        highScore = 0;
        mode = READY;
        groundX = 0;
        pipes.add(new Pipe(GROUND_LEVEL, X_VELOCITY));
    }
       
    //The renderFrame method is the one which is called each time a frame is drawn.
    //-------------------------------------------------------
    protected Graphics renderFrame(Graphics g) 
    {
    	// Draw backdrop image
    	g.drawImage(BACKDROP, 0, 0, FRAME_WIDTH, FRAME_HEIGHT, this);
    	
    	// Draw the pipes (draw these after the backdrop)
    	for (Pipe pipe : pipes) {
    		
    	}
    	
    	// Draw the moving ground (draw this after the pipes)
    	if (mode != CRASHED) {
    		groundX = (groundX < -23) ? 0 : groundX + X_VELOCITY;
    	}
    	g.drawImage(GROUND, groundX, GROUND_LEVEL, this);
    	
    	// Draw the bird
    	Rectangle bounds = new Rectangle(0, 0, FRAME_WIDTH, GROUND_LEVEL);
    	bird.animate(bounds);
    	bird.draw(g, this);
    	
        //General Text (Draw this last to make sure it's on top.)
        g.setColor(Color.BLACK);
        g.drawString("ArcadeEngine 2008", 10, 12);
        g.drawString("mouseX=" + mouseX, 200, 12);
        g.drawString("mouseY=" + mouseY, 200, 26);
        
        return g;
    }//--end of renderFrame method--
    
    //-------------------------------------------------------
    //Respond to Mouse Events
    //-------------------------------------------------------
    public void mouseClicked(MouseEvent e)  
    { 

    }
    
    //-------------------------------------------------------
    //Respond to Keyboard Events
    //-------------------------------------------------------
    public void keyTyped(KeyEvent e) 
    {
        char c = e.getKeyChar();
        
        // Make the bird fly up when spacebar is pressed
        if (c == ' ' && mode != CRASHED) {
        	bird.fly();
        	mode = PLAYING;
        }
    }
    
    public void keyPressed(KeyEvent e)
    {

    }

    public void keyReleased(KeyEvent e)
    {

    }
    
    //-------------------------------------------------------
    //Initialize Sounds
    //-------------------------------------------------------
//-----------------------------------------------------------------------
/*  Music section... 
 *  To add music clips to the program, do four things.
 *  1.  Make a declaration of the AudioClip by name ...  AudioClip clipname;
 *  2.  Actually make/get the .wav file and store it in the same directory as the code.
 *  3.  Add a line into the initMusic() function to load the clip. 
 *  4.  Use the play(), stop() and loop() functions as needed in your code.
//-----------------------------------------------------------------------*/
    AudioClip themeMusic;
    AudioClip bellSound;
    
    public void initMusic() 
    {
		themeMusic = loadClip("under.wav");
		bellSound = loadClip("ding.wav");
    }

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}// --end of ArcadeDemo class--

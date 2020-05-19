package arcade;

/**
 * Class ArcadeDemo
 * This class contains demos of many of the things you might
 * want to use to make an animated arcade game.
 * 
 * Adapted from the AppletAE demo from years past. 
 */


import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.applet.AudioClip;   
import java.util.ArrayList;


public class ArcadeDemo extends AnimationPanel 
{

    //Constants
    //-------------------------------------------------------

    //Instance Variables
    //-------------------------------------------------------
    PongBall ball = new PongBall();
    BouncyBall bouncer = new BouncyBall();
    ArrayList<Projectile> laserArray = new ArrayList<Projectile>();
    boolean zPressed;

    //Constructor
    //-------------------------------------------------------
    public ArcadeDemo()
    {   //Enter the name and width and height.  
        super("ArcadeDemo", 640, 480);
        zPressed = false;
    }
       
    //The renderFrame method is the one which is called each time a frame is drawn.
    //-------------------------------------------------------
    protected Graphics renderFrame(Graphics g) 
    {
        //Draw a square that is stationary on the screen.
        g.setColor(Color.BLUE);
        g.fillRect(220,120,50,50);
        
        //Draw a circle that follows the mouse.
        g.setColor(Color.BLACK);
        g.fillOval(mouseX-10, mouseY-10, 20,20);
        
        //Draw a ball that bounces around the screen.
        g.drawImage(ballImage,ball.getX(),ball.getY(),this);
        ball.animate();
        
        //Draw a ball with gravity! 
        bouncer.animate(g.getClipBounds());
        bouncer.draw(g);

        //Draw any 'lasers' that have been fired (spacebar).
        for(Projectile p : laserArray)
        {
            //Drawing element z from the array
            g.setColor(Color.YELLOW);
            g.fillRect(p.getX(), p.getY(), 5,15);
            p.animate();           
        }
        
        //General Text (Draw this last to make sure it's on top.)
        g.setColor(Color.BLACK);
        g.drawString("ArcadeEngine 2008", 10, 12);
        g.drawString("frame="+mouseX,200,12);
        g.drawString("frame="+mouseY,200,26);
        if(zPressed) g.drawString("Hooray!", 400, 400);
        
        return g;
    }//--end of renderFrame method--
    
    //-------------------------------------------------------
    //Respond to Mouse Events
    //-------------------------------------------------------
    public void mouseClicked(MouseEvent e)  
    { 
        ball.setFrozen(!ball.isFrozen()); //Toggle the ball's frozen status.
    }
    
    //-------------------------------------------------------
    //Respond to Keyboard Events
    //-------------------------------------------------------
    public void keyTyped(KeyEvent e) 
    {
        char c = e.getKeyChar();
        //Use the IJKM keys to move the PongBall
        if(c=='i' || c=='I') 
            ball.moveUp();
        if(c=='j' || c=='J') 
            ball.moveLeft();
        if(c=='k' || c=='K') 
            ball.moveRight();
        if(c=='m' || c=='M') 
            ball.moveDown();
            
        if(c==' ') //Fire a projectile when spacebar pressed.  
        {
            Projectile tempstar = new Projectile();
            tempstar.fireWeapon(mouseX,mouseY);           
            laserArray.add(tempstar);            
        }
    }
    
    public void keyPressed(KeyEvent e)
    {
        int v = e.getKeyCode();
        
        if(v == KeyEvent.VK_BACK_SPACE)
            ball.setY(400);
        if(v == KeyEvent.VK_Z)
            zPressed = true;
    }

    public void keyReleased(KeyEvent e)
    {
        int v = e.getKeyCode();
        
        if(v == KeyEvent.VK_Z)
            zPressed = false;
    }
    
    
    //-------------------------------------------------------
    //Initialize Graphics
    //-------------------------------------------------------
//-----------------------------------------------------------------------
/*  Image section... 
 *  To add a new image to the program, do three things.
 *  1.  Make a declaration of the Image by name ...  Image imagename;
 *  2.  Actually make the image and store it in the same directory as the code.
 *  3.  Add a line into the initGraphics() function to load the file. 
//-----------------------------------------------------------------------*/
    Image ballImage;        
    Image starImage;
    
    public void initGraphics() 
    {      
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        
        ballImage = toolkit.getImage("ball.gif");
        starImage = toolkit.getImage("star.gif");
    } //--end of initGraphics()--
    
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
                if(themeMusic != null)
                    themeMusic.loop(); //This would make it play!
    }

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}//--end of ArcadeDemo class--


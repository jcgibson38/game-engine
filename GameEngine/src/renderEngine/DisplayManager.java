package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager 
{
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 120;
	
	//Method to open display when game is started.
	public static void createDisplay()
	{
		//Configure context attributes.
		ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		
		//Establish attributes of users display.
		try 
		{
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.create(new PixelFormat(),attribs);
			Display.setTitle("My First Display");
		}
		catch (LWJGLException e) 
		{
			e.printStackTrace();
		}
		
		//Establish size of display in which to render game. In this case use the whole display.
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	//Method to update the display each frame.
	public static void updateDisplay()
	{
		Display.sync(FPS_CAP);
		Display.update();
	}
	
	//Method to close display on exit.
	public static void closeDisplay()
	{
		Display.destroy();
	}
}

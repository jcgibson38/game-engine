package renderEngine;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import EngineTester.MainGameLoop;

public class DisplayManager 
{
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 120;
	
	private static long lastFrameTime;
	
	private static float delta;
	
	private static boolean toClose = false;
	private static boolean inPlayMode = true;
	
	private static Frame mainFrame;
	
	//Method to open display when game is started.
	public static void createDisplay()
	{
		//Create the main UI frame
		mainFrame = new Frame("Test");
		mainFrame.setLayout(new GridBagLayout());
		
		//Frame Listener
		mainFrame.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent windowEvent)
			{
				closeRequested();
	        }
	    });
		
		//Create the frames menu
		createMenu();
		
		//Constraints
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		
		//Create the canvas.
		Canvas canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		mainFrame.add(canvas,c);
		
		createSidePanel(c);
		
		//Enable frame.
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		//Establish attributes of users display.
		try 
		{
			//Configure context attributes.
			ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
			
			//Create the display.
			Display.setParent(canvas);
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.create(new PixelFormat(),attribs);
			
			//Establish size of display in which to render game. In this case use the whole display.
			GL11.glViewport(0, 0, WIDTH, HEIGHT);
		}
		catch (LWJGLException e) 
		{
			e.printStackTrace();
		}
		
		lastFrameTime = getCurrentTime();
	}
	
	private static void createMenu()
	{
		//Create file Menu
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		MenuItem saveMenuItem = new MenuItem("Save");
		MenuItem exitMenuItem = new MenuItem("Exit");
		saveMenuItem.setActionCommand("Save");
		exitMenuItem.setActionCommand("Exit");
		
		//Create menu listeners
		saveMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				
			}
		});		
		exitMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				closeRequested();
			}
		});		
		
		//Add components to menu bar
		fileMenu.add(saveMenuItem);
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);
		mainFrame.setMenuBar(menuBar);
	}
	
	private static void createSidePanel(GridBagConstraints c)
	{
		//Button Panel
		Panel btnPanel = new Panel();
		btnPanel.setLayout(new GridLayout(4,1));
		
		//Create buttons
		Button playButton = new Button(" Play ");
		Button editButton = new Button(" Edit ");
		Button addButton = new Button(" Add ");
		
		//Create list
		List entityList = new List();
		entityList.add("Tree");
		entityList.add("Grass");
		
		//Button Listeners
		playButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				System.out.println("play button pressed");
				inPlayMode = true;
				MainGameLoop.disableSelection();
			}			
		});		
		editButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				System.out.println("edit button pressed");
				inPlayMode = false;
			}			
		});
		addButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				System.out.println("add button pressed");
				MainGameLoop.addTree();
			}			
		});
		
		//Add buttons and list to panel
		btnPanel.add(playButton);
		btnPanel.add(editButton);
		btnPanel.add(entityList);
		btnPanel.add(addButton);
		
		//Create east panel
		Panel east = new Panel(new GridBagLayout());
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.ipadx = 0;
		east.add(btnPanel,c);
		
		//Add panel to frame
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.ipadx = 10;
		mainFrame.add(east,c);
	}
	
	//Method to update the display each frame.
	public static void updateDisplay()
	{
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime)/1000f;
		lastFrameTime = currentFrameTime;
	}
	
	public static float getFrameTimeSeconds()
	{
		return delta;
	}
	
	//Method to close display on exit.
	public static void closeDisplay()
	{
		Display.destroy();
		mainFrame.dispose();
        System.exit(0);
	}
	
	private static long getCurrentTime()
	{
		return Sys.getTime()*1000/Sys.getTimerResolution();
	}
	
	public static boolean isCloseRequested()
	{
		return toClose;
	}
	
	public static void closeRequested()
	{
		toClose = true;
	}
	
	public static boolean isInPlayMode()
	{
		return inPlayMode;
	}
}

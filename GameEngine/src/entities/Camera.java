package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera 
{
	private Vector3f position = new Vector3f(0,20,0);
	private float pitch = 30;
	private float yaw;
	private float roll;
	
	public Camera() 
	{

	}
	
	public void move()
	{
		//User is moving camera into the screen.
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			position.z -= 1;
		}
		
		//User is moving camera to the right.
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			position.x += 1;
		}
		
		//User is moving camera to the left.
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			position.x -= 1;
		}
		
		//User is moving camera backwards.
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			position.z += 1;
		}
	}

	public Vector3f getPosition() 
	{
		return position;
	}

	public float getPitch() 
	{
		return pitch;
	}

	public float getYaw() 
	{
		return yaw;
	}

	public float getRoll() 
	{
		return roll;
	}
	
	
}

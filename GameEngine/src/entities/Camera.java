package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera 
{
	private Vector3f position = new Vector3f(0,15,0);
	private float pitch;
	private float yaw = 45;
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
		
		//Rotate camera forwards.
		if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8))
		{
			pitch -= 1;
		}
		
		//Rotate camera backwards.
		if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2))
		{
			pitch += 1;
		}
		
		//Rotate camera left.
		if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4))
		{
			yaw -= 1;
		}
		
		//Rotate camera right.
		if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6))
		{
			yaw += 1;
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

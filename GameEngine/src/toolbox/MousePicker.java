package toolbox;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import terrains.Terrain;

public class MousePicker 
{
	private Vector3f currentRay;
	
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Camera camera;
	
	private static final float DISTANCE = 300.0f;
	private static final int MAXCOUNT = 300;
	
	public MousePicker(Camera camera,Matrix4f projection)
	{
		this.camera = camera;
		this.projectionMatrix = projection;
		this.viewMatrix = Maths.createViewMatrix(camera);
	}
	
	public Vector3f getCurrentRay()
	{
		return currentRay;
	}
	
	public void update()
	{
		viewMatrix = Maths.createViewMatrix(camera);
		currentRay = calculateMouseRay();
	}
	
	private Vector3f calculateMouseRay()
	{
		//Get the 2D screen coordinates of the mouse.
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX,mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x,normalizedCoords.y,-1f,1f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}
	
	private Vector2f getNormalizedDeviceCoords(float mouseX,float mouseY)
	{
		//Get the mouse position in the OpenGL coordinate system.
		float x = (2f * mouseX)/Display.getWidth() - 1f;
		float y = (2f * mouseY)/Display.getHeight() - 1f;
		return new Vector2f(x,y);
	}
	
	private Vector4f toEyeCoords(Vector4f clipCoords)
	{
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix,null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection,clipCoords,null);
		return new Vector4f(eyeCoords.x,eyeCoords.y,-1f,0f);
	}
	
	private Vector3f toWorldCoords(Vector4f eyeCoords)
	{
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x,rayWorld.y,rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}
	
	private Vector3f getPointOnRay(Vector3f ray,float distance)
	{
		Vector3f cameraPos = camera.getPosition();
		Vector3f start = new Vector3f(cameraPos.x,cameraPos.y,cameraPos.z);
		Vector3f scaledRay = new Vector3f(ray.x,ray.y,ray.z);
		scaledRay.scale(distance);
		return Vector3f.add(start,scaledRay,null);
	}
	
	public Vector3f getTerrainCoords(Terrain terrain)
	{
		Vector3f ray = getCurrentRay();
		
		return terrainSearch(ray,terrain);
	}
	
	private Vector3f terrainSearch(Vector3f ray,Terrain terrain)
	{
		int loopCount = 0;
		float distance = DISTANCE;
		
		Vector3f mid = new Vector3f();
		
		while(loopCount < MAXCOUNT)
		{
			mid = getPointOnRay(ray,distance);
			float terrainHeight = terrain.getHeightOfTerrain(mid.x,mid.z);
			if(mid.y < terrainHeight)
			{
				distance--;
			}
			else
			{
				break;
			}
		}
		//System.out.println("posstart: " + mid.x + ", " + mid.y + ", " + mid.z);
		
		return mid;
	}
}

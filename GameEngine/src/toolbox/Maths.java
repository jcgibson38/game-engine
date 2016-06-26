package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

public class Maths
{
	//Create a 4x4 transformation matrix using translation, rotation, and scale.
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale)
	{
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		
		//Translate
		Matrix4f.translate(translation, matrix, matrix);
		
		//Rotate
		Matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		
		//Scale
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		
		return matrix;		
	}
	
	public static Matrix4f createViewMatrix(Camera camera)
	{
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		//Rotate
		Matrix4f.rotate((float)Math.toRadians(camera.getPitch()),new Vector3f(1,0,0),viewMatrix,viewMatrix);
		Matrix4f.rotate((float)Math.toRadians(camera.getYaw()),new Vector3f(0,1,0),viewMatrix,viewMatrix);		
		Vector3f cameraPos = camera.getPosition();
		
		//Move the world in the direction opposite of the camera movement.
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
		
		Matrix4f.translate(negativeCameraPos,viewMatrix,viewMatrix);
		return viewMatrix;
	}
}
package renderEngine;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/*
 * Loads 3D model data into memory storing positional vertex data in VAO.
 */

public class Loader 
{
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	
	//Load vertex points of a model into VAO.
	public RawModel loadToVAO(float[] positions)
	{
		//Create empty VAO
		int vaoID = createVAO();
		//Store data into VAO
		storeDataInAttributeList(0,positions);
		//Destroy VAO
		unbindVAO();
		//Return the VAO as a RawModel.
		return new RawModel(vaoID,positions.length/3);
	}
	
	//Delete all VAO and VBO upon exit.
	public void cleanUp()
	{
		for(int vao:vaos)
		{
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo:vbos)
		{
			GL15.glDeleteBuffers(vbo);
		}
	}
	
	//Creates a new empty VAO.
	private int createVAO()
	{
		//Create empty VAO and get ID;
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	//Store data into a certain location in the VAO.
	private void storeDataInAttributeList(int attributeNumber,float[] data)
	{
		//Create VBO element and get ID.
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber,3,GL11.GL_FLOAT,false,0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
	}
	
	private void unbindVAO()
	{
		GL30.glBindVertexArray(0);
	}
	
	//Convert float array of data into float buffer to be stored in VBO.
	private FloatBuffer storeDataInFloatBuffer(float[] data)
	{
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}

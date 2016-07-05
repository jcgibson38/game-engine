package toolbox;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.TexturedModel;
import randomize.RandomGenerator;
import terrains.Terrain;

public class EntityLoader 
{
	public static List<Entity> loadEntities(TexturedModel model,String fileName,Terrain terrain)
	{
		List<Entity> entities = new ArrayList<Entity>();
		
		//Read the model positions from specified file
		List<Vector2f> positions = new ArrayList<Vector2f>();
		positions = CSVReader.loadPositions(fileName);
		
		for(Vector2f pos:positions)
		{
			//Position
			float xPos = pos.x;
			float zPos = pos.y;
			float yPos = terrain.getHeightOfTerrain(xPos,zPos);
			Vector3f position = new Vector3f(xPos,yPos,zPos);
			
			//Rotation
			float rotation = RandomGenerator.randInt(0, 180);
			
			//Generate entity
			Entity entity = new Entity(model,position,0,rotation,0,1);
			
			//Add to list
			entities.add(entity);
		}
		
		return entities;
	}
}

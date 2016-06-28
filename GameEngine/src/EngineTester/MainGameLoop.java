package EngineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import randomize.RandomGenerator;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop 
{
	public static void main(String[] args)
	{		
		DisplayManager.createDisplay();		
		Loader loader = new Loader();

		//Some grass.
		RawModel model = OBJLoader.loadObjModel("grass1", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("grass1Texture"));
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		TexturedModel staticModel = new TexturedModel(model,texture);
		
		List<Entity> grass = new ArrayList<Entity>();
		for(int i = 0;i < 500;i++)
		{
			Vector3f position = new Vector3f(RandomGenerator.randInt(0,350),0,-RandomGenerator.randInt(0,350));
			float rotation = RandomGenerator.randInt(0, 180);
			Entity entity = new Entity(staticModel,position,0,rotation,0,0.5f);
			grass.add(entity);
		}		
		
		//Setup light source
		Light light = new Light(new Vector3f(3000,2000,2000),new Vector3f(1,1,1));		
		Camera camera = new Camera();		
		
		//Terrain
		Terrain terrain = new Terrain(0,-1,loader,new ModelTexture(loader.loadTexture("grass1Texture")));
		Terrain terrain2 = new Terrain(1,-1,loader,new ModelTexture(loader.loadTexture("grass1Texture")));
		
		
		MasterRenderer renderer = new MasterRenderer();
		
		//Primary game loop.
		while(!Display.isCloseRequested())
		{
			camera.move();
			
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			
			for(Entity entity:grass)
			{
				renderer.processEntity(entity);
			}
			renderer.render(light,camera);
			DisplayManager.updateDisplay();
		}
		
		//Cleanup on exit.
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}

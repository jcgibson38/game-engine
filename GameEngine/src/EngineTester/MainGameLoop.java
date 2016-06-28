package EngineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import randomize.RandomGenerator;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop 
{
	public static void main(String[] args)
	{		
		DisplayManager.createDisplay();		
		Loader loader = new Loader();
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("texture1"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("texture2"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("texture3"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("texture4"));
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);

		//Some grass.
		RawModel model = OBJLoader.loadObjModel("grass1", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("grass1Texture"));
		texture.setShineDamper(10);
		texture.setReflectivity(0);
		TexturedModel staticModel = new TexturedModel(model,texture);		
		List<Entity> grass = new ArrayList<Entity>();
		for(int i = 0;i < 250;i++)
		{
			Vector3f position = new Vector3f(RandomGenerator.randInt(0,350),0,-RandomGenerator.randInt(0,350));
			float rotation = RandomGenerator.randInt(0, 180);
			Entity entity = new Entity(staticModel,position,0,rotation,0,1);
			grass.add(entity);
		}
		
		//Trees
		RawModel tree = OBJLoader.loadObjModel("tree2", loader);
		ModelTexture treetexture = new ModelTexture(loader.loadTexture("tree1texture"));
		treetexture.setShineDamper(100);
		treetexture.setReflectivity(0);
		TexturedModel treeModel = new TexturedModel(tree,treetexture);
		List<Entity> trees = new ArrayList<Entity>();
		for(int i = 0;i < 50;i++)
		{
			Vector3f position = new Vector3f(RandomGenerator.randInt(0,350),0,-RandomGenerator.randInt(0,350));
			float rotation = RandomGenerator.randInt(0, 180);
			Entity entity = new Entity(treeModel,position,0,rotation,0,1);
			trees.add(entity);
		}
		
		RawModel dragonModel = OBJLoader.loadObjModel("dragon",loader);
		ModelTexture dragonTexture = new ModelTexture(loader.loadTexture("DragonTexture"));
		dragonTexture.setShineDamper(10);
		dragonTexture.setReflectivity(1);
		TexturedModel playerDragon = new TexturedModel(dragonModel,dragonTexture);
		Player player = new Player(playerDragon,new Vector3f(100,0,-50),0,0,0,1);
		
		//Setup light source
		Light light = new Light(new Vector3f(3000,2000,2000),new Vector3f(1,1,1));		
		Camera camera = new Camera();		
		
		//Terrain
		Terrain terrain = new Terrain(0,-1,loader,texturePack,blendMap);
		Terrain terrain2 = new Terrain(1,-1,loader,texturePack,blendMap);
		
		
		MasterRenderer renderer = new MasterRenderer();
		
		//Primary game loop.
		while(!Display.isCloseRequested())
		{
			camera.move();
			player.move();
			
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			
			for(Entity entity:grass)
			{
				renderer.processEntity(entity);
			}
			
			for(Entity entity:trees)
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

package EngineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import OBJFileLoader.ModelData;
import OBJFileLoader.OBJFileLoader;
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
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassA"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("grassB"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassC"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("grassD"));
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);

		//Generate terrain
		Terrain terrain = new Terrain(0,-1,loader,texturePack,blendMap,"heightmap");	
		
		//Some grass.
		RawModel model = OBJLoader.loadObjModel("grass1", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("grass1Texture"));
		texture.setShineDamper(10);
		texture.setReflectivity(0);
		TexturedModel staticModel = new TexturedModel(model,texture);		
		List<Entity> grass = new ArrayList<Entity>();
		for(int i = 0;i < 250;i++)
		{
			float xPos = RandomGenerator.randInt(0,600);
			float zPos = -RandomGenerator.randInt(0,600);
			float yPos = terrain.getHeightOfTerrain(xPos,zPos);
			Vector3f position = new Vector3f(xPos,yPos,zPos);
			float rotation = RandomGenerator.randInt(0, 180);
			Entity entity = new Entity(staticModel,position,0,rotation,0,1);
			grass.add(entity);
		}
		
		//Trees
		RawModel tree = OBJLoader.loadObjModel("tree2", loader);
		ModelTexture treetexture = new ModelTexture(loader.loadTexture("test"));
		treetexture.setShineDamper(100);
		treetexture.setReflectivity(0);
		TexturedModel treeModel = new TexturedModel(tree,treetexture);
		List<Entity> trees = new ArrayList<Entity>();
		for(int i = 0;i < 50;i++)
		{
			float xPos = RandomGenerator.randInt(0,600);
			float zPos = -RandomGenerator.randInt(0,600);
			float yPos = terrain.getHeightOfTerrain(xPos,zPos);
			Vector3f position = new Vector3f(xPos,yPos,zPos);
			float rotation = RandomGenerator.randInt(0, 180);
			Entity entity = new Entity(treeModel,position,0,rotation,0,1);
			trees.add(entity);
		}
		
		ModelData data = OBJFileLoader.loadOBJ("cowboy");
		RawModel dragonModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		ModelTexture dragonTexture = new ModelTexture(loader.loadTexture("cowboyTexture"));
		dragonTexture.setShineDamper(10);
		dragonTexture.setReflectivity(1);
		TexturedModel playerDragon = new TexturedModel(dragonModel,dragonTexture);
		Player player = new Player(playerDragon,new Vector3f(100,0,-50),0,180,0,0.6f);
		
		//Setup light source
		Light light = new Light(new Vector3f(0,10000,-7000),new Vector3f(1,1,1));
		List<Light> lights = new ArrayList<Light>();
		lights.add(light);
		
		
		Camera camera = new Camera(player);			
		
		MasterRenderer renderer = new MasterRenderer();
		
		//Primary game loop.
		while(!Display.isCloseRequested())
		{
			camera.move();
			player.move(terrain);
			
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			
			for(Entity entity:grass)
			{
				renderer.processEntity(entity);
			}
			
			for(Entity entity:trees)
			{
				renderer.processEntity(entity);
			}
			renderer.render(lights,camera);
			DisplayManager.updateDisplay();
		}
		
		//Cleanup on exit.
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}

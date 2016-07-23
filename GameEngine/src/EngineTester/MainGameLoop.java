package EngineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
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
import toolbox.CSVReader;
import toolbox.EntityLoader;
import toolbox.MousePicker;

public class MainGameLoop 
{
	private static List<Entity> treeEntities;
	private static Loader loader;
	private static Terrain terrain;
	
	private static TexturedModel treeTexturedModel;
	
	private static int selectedEntity;
	
	private static boolean isSelected = false;
	
	public static void main(String[] args)
	{		
		DisplayManager.createDisplay();		
		loader = new Loader();
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassA"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("grassB"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassC"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("grassD"));
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);

		//Generate terrain
		terrain = new Terrain(0,-1,loader,texturePack,blendMap,"heightmap");	
		
		//Some grass.
		RawModel grassModel = OBJLoader.loadObjModel("grass1", loader);
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("grass1Texture"));
		grassTexture.setShineDamper(10);
		grassTexture.setReflectivity(0);
		TexturedModel grassTexturedModel = new TexturedModel(grassModel,grassTexture);
		List<Entity> grassEntities = new ArrayList<Entity>();
		grassEntities = EntityLoader.loadEntities(grassTexturedModel,"grassPositions",terrain);
		
		//Trees
		RawModel treeModel = OBJLoader.loadObjModel("tree2", loader);
		ModelTexture treeTexture = new ModelTexture(loader.loadTexture("test"));
		treeTexture.setShineDamper(100);
		treeTexture.setReflectivity(0);
		treeTexturedModel = new TexturedModel(treeModel,treeTexture);
		treeEntities = new ArrayList<Entity>();
		treeEntities = EntityLoader.loadEntities(treeTexturedModel,"treePositions",terrain);
		
		ModelData data = OBJFileLoader.loadOBJ("cowboy");
		RawModel dragonModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		ModelTexture dragonTexture = new ModelTexture(loader.loadTexture("cowboyTexture"));
		dragonTexture.setShineDamper(10);
		dragonTexture.setReflectivity(1);
		TexturedModel playerDragon = new TexturedModel(dragonModel,dragonTexture);
		Player player = new Player(playerDragon,new Vector3f(153,5,-274),0,180,0,0.6f);
		
		//Setup light sources
		List<Light> lights = new ArrayList<Light>();
		Light light1 = new Light(new Vector3f(0,1000,-7000),new Vector3f(0.4f,0.4f,0.4f));
		Light light2 = new Light(new Vector3f(185,10,-293),new Vector3f(2,0,0),new Vector3f(1,0.01f,0.002f));
		Light light3 = new Light(new Vector3f(370,17,-300),new Vector3f(0,2,2),new Vector3f(1,0.01f,0.002f));
		Light light4 = new Light(new Vector3f(293,7,-305),new Vector3f(2,2,0),new Vector3f(1,0.01f,0.002f));
		lights.add(light1);
		lights.add(light2);
		lights.add(light3);
		lights.add(light4);
		
		Camera camera = new Camera(player);			
		
		MasterRenderer renderer = new MasterRenderer(loader);
		
		MousePicker picker = new MousePicker(camera,renderer.getProjectionMatrix());
		
		//Primary game loop.
		while(!DisplayManager.isCloseRequested() && !Display.isCloseRequested())
		{
			camera.move();
			player.move(terrain);
			
			if(!DisplayManager.isInPlayMode() && isSelected)
			{
				if(selectedEntity > 0)
				{
					picker.update();
					Vector3f pickerVec = picker.getTerrainCoords(terrain);
					if(pickerVec != null){
						float newX = pickerVec.x;
						float newZ = pickerVec.z;
						float newY = pickerVec.y;
						Vector3f newPosition = new Vector3f(newX,newY,newZ);
						treeEntities.get(selectedEntity).setPosition(newPosition);
					}
					if(Mouse.isButtonDown(1))
					{
						selectedEntity = -1;
					}
				}
			}
			
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			
			for(Entity entity:grassEntities)
			{
				renderer.processEntity(entity);
			}
			
			for(Entity entity:treeEntities)
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
	
	public static void addTree()
	{		
		//Position
		float xPos = 50;
		float zPos = -50;
		float yPos = terrain.getHeightOfTerrain(xPos,zPos);
		Vector3f position = new Vector3f(xPos,yPos,zPos);
		
		//Rotation
		float rotation = RandomGenerator.randInt(0, 180);
		
		//Generate entity
		Entity entity = new Entity(treeTexturedModel,position,0,rotation,0,1);
		
		treeEntities.add(entity);
		
		selectedEntity = treeEntities.size() - 1;
		enableSelection();
	}
	
	public static void enableSelection()
	{
		isSelected = true;
	}
	
	public static void disableSelection()
	{
		isSelected = false;
	}
}

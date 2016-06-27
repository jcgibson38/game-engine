package EngineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;

public class MainGameLoop 
{
	public static void main(String[] args)
	{
		DisplayManager.createDisplay();		
		Loader loader = new Loader();
		
		//Setup the model.
		RawModel model = OBJLoader.loadObjModel("dragon", loader);		
		ModelTexture texture = new ModelTexture(loader.loadTexture("DragonTexture"));
		texture.setShineDamper(10);
		texture.setReflectivity(1);		
		TexturedModel staticModel = new TexturedModel(model,texture);	
		Entity entity = new Entity( staticModel,new Vector3f(0,-2.5f,-25),0,0,0,1 );
		
		//Setup light source
		Light light = new Light(new Vector3f(0,0,-20),new Vector3f(1,1,1));		
		Camera camera = new Camera();		
		MasterRenderer renderer = new MasterRenderer();
		
		//Primary game loop.
		while(!Display.isCloseRequested())
		{
			entity.increaseRotation(0,1,0);
			camera.move();
			renderer.processEntity(entity);
			renderer.render(light,camera);
			DisplayManager.updateDisplay();
		}
		
		//Cleanup on exit.
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}

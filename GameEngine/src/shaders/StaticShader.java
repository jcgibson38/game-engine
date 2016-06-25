package shaders;

public class StaticShader extends ShaderProgram
{
	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";

	public StaticShader()
	{
		super(VERTEX_FILE,FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() 
	{
		//Bind attribute 0 of VAO to the position input in the vertex shader.
		super.bindAttribute(0, "position");	
		//Bind attribute 1 of VAO to the textureCoords input in the vertex shader.
		super.bindAttribute(1,"textureCoords");
	}
	
}

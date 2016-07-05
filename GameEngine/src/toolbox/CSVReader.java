package toolbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

public class CSVReader 
{
	public static List<Vector2f> loadPositions(String fileName)
	{
		List<Vector2f> posList = new ArrayList<Vector2f>();
		
		FileReader fr = null;
		
		//Try to find the file.
		try
		{
			fr = new FileReader(new File("res/landscape/"+fileName+".csv"));
		} 
		catch (FileNotFoundException e)
		{
			System.err.println("Couldn't load .csv file!");
			e.printStackTrace();
		}
		
		//Read in the file
		BufferedReader reader = new BufferedReader(fr);
		String line;
		
		try
		{
			while((line = reader.readLine()) != null)
			{
				String[] vals = line.split(" ");
				Vector2f vec = new Vector2f(Float.parseFloat(vals[0]),Float.parseFloat(vals[1]));
				
				posList.add(vec);
			}
		}
		catch(Exception e)
		{
			System.err.println("Error reading file");
			e.printStackTrace();
		}
		
		return posList;
	}
}

package guardian.being;

import java.util.*;
import guardian.ui.*;

public class CellMemory {
	/** Represents the locations that the player
	 * remembers as he has perceived them already.
	 *
	 * By now its using a matrix data structure,
	 * must be replaced with a proper hashtable
	 * implementation */

	private Appearance [][][] memory;

	public CellMemory(){
		memory = new Appearance[512][512][5];
	}

	public void addCell (int x, int y, int z, Appearance what){
		memory[x][y][z] = what;
	}

	public boolean remembers (int x, int y, int z){
		return (memory[x][y][z] != null);
	}

	public Appearance remembered (int x, int y, int z){
		return memory[x][y][z];
	}

/*	private Hashtable points;

	public CellMemory(){
		points = new Hashtable(400);
	}

	public void addCell (int x, int y, int z, Appearance what){
		if (!points.containsKey(x+','+y+","+z))
			points.put(x+','+y+","+z, what);
	}

	public boolean remembers (int x, int y, int z){
		return points.containsKey(x+','+y+","+z);
	}

	public Appearance remembered (int x, int y, int z){
		return (Appearance) points.get(x+','+y+","+z);
	}
	*/
}
package guardian.world.feature;

public class StaticFeature implements Feature{
	private int[][] representation;
	private String type, aID;

	public String getType(){
		return type;
	}

	public String getID(){
		return aID;
	}

	public int getWidth(){
		return representation[0].length;
	}

	public int getHeight(){
		return representation.length;
	}

	public void set(int[][] pRepresentation, String pType, String pID){
		type = pType;
		aID = pID;
		representation = pRepresentation;
	}

	public void rotate(){
		int[][] newMatrix = new int[getWidth()][getHeight()];
		//System.out.println("Height "+ getHeight());
		//System.out.println("Width"+ getWidth());
		for (int x = 0; x < getWidth(); x++)
			for (int y = 0; y < getHeight(); y++){
				//System.out.println("X = " + x +", new x = "+ (getWidth() - y));
				//System.out.println("Y = " + y +", new y = "+ x);
				newMatrix [x][getHeight() - y -1] = representation[y][x];
				//newMatrix [x][y] = representation[y][x];
			}
		representation = newMatrix;
	}

	public int[][] getMap(){
		return representation;
	}

}
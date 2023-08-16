package guardian.world.feature;

public interface Feature {
	public String getType();
	public String getID();
	/*public int getWidth();
	public int getHeight();*/
	public int[][] getMap();
}
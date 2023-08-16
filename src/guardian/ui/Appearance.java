package guardian.ui;

public interface Appearance{
	/** Defines an abstract appearance. Used by a UI implementation
	 * A appearance represents the perception a Being has on something
	 * using a Sense, so each Being may have a SightAppearance, a SmellAppearance
	 * and so on; when a being uses his senses he may get the appearances of some
	 * other Beings or MapCells in a given range of the world, and each Appearance
	 * is drawn according to its type by the UI Implementation
	 *
	 * */

	public Appearance duplicate();

	public String getID();


}
package guardian.world;

import engine.*;
import guardian.actor.*;
import guardian.ui.*;
import util.*;

public class GMapCell{
    /*
	 *
	 */


	/* Categories of MapCells.
	 used in the world generation process */
	public static final int
		C_INFERTILE = 4,
		C_GRASS = 5,
		C_OCEAN = 6,
		C_MOUNTAIN = 7;

	/* Standard Transformations */
	public final static String BASH = "BASH",
		OPEN = "OPEN",
		CLOSE = "CLOSE";

	private boolean solid, opaque, water;
    private int category;
   	private String description, shortDescription;
   	private String ID;
    private int heightMod;
    private ActionSelector aSelector;
    private Appearance aAppearance;

    private Properties cellTransformations;
/*
    public Actor getActor(){
		/** Manufactures an Actor from the MapCell data
		 * used when the MapCell has dynamic behaviour and
		 * must be included into the world */
/*		Actor ret = new Actor();
		ret.setActionSelector(aDefaultAI);
		return ret;
	}*/

    public boolean isCategory(int w){
	    if (w == category)
			return true;
		else return false;
	}

    public GMapCell(String pID,
					Appearance pAppearance,
					String pShortDescription,
					String pDescription,
					int pCategory,
					int pHeightMod,
					ActionSelector pSelector){
    	Debug.enterMethod(this, "{Constructor}");
    	ID = pID;
    	aAppearance = pAppearance;
  	    heightMod = pHeightMod;
        category = pCategory;
    	shortDescription = pShortDescription;
    	description = pDescription;
    	aSelector = pSelector;

    	cellTransformations = new Properties();
    	Debug.exitMethod();
	}



	public GMapCell getTransform(String transform) throws EngineException {
	    Debug.enterMethod(this, "getTransform", transform);
        Property transformation = cellTransformations.getProperty(transform);
        if (transformation!= null){
            Debug.exitMethod(transformation.getValue());
            return (GMapCell) transformation.getValue();
		}

        /*Debug.exitMethod("*Exception*");
        throw new EngineException("Can't Apply " + transform +" transformation to MapCell");*/
        Debug.exitMethod("null");
        return null;
    }

    public void addTransformation(Property p){
	    Debug.enterMethod(this, "addTransformation", p);
		cellTransformations.add(p);
	}


	/*public void transform(Actor transformer, String transformation) throws EngineException{
        GMapCell afterTransform = getTransform (transformation);
        GWorld world = (GWorld) transformer.getWorld();
		if (afterTransform != null){
			world.setCell(world.indexOf(this), afterTransform);
		} else {
	        throw new EngineException("Can't apply transform " + transformation);
		}
	} */

	public int getCategory(){
		return category;
	}

	public Appearance getAppearance(){
		return aAppearance;
	}

    public String getDescription() {
        return shortDescription;
    }

    public int getHeightMod(){
        return heightMod;
    }

    public boolean isSolid(){
		return solid;
	}

	public boolean isOpaque(){
		return opaque;
	}

	public String getID(){
		return ID;
	}

	public void setOpaque (boolean value){
		opaque = value;
	}

	public void setID (String value){
		ID = value;
	}

	public void setSolid(boolean value) {
		solid = value;
	}

	public boolean isWater() {
		return water;
	}

	public void setWater(boolean value) {
		water = value;
	}
}
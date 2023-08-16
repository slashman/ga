package guardian.actor;

import engine.*;

import guardian.ui.*;
import guardian.world.*;

import util.*;

import java.util.Hashtable;


/** Contains the appearances of the Visible Actors that a Smart Actor is
 * perceiving.
 *
 *  This perception is defined by applying its active "senses",
 * which check an area to see if there are Visible Actors that have
 * an Appearance matching the sense, which are collected.
 *
 *  Each appearance must contain a position relative to the Being,
 * to allow proper rendering
 *
 **/

public class Perception {
	/** Must include some other things the being perceives<
	 * the time of the day, the far events, and even consider
	 * including the "Events", this would obligue a change in the
	 * UI Messaging.
	 * The Actions may add "Events" to the world, objects with
	 * location and description, and they may be included in the
	 * perception of the Being while catching it */

	public void perceive(PhysicalActor pActor, Appearance pAppearance, Point pLocation){
		 //Debug.enterMethod(this, "perceive(PhysicalActor:"+pActor+", Appearance:"+pAppearance+", location: "+pLocation+"");
		 perceive(pAppearance, pLocation);
		 aPhysicalActors.add(pActor);
		 //Debug.exitMethod();
	}

    public void perceive(PhysicalActor pActor, Appearance pAppearance, int x, int y){
		 //Debug.enterMethod(this, "perceive(PhysicalActor:"+pActor+", Appearance:"+pAppearance+", location: "+pLocation+"");
		 perceive(pAppearance, x,y);
			aPhysicalActors.add(pActor);
		 //Debug.exitMethod();
	}

	public void perceive (Appearance pAppearance, Point pLocation){
		 //Debug.enterMethod(this, "perceive(Appearance:"+pAppearance+", location: "+pLocation+"");
		 if (xCenter + pLocation.x >= 0 &&
		 	 xCenter + pLocation.x < perWidth &&
			yCenter + pLocation.y >=0 &&
			yCenter + pLocation.y < perHeight)
	         aAppearances[xCenter + pLocation.x][yCenter + pLocation.y] = pAppearance;
		 //Debug.exitMethod();
	}

	public void perceive (Appearance pAppearance, int x, int y){
		 //Debug.enterMethod(this, "perceive(Appearance:"+pAppearance+", location: "+pLocation+"");
		 if (xCenter + x >= 0 &&
		 	 xCenter + x < perWidth &&
			yCenter + y >=0 &&
			yCenter + y < perHeight)
	         aAppearances[xCenter + x][yCenter + y] = pAppearance;
		 //Debug.exitMethod();
	}

	public Perception(){
		 aAppearances = new Appearance[perWidth][perHeight];
		 aPhysicalActors = new VPhysicalActor();
	}

	public VPhysicalActor getPhysicalActors(){
		return aPhysicalActors;
	}

	public Appearance getAppearance(Point where){
		return aAppearances [where.x][where.y];
	}

	public Appearance getAppearance(int x, int y){
		return aAppearances [x][y];
	}

	public boolean hasPoint(Point what){
		return (aAppearances[what.x][what.y] != null);
	}


	public void erase(){
		for (int x = 0; x < aAppearances.length; x++)
			for (int y = 0; y < aAppearances[0].length; y++)
				aAppearances[x][y] = null;
//    	aAppearances = new Appearance[perWidth][perHeight];
		aPhysicalActors.removeAll();
	}

	public int getWidth(){ return perWidth;}

	public int getHeight(){ return perHeight;}

	private int perWidth = 25, perHeight = 25, xCenter = 12, yCenter = 12;
	private Appearance [][] aAppearances;
		/** Location relative to the player where there was perception,
		 * paired one by one with "aAppearances" */

	private VPhysicalActor aPhysicalActors;
	/** These are the Actors that the Being is perceiving,
	 * they are used in the "pick" methods of the AI and the UI
	 * NOTE: Must try to find a way to not let the UI or the AI
	 * get info from the reference. It exists only as a mean to pass
	 * it to the game, which will take care of it.
	 * */
}
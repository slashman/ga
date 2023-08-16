/*
 * GWorld.java
 *
 * Created on 2 de septiembre de 2004, 18:26
 */

package guardian.world;

import guardian.being.*;
import guardian.actor.*;
import guardian.item.*;
import engine.*;
import util.*;
import java.util.*;

public class GWorld extends World{
	private final static int EVENTMEMORIALSIZE = 500;
    private GTime time;

    private GMapCell [][][] world;
    private int aOverWorldIndex;
    //private TimeLine aEvents;

    private VPhysicalActor aPhysicalActors;
    private VBeing listeningBeings;


    public void init(GMapCell [][][] pMapCells, int pOverWorldIndex){
		world = pMapCells;
		aOverWorldIndex = pOverWorldIndex;
		aPhysicalActors = new VPhysicalActor();
		listeningBeings = new VBeing();
//		aEvents = new TimeLine();
	}

	public void setTime(GTime what){
		time = what;
	}

	public void addEvent(String msgYou, String msgOther, Actor performer, Action action, VActor targets, Point position){
		Debug.doAssert(position != null, "Added an event to the world with null position");
		Event e = new Event(informTime(), msgYou, msgOther, performer, action, targets, position);
		addEvent(e);
	}

	public void addEvent(Event e){
		Debug.enterMethod(this, "addEvent", e);
		for (int i = 0; i < listeningBeings.size(); i++){
			listeningBeings.elementAt(i).eventHappened(e);
		}
		Debug.exitMethod();
	}


/*
	public VEvent getEventsPastAt(Point pWhere, GTime pTime){
		/** Gets the events that happened at the point pWhere of the world
		 * after the time pTime */
	/*	VEvent vEvents = new VEvent();
		for (int i=0; i< aEvents.size(); i++){
			Event e = aEvents.elementAt(i);
			//System.out.println(e);
			Debug.doAssert(e.getPosition() != null, "Failed...");
			if (e.getPosition().equals(pWhere) && (e.getTime().equals(pTime) || e.getTime().isAfter(pTime))){
				vEvents.add(e);
			}
		}
		return vEvents;
	}   */

//	public VEvent getEventsAt(Point pWhere, GTime pTime){
		/** Gets the events that happened at the point pWhere of the world
		 * after the time pTime */
	/*	VEvent vEvents = new VEvent();
		for (int i=0; i< aEvents.size(); i++){
			Event e = aEvents.elementAt(i);
			if (e.getPosition().equals(pWhere)){
				vEvents.add(e);
			}
		}
		return vEvents;
	}
	*/
	public GBeing getBeingAt(Point p){
		PhysicalActor[] ph = getActorsAt(p);
		for (int i =0; i< ph.length; i++){
			if (ph[i] instanceof GBeing)
				return (GBeing) ph [i];
		}
		return null;
	}

	public GBeing getBeingAt(int x, int y, int z){
		PhysicalActor[] ph = getActorsAt( new Point(x,y,z));
		for (int i =0; i< ph.length; i++){
			if (ph[i] instanceof GBeing)
				return (GBeing) ph [i];
		}
		return null;
	}

	public GItem getItemAt(Point p){
		PhysicalActor[] vActors = getActorsAt(p);
        for (int i=0; i<vActors.length; i++){
			if (vActors[i] instanceof GItem)
				return (GItem) vActors[i];
		}
		return null;
	}

	public GItem[] getItemsAt(Point p){
		PhysicalActor[] vActors = getActorsAt(p);
		Vector vItems = new Vector();
		for (int i=0; i<vActors.length; i++){
			if (vActors[i] instanceof GItem)
				vItems.add(vActors[i]);
		}
		GItem [] vReturn = new GItem[vItems.size()];
		for (int i=0; i<vReturn.length; i++){
			vReturn[i] = (GItem) vItems.elementAt(i);
		}
		return vReturn;
	}

	public int [][] getIntMapCells(){
		int [][] ret = new int[getWidth(aOverWorldIndex)][getHeight(aOverWorldIndex)];
		for (int x = 0; x < getWidth(aOverWorldIndex); x++){
			for (int y = 0; y < getHeight(aOverWorldIndex); y++){
				String def = world [aOverWorldIndex][x][y].getID();
				if (def.equals("ICE"))
					ret[x][y] = 1;
				else if (def.equals("TUNDRA"))
					ret[x][y] = 2;
				else if (def.equals("OCEANIC"))
					ret[x][y] = 3;
				else if (def.equals("PRAIRIE"))
					ret[x][y] = 4;
				else if (def.equals("SEA"))
					ret[x][y] = 5;
				else if (def.equals("TAIGA"))
					ret[x][y] = 6;
				else if (def.equals("MEDIT"))
					ret[x][y] = 7;
				else if (def.equals("COAST"))
					ret[x][y] = 8;
				else if (def.equals("ASDFOREST"))
					ret[x][y] = 9;
				else if (def.equals("SAVANNA"))
					ret[x][y] = 10;
				else if (def.equals("PLUVI"))
					ret[x][y] = 11;
				else if (def.equals("STEEPE"))
					ret[x][y] = 12;
				else if (def.equals("DESERT"))
					ret[x][y] = 13;
				else if (def.equals("TROPICAL"))
					ret[x][y] = 14;
				else if (def.equals("PLATEAU"))
					ret[x][y] = 15;
				else if (def.equals("DRYFOREST"))
					ret[x][y] = 16;
				else if (def.equals("HILLS"))
					ret[x][y] = 17;
				else if (def.equals("ICEMNTN"))
					ret[x][y] = 18;
				else if (def.equals("RCKMNTN"))
					ret[x][y] = 19;
				else if (def.equals("DEEPSEA"))
					ret[x][y] = 20;
			}
		}
		return ret;
	}

	public GMapCell getMapCell(int z, int x, int y) {
		return world[z][x][y];
	}

	public GMapCell getMapCell(Point p){
		return getMapCell(p.z,p.x,p.y);
	}

	public void transform(Point p, String transformation) throws EngineException{
		GMapCell gm = getMapCell(p);
		GMapCell afterTransform = gm.getTransform (transformation);

		if (afterTransform != null){
			setCell(p, afterTransform);
		} else {
	        throw new EngineException("Can't apply transform " + transformation);
		}
	}

	public Point indexOf(GMapCell m){
		for (int z = 0; z < getDepth(); z++)
			for (int x =0; x< getWidth(z); x++)
				for (int y =0; y < getHeight(z); y++)
					if (world[z][x][y] == m)
						return new Point(x, y, z);
		return null;
	}

	public int getWidth(int level){
    	return world[level][0].length;
    }

    public int getHeight(int level){
	    return world[level].length;
    }

    public int getDepth(){
		return world.length;
	}

    public void setCell(Point where, GMapCell what){
		world[where.z][where.x][where.y] = what;
	}

	public PhysicalActor getFirstActor(int x, int y, int z){

		for (int i=0; i < aPhysicalActors.size(); i++){
			Point position = aPhysicalActors.elementAt(i).getPosition();
			if (position.x == x && position.y == y && position.z==z)
				return aPhysicalActors.elementAt(i);
		}
		return null;
	}

    public PhysicalActor[] getActorsAt(Point p){
	    /** Returns a collection of PhysicalActors which position
	     * equals the parameter */
	    VPhysicalActor v = new VPhysicalActor ();

		for (int i=0; i < aPhysicalActors.size(); i++){
			if (Point.equals(aPhysicalActors.elementAt(i).getPosition(), p)){
				v.add(aPhysicalActors.elementAt(i));
			}
		}

		PhysicalActor [] ret = new PhysicalActor[v.size()];

		for (int i=0; i<v.size(); i++){
			ret[i] = v.elementAt(i);
		}
		return ret;
	}


	public void addPhysicalActor(PhysicalActor a){
		Debug.enterMethod(this, "addPhysicalActor", a);
		super.addActor(a);
		aPhysicalActors.add(a);
		if (a instanceof GBeing){
			((GBeing)a).setLastActionTime(new GTime(time));
		}
		Debug.exitMethod();
	}

	public void addPhysicalActor(PhysicalActor a, boolean activate){
		Debug.enterMethod(this, "addPhysicalActor", a + " activate: " +activate);
		super.addActor(a, activate);
		aPhysicalActors.add(a);
		if (a instanceof GBeing){
			((GBeing)a).setLastActionTime(time);
		}
		Debug.exitMethod();
	}

	public Actor getNextActor(){
		if (actors.size() > 0)
			if (actors.size() == 1)
				time.addSeconds(actors.elementAt(0).getTime());
			else {
				time.addSeconds(actors.elementAt(0).getTime() - actors.elementAt(1).getTime());
			}
	     return super.getNextActor();
	}

	public GTime getTime(){
		return time;
	}

	public GTime informTime(){
		return new GTime(time.getYear(), time.getMonth(), time.getDay(), time.getHour(), time.getMinute(), time.getSecond());
	}

	public void removePhysicalActor(PhysicalActor what){
		Debug.enterMethod(this, "removePhysicalActor", what);
		aPhysicalActors.remove(what);
		Debug.exitMethod();
	}

}
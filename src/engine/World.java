package engine;

import java.util.*;
import util.*;

public abstract class World {
	public void activateActor(Actor who){
		/** Inserts the actor into the infinite timeline, thus allowing him
		 * to use its Action Selector to select actions and perform them */
		 Debug.enterMethod(this, "activateActor", who);
		 who.setTime(1);
		 activeActors.insert(who);
		 Debug.exitMethod();
	}

	public void disableActor(Actor who){
		/** Removes the Actor from the Active Actors list */
		Debug.enterMethod(this,"disableActor",who);
		who.setTime(1);
		activeActors.remove(who);
		Debug.exitMethod();
	}

	public void updateActionTime(Actor who, int cost){
		/** Updates the action time for an Actor and inserts him into
		 * the infinite timeline at the corresponding position */
		Debug.enterMethod(this, "updateActionTime", who);
		who.setTime(cost);
		activeActors.insert(who);
		Debug.exitMethod();
	}

	public void reactivateActor(Actor who){
		/** Inserts the actor on the timeline without setting him
		 * to be the first to act */
		Debug.enterMethod(this, "reactivateActor", who);
		activeActors.insert(who);
		Debug.exitMethod();
	}

    public Actor getNextActor(){
	    /** Returns the Actor which turn corresponds and updates the
	     * time to act of the rest of Actors according to the time it
	     * took him to Act. It also removes the actor while he performs
		 * the action. Its reinsertion is in charge of the Actor class */
	    Debug.enterMethod(this, "getNextActor");
		Actor returnable = activeActors.elementAt(0);
		int baseTime = returnable.getTime();
		for (int i=0; i<activeActors.size(); i++){
			activeActors.elementAt(i).setTime(activeActors.elementAt(i).getTime()-baseTime);
			//Debug.say (i+" "+ activeActors.elementAt(i)+","+activeActors.elementAt(i).getTime());
		}
		activeActors.remove(returnable);
		Debug.exitMethod(returnable);
		return returnable;
    }

	public void addActor(Actor a){
		/** Adds the actor to the World, as well as to the timeline */
		a.setWorld(this);
		actors.add(a);
	}

	public void addActor(Actor a, boolean activate){
		/** Adds the Actor to the world, optionally activating him at
		 * the same time */
		Debug.enterMethod(this, "addActor", a+","+activate);
		addActor(a);
		if (activate){
			activateActor(a);
		}
		Debug.exitMethod();
	}

	public void removeActor(Actor a){
		/** Removes an Actor from the World */
		Debug.enterMethod(this, "removeActor", a);
		disableActor(a);
		actors.remove(a);
		Debug.exitMethod();
	}

	protected VActor actors;
	private VActor activeActors;

    public World() {
        actors = new VActor();
        activeActors = new VActor();
    }

    public VActor getActors() {
		return actors;
	}

}
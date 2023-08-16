package engine;

import util.*;

public abstract class Actor {
	private World aWorld;
	private ActionSelector aSelector;

	int actionTime;

	public void performAction(Action a) throws EngineException{
		/**
		 * Performs the given action using *this* as the performer
		 * Takes care of concurrent Actions and reactivation of
		 * the actor inside of the world.
		 * */
		Debug.enterMethod(this, "performAction", a);
		if (a == null)
			throw new EngineException("No Action Selected");
		int actionCost = 0;
        do { // This loop takes care of concurrent Actions
            a.setPerformer(this);
            actionCost += a.getCost();
			a = a.execute();
			setTime(actionCost);
			aWorld.reactivateActor(this); /* moved this*/
		} while (a != null);
       	Debug.exitMethod();
	}

	public World getWorld() {
		return aWorld;
	}

	public void setWorld(World value) {
		aWorld = value;
	}

	public ActionSelector getActionSelector() {
		return aSelector;
	}

	public void setActionSelector(ActionSelector value) {
		aSelector = value;
	}

	public void setTime(int time){
		actionTime = time;
	}

	public int getTime(){
		return actionTime;
	}
}
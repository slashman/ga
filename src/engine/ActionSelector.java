package engine;

import util.*;

public abstract class ActionSelector {
    public abstract Action selectAction();
    public abstract Action selectReflexAction(String input);

	public Action getAction(String ID) throws EngineException{
		for (int i=0; i<actionSet.length; i++){
			if (actionSet[i].getID().equals(ID)){
				return actionSet[i];
			}
		}
		throw new EngineException ("Couldn't find Action matching "+ID+" at ActionSelector");
	}

	public void init(Action[] actionSet, String ID){
		this.actionSet = actionSet;
		this.ID = ID;
	}

	public ActionSelector clonate(){
		try {
			return (ActionSelector)super.clone();
		}catch (CloneNotSupportedException cnse){
			Debug.doAssert(false, "Error Cloning ActionSelector "+ID);
		}

		return null;
	}

	public String getID(){
		return ID;
	}

    protected Action[] actionSet;
	protected String ID;
}
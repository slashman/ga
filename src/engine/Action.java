package engine;

import util.EngineException;

public abstract class Action {

    /**
	 * Defines the abstract interface for an action
	 * It has targets that are set when the action object is selected and used
	 * during execution
	 * June 19/2004
	*/


    public abstract Action execute() throws EngineException;
    public abstract int getCost();

    protected Actor performer;
    private String ID;

	public void setPerformer(Actor what) throws EngineException{
		performer = what;
	}

	public void setID(String what){
		ID = what;
	}

	public String getID(){
		return ID;
	}
}
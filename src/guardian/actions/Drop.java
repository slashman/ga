package guardian.actions;

import engine.*;
import util.*;
import guardian.being.*;
import guardian.actions.*;

import guardian.world.*;

public class Drop extends GuardianAction{

	public String getID(){
		return "DROP";
	}

	public int getCost(){
		return 50;
	}

	public String getDescription(){
		return "Drop";
	}

	public boolean needsInventoryItem(){
		return true;
	}

	private GBeing aBeingPerformer;

 	public Action execute() throws EngineException{
	 	Debug.enterMethod(this, "execute");
	 	if (performer instanceof GBeing == false)
			throw new EngineException("Invalid performer");
        aBeingPerformer = (GBeing) performer;
        aBeingPerformer.removeFromInventory(targetInventoryItem);
        aBeingPerformer.getGWorld().addPhysicalActor(targetInventoryItem);
        targetInventoryItem.setPosition(aBeingPerformer.getPosition());
        String msgYou = "You drop the "+ targetInventoryItem.getDescription();
		String msgOther = aBeingPerformer.getDesc() + " opens the "+ targetInventoryItem.getDescription();
		aBeingPerformer.getGWorld().addEvent(new Event(aBeingPerformer.getGWorld().informTime(), msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition()));
		return null;

	}

	public String getPromptInventoryItem(){
		return "Select the item you want to drop";
	}


}
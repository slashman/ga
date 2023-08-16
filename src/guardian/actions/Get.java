package guardian.actions;

import engine.*;
import util.*;

import guardian.being.*;
import guardian.item.*;
import guardian.actor.*;
import guardian.actions.*;
import guardian.world.*;

public class Get extends GuardianAction {

	public String getID(){
		return "GET";
	}

	public boolean needsWorldItem(){
		return true;
	}

	private GBeing aBeingPerformer;

	public Action execute() throws EngineException{
		Debug.enterMethod(this, "execute");
		if (performer instanceof GBeing == false){
			Debug.exitMethod("*Throws EngineException*");
            throw new EngineException("Invalid performer");
		}

        aBeingPerformer = (GBeing) performer;
        GWorld aGWorld = aBeingPerformer.getGWorld();

		if (aBeingPerformer.canCarry(targetWorldItem)){
			try {
            	aBeingPerformer.addToInventory(targetWorldItem);
				aBeingPerformer.getGWorld().removePhysicalActor(targetWorldItem);
				String msgYou = "You pickup the "+targetWorldItem.getDescription();
				String msgOther = aBeingPerformer.getDesc() + " picks up the "+targetWorldItem.getDescription();
				aGWorld.addEvent(new Event(aGWorld.informTime(), msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition()));
			} catch (EngineException ee){
				String msgYou = "You almost pickup the "+targetWorldItem.getDescription()+", but there was no room for it";
				String msgOther = aBeingPerformer.getDesc() + " almost picks up the "+targetWorldItem.getDescription()+", but he found no room for it";
				aGWorld.addEvent(new Event(aGWorld.informTime(), msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition()));
				//Debug.exitMethod("*Throws EngineException*"+ee);
				//throw ee;
			}
		} else {
			String msgYou = "You can't pickup the "+targetWorldItem.getDescription();
			String msgOther = aBeingPerformer.getDesc() + " tried to pickup the "+targetWorldItem.getDescription();
			aGWorld.addEvent(new Event(aGWorld.informTime(), msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition()));
		}
		Debug.exitMethod(null);
		return null;
	}

    public String getDescription(){
		return "Get";
	}

	public int getCost(){
		return 5;
	}

	public String toString(){
		return getDescription() +super.toString();
	}

	public String getPromptWorldItem(){
		return "What do you want to pick up?";
	}
}
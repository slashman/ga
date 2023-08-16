package guardian.actions;

import guardian.being.*;
import guardian.world.*;
import guardian.item.*;

import engine.*;

import util.*;

public class UnEquip extends GuardianAction{
	public String getID(){
		return "UNEQUIP";
	}

	public int getCost(){
		return 50;
	}

	public String getDescription(){
		return "UnEquip";
	}

	public boolean needsBodyPart(){
		return true;
	}

	private GBeing aBeingPerformer;

 	public Action execute() throws EngineException{
		if (performer instanceof GBeing == false)
			throw new EngineException("Invalid performer");

        aBeingPerformer = (GBeing) performer;

        if (aBeingPerformer.hasBodyPart(targetBodyPart)){
	        GItem equiped = targetBodyPart.unequip();
	        aBeingPerformer.addToInventory(equiped);
			String msgYou = "You unwear the "+ equiped.getDescription() + " from your "+ targetBodyPart.getName();
			String msgOther = aBeingPerformer.getDesc() + " unwears "+ equiped.getDescription() + " from its "+targetBodyPart.getName();
			aBeingPerformer.getGWorld().addEvent(new Event(aBeingPerformer.getGWorld().informTime(), msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition()));
		} else {
			throw new EngineException("Target BodyPart doesn't belong to performer, must use equipOnOther action");
		}

		return null;
	}

	public String getPromptBodyPart(){
		return "What bodyPart do you want to unequip?";
	}

}
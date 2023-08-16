package guardian.actions;

import guardian.being.*;
import guardian.world.*;

import engine.*;

import util.*;

public class Equip extends GuardianAction{
	public String getID(){
		return "EQUIP";
	}

	public int getCost(){
		return 50;
	}

	public String getDescription(){
		return "Equip";
	}

	public boolean needsEquipmentSlot(){
		return true;
	}

	public boolean needsInventoryItem(){
		return true;
	}

	private GBeing aBeingPerformer;

 	public Action execute() throws EngineException{
		if (performer instanceof GBeing == false)
			throw new EngineException("Invalid performer");

        aBeingPerformer = (GBeing) performer;

		if (targetEquipmentSlot.getBodyPart().canContain(targetInventoryItem)){
			aBeingPerformer.removeFromInventory(targetInventoryItem);
			targetEquipmentSlot.setItem(targetInventoryItem);
			String msgYou = "You equip "+ targetInventoryItem.getDescription() + " in the "+ targetEquipmentSlot.getBodyPart().getName();
			String msgOther = aBeingPerformer.getDesc() + " equips "+ targetInventoryItem.getDescription() + " in the "+targetEquipmentSlot.getBodyPart().getName();
			aBeingPerformer.getGWorld().addEvent(new Event(aBeingPerformer.getGWorld().informTime(), msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition()));
		}


		return null;
	}

	public String getPromptBodyPart(){
		return "Where do you want to equip the "+targetInventoryItem.getDescription()+"?";
	}

	public String getPromptInventoryItem(){
		return "Which item do you want to equip?";
	}

}
package guardian.actions;

import engine.*;
import util.*;

import guardian.world.*;
import guardian.being.*;
import guardian.item.*;

public class Close extends GuardianAction {

	public String getID(){
		return "CLOSE";
	}

	public int getCost(){
		return 50;
	}

	public String getDescription(){
		return "Close";
	}

	public boolean needsNearMapCell(){
		return true;
	}

	private GBeing aBeingPerformer;

 	public Action execute() throws EngineException{
	 	Debug.enterMethod(this, "execute");
	 	if (performer instanceof GBeing == false)
			throw new EngineException("Invalid performer");
        aBeingPerformer = (GBeing) performer;
        GMapCell where = targetNearMapCellW.getMapCell(targetNearMapCellP);
	    try{

		    targetNearMapCellW.transform(targetNearMapCellP, "CLOSE");
		    String msgYou = "You close the "+where.getDescription();
			String msgOther = aBeingPerformer.getDesc() + " closes the "+where.getDescription();
			targetNearMapCellW.addEvent(new Event(targetNearMapCellW.informTime(), msgYou, msgOther, performer, this, null, targetNearMapCellP));
		}
		catch (EngineException ee){
			String msgYou = "You try to close the "+where.getDescription();
			String msgOther = aBeingPerformer.getDesc() + " tries to close the "+where.getDescription();
			targetNearMapCellW.addEvent(new Event(targetNearMapCellW.informTime(), msgYou, msgOther, performer, this, null, targetNearMapCellP));
		}
		return null;

	}

	public String getPromptNearMapCell(){
		return "What do you want to close?";
	}

}
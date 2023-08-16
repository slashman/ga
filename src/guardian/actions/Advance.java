package guardian.actions;

import engine.*;
import util.*;

import guardian.world.*;
import guardian.actor.*;
import guardian.being.*;
import guardian.item.*;
import guardian.actions.*;

public class Advance extends GuardianAction {

	private GMapCell destination;

	private GBeing aBeingPerformer;
	private GWorld aGWorld;

	public boolean needsDirection(){
		return true;
	}

	public int getCost(){
		return (int) 1200 / aBeingPerformer.getSpeed();
	}

	public String getID(){
		return "ADVANCE";
	}

	public String getDescription(){
		return null;
	}

	public void setPerformer(Actor a) throws EngineException{
		try {
        	aBeingPerformer = (GBeing) a;
        	aGWorld = (GWorld) aBeingPerformer.getWorld();
		} catch (ClassCastException cce){
            throw new EngineException("Invalid Performer trying to be assigned");
		}
		super.setPerformer(a);
	}

 	public Action execute() throws EngineException{
	 	Debug.enterMethod(this, "execute");

		Point variation = getVariation();
		Point wayPoint = Point.add(aBeingPerformer.getPosition(), variation);

		if (variation.x == 1 && aBeingPerformer.getPosition().x == aGWorld.getWidth(aBeingPerformer.getPosition().z)-1){
			wayPoint = new Point(0, aBeingPerformer.getPosition().y);
		}

		if (variation.x == -1 && aBeingPerformer.getPosition().x == 0){
			wayPoint = new Point(aGWorld.getWidth(aBeingPerformer.getPosition().z)-1, aBeingPerformer.getPosition().y);
		}

		if (variation.y == -1 && aBeingPerformer.getPosition().y == 0){
			wayPoint = new Point(aBeingPerformer.getPosition().x, aGWorld.getHeight(aBeingPerformer.getPosition().z)-1);
		}

		if (variation.y == 1 && aBeingPerformer.getPosition().y == aGWorld.getHeight(aBeingPerformer.getPosition().z)-1){
			wayPoint = new Point(aBeingPerformer.getPosition().x, 0);
		}

		destination = mapCellAt(wayPoint);

		if (crashAt(wayPoint)){
			GBeing crasher = beingAt(wayPoint);
			if (crasher == null){
				addMapCellBumpMessage(destination);
			} else {
				addBumpMessage(crasher);
			}
		} else {
			GItem[] floor = itemsAt(wayPoint);
			if (floor.length != 0){
				addStepMessage(floor);
			}

			wayPoint.z += destination.getHeightMod();
			aBeingPerformer.setPosition(wayPoint);
//			aBeingPerformer.moveItems(wayPoint);

			switch (destination.getHeightMod()){
				case -1:
					addAscendMessage(destination);
					break;
				case 1:
					addDescendMessage(destination);
					break;
			}
		}
		Debug.exitMethod(null);
		return null;
	}

	private boolean crashAt(Point p){
		GMapCell destination = mapCellAt(p);
		if (destination.isSolid()){
			return true;
		}

		if (beingAt(p) != null) return true;

		if (destination.isWater()){
			return true;
		}
		return false;
	}

	private GBeing beingAt(Point p){

    	PhysicalActor [] actors = aGWorld.getActorsAt(p);

		for (int i=0; i<actors.length; i++){
			if (actors[i] instanceof GBeing)
				return (GBeing)actors[i];
		}
		return null;
	}

	private GItem[] itemsAt(Point p){
    	return aGWorld.getItemsAt(p);
	}

	private GMapCell mapCellAt(Point p){
		return aGWorld.getMapCell(p.z, p.x, p.y);
	}

	private void addAscendMessage(GMapCell what){
					/*
		if (performer == ui.getPlayer()){
			//ui.addMessage("You ascend through the " + destination.getDescription()+ "...");
		} else {
			if (ui.getPlayerPerception().getActors().has(performer))
				ui.addMessage(performer.getDesc() + " ascends through the " + destination.getDescription()+ "...");
		}*/
	}

	private void addDescendMessage(GMapCell what){
        /*if (performer == ui.getPlayer()){
			ui.addMessage("You descend through the " + destination.getDescription()+ "...");
		} else {
			if (ui.getPlayerPerception().getActors().has(performer))
				ui.addMessage(performer.getDesc() + " descends through the " + destination.getDescription()+ "...");
		}*/
	}

	private void addStepMessage(GItem[] what){
		String msgOther = "";
		String msgYou = "";
		if (what.length == 1){
            msgYou = "You step over " + what[0].getDescription();
			msgOther = aBeingPerformer.getDesc() + " steps over " + what[0].getDescription();
		} else {
            msgYou = "You step over some items";
			msgOther = aBeingPerformer.getDesc() + " steps over some items";
		}

		aGWorld.addEvent(msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition());
	}

	private void addMapCellBumpMessage(GMapCell what){
		String msgOther = "";
		String msgYou = "";
		if (what.isWater()){
            msgOther = aBeingPerformer.getDesc()  + " tried to swim";
            msgYou = "You can't swim... yet...";
		}
		else
		if (what.isCategory(GMapCell.C_MOUNTAIN)){
            msgYou = "You can't climb the mountain";
            msgOther = aBeingPerformer.getDesc()  + " tried to climb the mountain";
		}
		else{
            msgYou = "You crash with " + what.getDescription()+ "!";
            msgOther = aBeingPerformer.getDesc()  + " crashes with " + what.getDescription()+ "!";
		}
		aGWorld.addEvent(msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition());
	}

	private void addBumpMessage(GBeing what){
		String msgYou = "You bump with " + what.getDesc()+ "!";
		String msgOther = aBeingPerformer.getDesc() + " crashes with " + what.getDesc()+ "!";
		aGWorld.addEvent(msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition());
	}

	public String toString(){
		return getDescription()+super.toString();
	}

	public String getPromptDirection(){
		return "What direction do you want to advance into?";
	}
}
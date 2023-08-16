package guardian.actions.combat;

import guardian.actions.*;

import guardian.being.*;
import engine.*;
import util.*;
import guardian.world.*;

public class ShieldBash extends GuardianAction{
	/** Bash the enemy with an Item, possibly stunning him,
	 * and bribing him into attacking */

	private GBeing aBeingPerformer;

	public boolean needsInventoryItem(){
		return true;
	}

	public boolean needsNearBeing(){
		return true;
	}

	public String getPromptNearBeing(){
		return "Who do you want to bash?";
	}

	public String getPromptInventoryItem(){
		return "What do you want to bash with?";
	}

	public Action execute() throws EngineException{
		/** The bash may push back the enemy, depending on:
		 *  	The Flexible Strenght of the performer,
		 * 		The Area of the Shield
		 * 		The Bash resistance of the shield
		 * 		The weight of the enemy
		 *
		 * If it hits a critical bodyPart, it may also stun the enemy
		 *
		 * A Shield Bashed enemy can't counter attack
		 * */
		Debug.enterMethod(this, "execute");
		aBeingPerformer = (GBeing) performer;
		int pushing = getPushing();

		if (pushing > 0){
			pushTarget(pushing);
			String msgYou = "You bash "+targetNearBeing.getDesc()+" with your "+targetInventoryItem.getDescription()+", pushing him aside.";
			String msgOther = aBeingPerformer.getDesc() + " bashes "+targetNearBeing.getDesc()+" with his "+targetInventoryItem.getDescription()+", pushing him aside.";
			System.out.println(aBeingPerformer.getGWorld());
			aBeingPerformer.getGWorld().addEvent(new Event(aBeingPerformer.getGWorld().informTime(), msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition()));
		} else {
			String msgYou = "You bash "+targetNearBeing.getDesc()+" with your "+targetInventoryItem.getDescription()+".";
			String msgOther = aBeingPerformer.getDesc() + " bashes "+targetNearBeing.getDesc()+" with his "+targetInventoryItem.getDescription()+".";
			aBeingPerformer.getGWorld().addEvent(new Event(aBeingPerformer.getGWorld().informTime(), msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition()));
		}
		/*if (stunsEnemy()){
			stunEnemy();
		} */
		Debug.exitMethod(null+"");
		return null;
	}

	public int getCost(){

		return 20;

	}

	private int getPushing(){
		Debug.enterMethod(this, "getPushing");
		double pushingIndex = getPushingIndex();
		int pushing = (int)(MathF.pow(pushingIndex,3)*0.4461 -
			   MathF.pow(pushingIndex,2)*8.7636 +
			   pushingIndex * 57.892 - 126.93)
			   *(1+targetInventoryItem.getStructStr()/30);
		if (pushing > 20) pushing = 20;
		Debug.exitMethod(pushing+"");
		return pushing;
	}

	private double getPushingIndex(){
		Debug.enterMethod(this, "getPushingIndex");
		double pi = (aBeingPerformer.getFlexStr() + (400 - targetNearBeing.getWeight()) / 40)/2;
		Debug.exitMethod(pi+"");
		return pi;
	}

	private void pushTarget (int pushing){
		Debug.enterMethod(this, "calculateNewPosition", pushing+"");
		Point variation = Point.rest(targetNearBeing.getPosition(), aBeingPerformer.getPosition());
		GWorld wor = aBeingPerformer.getGWorld();
		for (int i = 0; i<pushing; i++){
			Point destLocation = Point.add(
				targetNearBeing.getPosition(),
				variation
			);
			GMapCell destination = wor.getMapCell(destLocation);
			if (destination.isSolid()){
				if (i>5){
					// Blow the mapCell
					try {
						wor.transform(destLocation, GMapCell.BASH);
						String msgYou = "You bash down the "+destination.getDescription()+" because of the bash!";
						String msgOther = aBeingPerformer.getDesc() + " bashes down the "+destination.getDescription()+" because of a bash!";
						aBeingPerformer.getGWorld().addEvent(new Event(aBeingPerformer.getGWorld().informTime(), msgYou, msgOther, performer, this, null, destLocation));
					} catch (EngineException ee){
						//Can't be blown
						String msgYou = "You crash with the "+destination.getDescription()+"!";
						String msgOther = aBeingPerformer.getDesc() + " crashes with the "+destination.getDescription()+"!";
						aBeingPerformer.getGWorld().addEvent(new Event(aBeingPerformer.getGWorld().informTime(), msgYou, msgOther, performer, this, null, destLocation));
						targetNearBeing.setPosition(destLocation);
						Debug.exitMethod();
						return;
					}
				} else {
					//Stop there
					String msgYou = "You crash with the "+destination.getDescription()+"!";
					String msgOther = aBeingPerformer.getDesc() + " crashes with the "+destination.getDescription()+"!";
					aBeingPerformer.getGWorld().addEvent(new Event(aBeingPerformer.getGWorld().informTime(), msgYou, msgOther, performer, this, null, destLocation));
					targetNearBeing.setPosition(destLocation);
					Debug.exitMethod();
					return;
				}
			}
			targetNearBeing.setPosition(destLocation);
		}

		Debug.exitMethod();
	}

}
package guardian.actions.combat;

import guardian.actions.*;

import guardian.being.*;
import guardian.item.*;
import engine.*;
import util.*;
import guardian.world.*;

public class Bash extends GuardianAction{
	private GBeing aBeingPerformer;

	public boolean needsBodyPart(){
		/** The bodyPart with which the performer will attack */
		return true;
	}

	public boolean needsNearBeing(){
		/** The being that will be bashed*/
		return true;
	}

	public String getPromptNearBeing(){
		return "Who do you want to Bash?";
	}

	public String getPromptBodyPart(){
		return "What do you want to Bash with?";
	}

	public Action execute() throws EngineException{
		/** Bash: Uses an item, so that its weight, integrity and
		 *  additional harm index cause "impact" wounds into one
		 *  or more bodyparts of the target, or his equipment is damaged,
		 *  or both.
		 *
		 *  The bodyparts on which the blow lands are determined by the
		 *  instance of the target.
		 *
		 * The global integrity falls with each impact.
		 *
		 * If the blow is strong enough, the bodyPart may be broken
		 * instead of adding an impact wound to it, droping its
		 * equiped item to the floor if it is being handled, and reducing
		 * the global integrity in a high defree.
		 *
		 * The integrity of the weapon is reduced when it hits hard
		 * materials, in a factor determined by its material.
		 *
		 * Additionally, the blow may cause Graze wounds due to
		 * additional harm accesories resumed into a grazingIndex.
		 *
		 * If the blow misses the target, it may destroy Items and
		 * MapCells as well.
		 *
		 * Has a very high hit rate, which depens on the size of
		 * the item.
		 *
		 * Even if the blow hits the armor, little of the impact
		 * can be reduced, thus making this move very powerful
		 * against armored enemies where no cut or pierce are of use.*/
			 /*
		Debug.enterMethod(this, "execute");

		aBeingPerformer = (GBeing) performer;
		Action x = targetNearBeing.getReflexAction("Bash");
		if (x!=null)
			targetNearBeing.performAction(x);
		if (hitsTarget()){
			BodyPart landingBodyPart = getLandingBodyPart();
			GItem landingArmor = getLandingArmor(landingBodyPart);
			GItem weapon = targetBodyPart.getEquipedItem();
			if (weapon == null)
				weapon = targetBodyPart; /** Hits with bare fists */
			/*int bashStrength = getBashStrength(weapon);
			bashStrength = MathF.rand(bashStrength-5, bashStrength+10);;
			if (landingArmor != null){
				if (bashStrength > landingArmor.getBashTolerance()){
                	landingArmor.crash(bashStrength, weapon);
                	weapon.crash(bashStrength, landingArmor);
					bashStrength = landingArmor.absorbImpact(bashStrength);
				} else {
					String msgYou = "Your blow is absorbed by the "+ targetNearBeing.getDesc() +"'s "+landingArmor.getDescription()+"!!!!";
					String msgOther = aBeingPerformer.getDesc() +" blow is absorbed by the "+ targetNearBeing.getDesc() +"'s "+landingArmor.getDescription()+"!!!!!";
					aBeingPerformer.getGWorld().addEvent(new Event(aBeingPerformer.getGWorld().informTime(), msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition()));
					bashStrength = 0;
				}
			}

			if (bashStrength > landingBodyPart.getBashTolerance()){
				weapon.crash(bashStrength, landingBodyPart);
				if (bashStrength > landingBodyPart.getRequiredBreakStrength()){
					landingBodyPart.breakBodyPart();
					String msgYou = "You break the "+ targetNearBeing.getDesc() +"'s "+landingBodyPart.getDescription()+"!!!!";
					String msgOther = aBeingPerformer.getDesc() +" breaks the "+ targetNearBeing.getDesc() +"/'s "+landingBodyPart.getDescription()+"!!!!!";
					aBeingPerformer.getGWorld().addEvent(new Event(aBeingPerformer.getGWorld().informTime(), msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition()));
				} else {
                	landingBodyPart.addWound(new Impact(bashStrength, landingBodyPart));
                    String msgYou = "You impact the "+ targetNearBeing.getDesc()+"'s "+landingBodyPart.getDescription()+" causing "+bashStrength+" pain";
                    String msgOther = aBeingPerformer.getDesc()+ " impacts the "+ targetNearBeing.getDesc()+"'s "+landingBodyPart.getDescription()+" causing "+cutGravity+" pain";
					aBeingPerformer.getGWorld().addEvent(new Event(aBeingPerformer.getGWorld().informTime(), msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition()));
				}
			} else {
				String msgYou = "Your slash was shrugged off by "+ targetNearBeing.getDesc();
                String msgOther = aBeingPerformer.getDesc()+ " slash was shrugged off by "+ targetNearBeing.getDesc();
				aBeingPerformer.getGWorld().addEvent(new Event(aBeingPerformer.getGWorld().informTime(), msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition()));
			}
		} else {
			String msgYou = "You tried to slash at "+ targetNearBeing.getDesc()+".";
			String msgOther = aBeingPerformer.getDesc() + " tried to slash at "+ targetNearBeing.getDesc()+".";
			aBeingPerformer.getGWorld().addEvent(new Event(aBeingPerformer.getGWorld().informTime(), msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition()));
		}
			   */
		Debug.exitMethod(null+"");
		return null;
	}

	public int getCost(){
		Debug.enterMethod(this, "getCost");
		Debug.exitMethod(20+"");
		return 20;
	}

	private boolean hitsTarget(){
		Debug.enterMethod(this, "hitsTarget");
		/** Depends on
		 * 	the size of the item 0.25
		 * 	the performer agility 0.125
		 *  the size of the target 0.125
		 *  the weigth of the item 0.125
		 *  the DII of the target 0.25
		 *  the AII of the performer 0.125
		 */
		 /*
		 GItem weapon = targetBodyPart.getEquipedItem();
		 if (weapon == null)
         	weapon = targetBodyPart;

		 double itemSize = weapon.getSize();

		 Debug.say("itemSize "+itemSize);
		 double itemSizeQ = MathF.poly(itemSize, new double[] {-0.000005, 0.0005, 0.0048, 0.0166});
		 Debug.say("bladeQ "+bladeQ);

		 int targetDII = targetNearBeing.getDII();
		 Debug.say("targetDII "+targetDII);
		 double targetDIIQ = MathF.poly(targetDII, new double[] {-0.001, -0.000000000000007, 0.0042, 1});
         Debug.say("targetDIIQ "+targetDIIQ);

		 int performerAII = ((GBeing)performer).getAII();
		 Debug.say("performerAII " + performerAII);
		 double performerAIIQ = MathF.poly(performerAII, new double[]{ 0.0028, -0.0454, 0.2781, 0.0000000000001});
		 Debug.say("performerAIIQ " + performerAIIQ);

		 int targetSize = targetNearBeing.getSize();
		 Debug.say("targetSize "+targetSize);
		 double targetSizeQ = MathF.poly(targetSize, new double[] {0.000003, -0.0005, 0.0294, 0.000000000000003});
		 Debug.say("targetSizeQ "+targetSizeQ);

		 int combatSkill = ((GBeing)performer).getCombatSkill();
		 Debug.say("combatSkill "+combatSkill);
		 double combatSkillQ = MathF.poly(combatSkill, new double[] {0.000001, -0.00009, 0.0084, 0.0000000000001});
		 Debug.say("combatSkillQ "+combatSkillQ);

		 double hitQ = bladeQ * 0.3 + targetSizeQ * 0.1 + targetDIIQ * 0.1 + performerAIIQ * 0.2 + combatSkillQ * 0.3;
		 Debug.say("hitQ "+hitQ);
		 double chance = MathF.poly(hitQ, new double[] {104.17, -187.5, 183.33, 0});;
		 Debug.say("chance "+chance);

		 boolean result = MathF.rand(0, 100) < (int)(chance*100);
		 Debug.exitMethod(result+"");*/
		 return false;
	}

	private BodyPart getLandingBodyPart(){
		Debug.enterMethod(this, "getLandingBodyPart");
		/** The bodyPart at which the performer aims depends on his
		 * fighting style. */
		BodyPart ret = targetNearBeing.getBodyParts().elementAt(MathF.rand(0, targetNearBeing.getBodyParts().size()-1));
		Debug.exitMethod(ret);
		return ret;
	}

	private GItem getLandingArmor(BodyPart where){
		Debug.enterMethod(this, "getLandingArmor", where);
		/** The blow may hit into the armor of the bodyPart*/
		GItem armor = where.getEquipedItem();
		if (armor != null){
            if (MathF.rand(0,100)< armor.getBodyPartCoverage()){
	            Debug.exitMethod(armor);
				return armor;
			}
		}
		Debug.exitMethod(null);
		return null;
	}

	private int getCutGravity(GItem weapon){
		Debug.enterMethod(this, "getCutGravity", weapon);
		/** The gravity of the cut depends on the quality of the weapon
		 * The attack effectiveness is reduced by hard or ringed armour.*/
		int ret = (weapon.getBladeSharpness() * 3 + ((GBeing)performer).getCombatSkill() * 2)/5;
		Debug.exitMethod(ret+"");
		return ret;
	}



}
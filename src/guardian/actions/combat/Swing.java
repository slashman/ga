package guardian.actions.combat;

import guardian.actions.*;

import guardian.being.*;
import guardian.item.*;
import engine.*;
import util.*;
import guardian.world.*;

public class Swing extends GuardianAction{
	private GBeing aBeingPerformer;
	public boolean needsDirection(){
		return true;
	}

	public String getPromptDirection(){
		return "Where do you want to swing your weapon at?";
	}

	public Action execute() throws EngineException{
		/** Uses the item equiped in the performer weaponPart to produce
		 * an impact in the Beings, Items and MapCells standing in the adjacent
		 * cells in the targetDirection.
		 *
		 * General Considerations:
		 *   The strength of the impact is the average of the performer flexible
		 *   strength and the hitIndex of the weapon.
		 *
		 *   The effect of this action is a pole collition against an Item or
		 *   BodyPart or MapCell.
		 *
		 *   The hit chance is 100% against static targets.
		 *
		 * Considerations with Beings:
		 *   The bodyparts on which the blow lands are determined by the
		 * 	 instance of the being.
		 *
		 *   The hit chance is the average of the performer agility and his skill
		 *   with the weapon.
		 *
		 */

		Debug.enterMethod(this, "execute");

		aBeingPerformer = (GBeing) performer;
		GWorld aWorld = aBeingPerformer.getGWorld();

		GItem aWeapon = aBeingPerformer.getEquipedWeapon();

		int impactStrength = MathF.avg(aBeingPerformer.getFlexStr(), aWeapon.getSwingIndex());

		Point targetPoint = Point.add(aBeingPerformer.getPosition(), getVariation());

		GBeing targetBeing = aWorld.getBeingAt(targetPoint);
		GItem targetItem = aWorld.getItemAt(targetPoint);
		GMapCell targetMapCell = aWorld.getMapCell(targetPoint);

		if (targetBeing != null){
			Action x = targetBeing.getReflexAction("Swing");
			if (x!=null)
				targetBeing.performAction(x);
			if (hitsTarget(aBeingPerformer.getEquipedWeapon(), targetBeing)){
				BodyPart landingBodyPart = getLandingBodyPart(targetBeing); // What part of the body the blow lands into
				GItem landingArmor = getLandingArmor(landingBodyPart); // See if the blow is covered by the armor
				System.out.println("You hit the "+ targetBeing.getDesc()+"'s "+landingBodyPart.getDescription()+" ("+impactStrength+") " + targetBeing.getIntegrity() +" with "+aWeapon+") at "+targetBeing.getPosition());
				String msgYou = "You hit the "+ targetBeing.getDesc()+"'s "+landingBodyPart.getDescription()+" ("+impactStrength+") at "+targetBeing.getPosition();
				String msgOther = aBeingPerformer.getDesc()+ " impacts the "+ targetBeing.getDesc()+"'s "+landingBodyPart.getDescription()+" ("+impactStrength+")";
				aBeingPerformer.getGWorld().addEvent(msgYou, msgOther, performer, this, null, targetBeing.getPosition());
				int residualStrength = landingArmor.receivePoleBlow(aWeapon, impactStrength, aWorld);
				if (residualStrength > 0 && landingArmor != landingBodyPart) {
					landingBodyPart.receivePoleBlow(aWeapon, residualStrength, aBeingPerformer.getGWorld());
				}
				System.out.println("You hit the "+ targetBeing.getDesc()+"'s "+landingBodyPart.getDescription()+" ("+impactStrength+") " + targetBeing.getIntegrity() +" with "+aWeapon);
				targetBeing.checkStatus();
			} else { // The blow missed
				String msgYou = "You missed "+ targetBeing.getDesc();
				String msgOther = aBeingPerformer.getDesc() + " missed "+ targetBeing.getDesc();
				aBeingPerformer.getGWorld().addEvent(msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition());
			}
		} else
		if (targetItem != null){
			String msgYou = "You hit the "+ targetItem.getDescription();
			String msgOther = aBeingPerformer.getDesc()+ " hits the "+ targetItem.getDescription();
			aBeingPerformer.getGWorld().addEvent(msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition());
			targetItem.receivePoleBlow(aWeapon, impactStrength, aWorld);
			if (targetItem.getWeight() < impactStrength * 10){
				// Displace the item in the direction
			}
		} else {
			String msgYou = "You hit the "+ targetMapCell.getDescription();
			String msgOther = aBeingPerformer.getDesc()+ " hits the "+ targetMapCell.getDescription();
			aBeingPerformer.getGWorld().addEvent(msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition());
			GMapCell transform = targetMapCell.getTransform("Impact."+impactStrength);
			if (transform != null){
				aWorld.setCell(targetPoint, transform);
			}
		}
		Debug.exitMethod(null+"");
		return null;
	}

	public int getCost(){
		return 20;
	}

	private boolean hitsTarget(GItem weapon, GBeing targetBeing){
		Debug.enterMethod(this, "hitsTarget");
		/* Relevances
		 * 	the length of the item 0.2
		 * 	the performer agility 0.3
		 *  the size of the target 0.125
		 *  the DII of the target 0.25
		 *  the AII of the performer 0.125
		*/

		/* ITEM LENGTH
			A	Q
			0	0
			100	0,6
			200	0,9

			Eq: y = -2E-05x2 + 0,0075x - 2E-15
			*/
		int itemLength = weapon.getLength();
		Debug.say("itemLength "+itemLength);
		double itemLengthQ = MathF.poly(itemLength, new double[] {-0.00002, 0.0075, -0.000000000000002});

		/* PERFORMER AGILITY
			A	Q
			0	0
			50	0,6
			250	4


		    Eq: y = 2E-05x2 + 0,011x + 4E-15
		*/

		int agility = ((GBeing)performer).getQuickness();
		Debug.say("agility "+agility);
		double agilityQ = MathF.poly(agility, new double[] {0.00002, 0.011, 0});
		Debug.say("agilityQ "+agilityQ);

		/* TARGET SIZE
			A	Q
			0	0
			100	0,7
			200	3

            Eq: Q = y = 8E-05x2 - 0,001x - 2E-15
		*/

		int targetSize = targetBeing.getSize();
		Debug.say("targetSize "+targetSize);
		double targetSizeQ = MathF.poly(targetSize, new double[] {0.00008, -0.001, 0});
		Debug.say("targetSizeQ "+targetSizeQ);

		/* Relevances
		 * 	the length of the item 0.2
		 * 	the performer agility 0.3
		 *  the size of the target 0.125
		*/


		double hitQ = itemLengthQ * 0.2 + agilityQ * 0.3 + targetSizeQ * 0.125;
		Debug.say("hitQ "+hitQ);
		double chance = hitQ*100;
		Debug.say("chance "+chance);

		boolean result = MathF.rand(0, 100) < (int)(chance);
		Debug.exitMethod(result+"");
		return result;
	}

	private BodyPart getLandingBodyPart(GBeing targetBeing){
		Debug.enterMethod(this, "getLandingBodyPart");
		/** The bodyPart at which the performer aims depends on his
		 * fighting style. */
		BodyPart ret = targetBeing.getExposedBodyPart();
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
		Debug.exitMethod(where);
		return  where;
	}

	private int getImpactStrength(GItem aWeapon){
		Debug.enterMethod(this, "getImpactStrength");
		/** The strength of the impact is determined by the
		 * strength of the performer, (60%)
		 * and the weigth of the item. (40%)*/

		/* Performer Strength
		A	Q
		0	0
		65	50
		85	90

		Eq> y = 0,0145x2 - 0,1719x - 2E-13
		*/

        int strength = ((GBeing)performer).getFlexStr();
		Debug.say("Strength "+strength);
		int strQ = (int)MathF.poly(strength, new double[] {0.0145, -0.1719, 0});
		Debug.say("strQ "+strQ);

		/* Item weight

		A	Q
		0	0
		30	60
		70	90

		Eq: y = -0,0179x2 + 2,5357x + 2E-13
		*/

        int weigth = aWeapon.getWeight();
		Debug.say("weigth"+weigth);
		int weiQ =  (int) MathF.poly(strength, new double[] {-0.0179, 2.5357, 0});
		Debug.say("weigthQ"+weiQ);

		int ret = (int)(strQ * 0.6 + weiQ * 0.4);
		Debug.exitMethod(ret+"");
		return ret;
	}

}
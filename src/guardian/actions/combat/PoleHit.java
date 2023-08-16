package guardian.actions.combat;

import guardian.actions.*;

import guardian.being.*;
import guardian.item.*;
import engine.*;
import util.*;
import guardian.world.*;

public class PoleHit extends GuardianAction{
	private GBeing aBeingPerformer;
	private GItem aWeapon;

	public boolean needsBodyPart(){
		/** The bodyPart with which the performer will attack */
		return true;
	}

	public boolean needsNearBeing(){
		/** The being that will be attacked*/
		return true;
	}

	public String getPromptNearBeing(){
		return "Who do you want to swing your weapon at?";
	}

	public String getPromptBodyPart(){
		return "What item will you handle?";
	}

	public Action execute() throws EngineException{
		/** Uses the item handled in targetBodyPart to produce a pole impact
		 * into the enemy bodyparts or armor or both.
		 *
		 * The bodyparts on which the blow lands are determined by the
		 * instance of the target.
		 *
		 * The strength of the impact is determined by the strength
		 * of the performer and the weigth of the item.
		 *
		 * The hit chance is determined by the agility of the performer
		 * the weigth of the item and the length of the item.
		 *
		 * If the blow misses the target, it may impact the underlying
		 * MapCells or Items.
		 *
		 * The effect of this bash is a pole collition against an item or
		 * bodyPart.
		 *
		 */
		Debug.enterMethod(this, "execute");

		aBeingPerformer = (GBeing) performer;
		Point oldPosition = targetNearBeing.getPosition();
		Action x = targetNearBeing.getReflexAction("PoleHit");

		if (x!=null)
			targetNearBeing.performAction(x);

		aWeapon = targetBodyPart.getEquipedItem();
		if (aWeapon == null)
			aWeapon = targetBodyPart; // Hit with bare bodyPart

		int impactStrength = getImpactStrength();

		if (oldPosition.equals(targetNearBeing.getPosition())){ // If the target didn't move away
			if (hitsTarget()){ // The blow impacts into one of the targets bodyParts
				BodyPart landingBodyPart = getLandingBodyPart(); // What part of the body the blow lands into
				GItem landingArmor = getLandingArmor(landingBodyPart); // See if the blow is covered by the armor
				if (landingArmor == null)
					landingArmor = landingBodyPart; // Receive the blow with the body
				String msgYou = "You impact the "+ targetNearBeing.getDesc()+"'s "+landingBodyPart.getDescription()+" ("+impactStrength+")";
				String msgOther = aBeingPerformer.getDesc()+ " impacts the "+ targetNearBeing.getDesc()+"'s "+landingBodyPart.getDescription()+" ("+impactStrength+")";
				aBeingPerformer.getGWorld().addEvent(msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition());
				int residualStrength = landingArmor.receivePoleBlow(aWeapon, impactStrength, aBeingPerformer.getGWorld());
				if (residualStrength > 0 && landingArmor != landingBodyPart) {
					landingBodyPart.receivePoleBlow(aWeapon, residualStrength, aBeingPerformer.getGWorld());
				}
			} else { // The blow missed
				String msgYou = "You tried to slash at "+ targetNearBeing.getDesc()+".";
				String msgOther = aBeingPerformer.getDesc() + " tried to slash at "+ targetNearBeing.getDesc()+".";
				aBeingPerformer.getGWorld().addEvent(msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition());
			}
		} else { //The target moved by reflex
			String msgYou = targetNearBeing.getDesc() + " evades your blow!";
			String msgOther = targetNearBeing.getDesc() + " evades " + aBeingPerformer.getDesc() + " blow!";
			aBeingPerformer.getGWorld().addEvent(new Event(aBeingPerformer.getGWorld().informTime(), msgYou, msgOther, performer, this, null, aBeingPerformer.getPosition()));
			GWorld aWorld = aBeingPerformer.getGWorld();
			int residualStrength = impactStrength;
			GItem [] aItems = aWorld.getItemsAt(oldPosition);
			if (aItems.length > 0){
				int i = 0;
				while (i < aItems.length && residualStrength > 0){
					residualStrength = aItems[i].receivePoleBlow(aWeapon, residualStrength, aWorld);
					i++;
				}
			}
			if (residualStrength > 0){
            	GMapCell landingMapCell = aBeingPerformer.getGWorld().getMapCell(oldPosition);
            	/*String transformation = landingMapCell.getImpactTransformation(residualStrength);
            	if (transformation != null) {
					aWorld.transform(oldPosition, transformation);
				} */
			}
		}
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
		GItem weapon = targetBodyPart.getEquipedItem();
		if (weapon == null)
			weapon = targetBodyPart;
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

		int targetSize = targetNearBeing.getSize();
		Debug.say("targetSize "+targetSize);
		double targetSizeQ = MathF.poly(targetSize, new double[] {0.00008, -0.001, 0});
		Debug.say("targetSizeQ "+targetSizeQ);

		/* Relevances
		 * 	the length of the item 0.2
		 * 	the performer agility 0.3
		 *  the size of the target 0.125
		 *  the DII of the target 0.25
		 *  the AII of the performer 0.125
		*/


		double hitQ = itemLengthQ * 0.2 + agilityQ * 0.3 + targetSizeQ * 0.125;
		Debug.say("hitQ "+hitQ);
		double chance = hitQ*100;
		Debug.say("chance "+chance);

		boolean result = MathF.rand(0, 100) < (int)(chance);
		Debug.exitMethod(result+"");
		return result;
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

	private int getImpactStrength(){
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
/*
 * GItem.java
 *
 * Created on 2 de septiembre de 2004, 17:38
 */

package guardian.item;

import util.*;
import engine.*;
import guardian.actor.*;
import guardian.being.*;
import guardian.world.*;

public class GItem extends PhysicalActor implements ActorDefinition, Cloneable{
	/** An item is a object that has several properties, including
	 * the capacity of containing other items and of being equipped*/

	 /* Basic Actor Atributes */
	private String ID;
	private String aName;
	private Material material;

	private int weightCapacity,
		volumeCapacity,
		bodyPartCoverage /**Represents how much of the size is closed */,
		structStr /**Represent how structuralStrong, how hard it is to break it is*/,
		bladeSize,
		bladeSharpness,
		poleSize;

    private int missileModifier;

    private int weight,
	volume,
	integrity;

    private String [] allowedBodyParts;
	protected VItem items;
	private Property[] effects;

// Affectors- -------------------------
	public int receivePoleBlow(GItem impactant, int blowStrength, GWorld space){
		/** Receives a blow of certain strength by another item, causing
		 * transformations into both items and launching events to the world
		 *
		 * The effectors are two> The cutting result and the bludging result
		 *
		 * The cutting result depends on the sharpness of the impactant,
		 * The bludging results depends on the structStr of the impactant
		 *
		 * Effects on the impacted item
		 * <ul>
		 * <li> Reduction on bladeSharpness and bodyPartCoverage
		 * 		Determined the cutting result, countered by the cut tolerance
		 * <li> Reduction on the bladeSize and poleSize
		 * 		Determined by the cuttingresult, countered by the cutTolerance and structStr
		 * <li> Reduction in the integrity and cutTolerance
		 * 		determined by the bludging result countered by the structStr
		 * </ul>
		 *
		 * Effects on the impactant
		 * <ul>
		 * <li> Reduction on bladeSharpness and integrity
		 * 		Determined by the cutTolerance
		 * <ul>
		 *
		 * @param impactant The item that has potencial to impact
		 * @param blowStrength The strength with which the impactant was powered
		 * @param space The World in which the impact happens
		 * @return The remnant stregth of the impact
		 * */
		 int cuttingResult = (int)(impactant.getBladeSharpness() * blowStrength)/100;
		 int bludgingResult = (int)(impactant.getStructStr() * blowStrength)/100;
		 int remnant = blowStrength;

		 /* Effects on the impacted item */
		 if (cuttingResult < getCutResistance()) {
			 // The cut caues no harm, add a shrug off event
			 String msgOther = "The "+ impactant.getDescription() +" causes no damage into the " + this.getDescription()+"!!!";
			 space.addEvent("", msgOther, impactant, null, null, impactant.getPosition());
		 } else {
			 /* Reduction on bladeSharpness and bodyPartCoverage
			 * 		Determined the cutting result, countered by the cut tolerance */
			 int bladeSharpnessReduction = (int) ((cuttingResult * 0.2) - (getCutTolerance() * 0.1));
			 int bodyPartCoverageReduction = (int)( (cuttingResult * 0.15) - (getCutTolerance() * 0.1));

			 remnant -= bladeSharpness / 10;
			 setBladeSharpness(bladeSharpness - bladeSharpnessReduction);
			 remnant -= (int)(bladeSharpnessReduction / 2);
			 setBodyPartCoverage(bodyPartCoverage - bodyPartCoverageReduction);
			 remnant -= (int)(bodyPartCoverageReduction / 2);

			 /* Reduction on the bladeSize and poleSize
			 * 	Determined by the cuttingresult, countered by the cutTolerance and structStr */
			 int bladeSizeReduction  = (int)((cuttingResult * 0.1) - (getCutTolerance() * 0.1 + structStr * 0.1));
			 remnant -= (int)(bladeSizeReduction / 3);
			 Debug.say ("bladeSizeReduction (cR = " + cuttingResult + "cutTolerance = " + getCutTolerance() +" StructStr = " + structStr + " = " + bladeSizeReduction);
			 int poleSizeReduction  = (int) ((cuttingResult * 0.07) - (getCutTolerance() * 0.05 + structStr * 0.1));
			 remnant -= (int)(poleSizeReduction / 3);
			 Debug.say ("poleSizeReduction (cR = " + cuttingResult + "getCutTolerance() = " + getCutTolerance() +" StructStr = " + structStr + " = " + poleSizeReduction);
			 setBladeSize(bladeSize - bladeSizeReduction);
			 setPoleSize(poleSize - poleSizeReduction);
			 // Add events here resuming all the reductions
			 String msgOther = "The "+ impactant.getDescription() +" damages the " + this.getDescription()+"!!!";
			 System.out.println("The "+ impactant.getDescription() +" damages the " + this.getDescription()+"!!!");
			 System.out.println(((BodyPart)impactant).getOwner());
 			 System.out.println(((BodyPart)impactant).getPosition());
			 space.addEvent("", msgOther, impactant, null, null, impactant.getPosition());
		 }


		int integrityReduction = (int)((bludgingResult * 0.4) - (structStr * 0.3));
		remnant -= (int)(integrityReduction);
		setIntegrity(integrity - integrityReduction);
		// Add an event if the item is badly damaged or is about to be destroyed

		 // Effects on the impactant

		 return remnant;

	}



//Containering ----------------------------
    public void addItem(GItem i) throws EngineException{
	    /** Adds an item into this one */
	    Debug.enterMethod(this, "addItem", i);
	    if (i == this)
	    	Debug.doAssert(false, "the problem lies here");
		if (canContain(i)){
			items.add(i);
		} else {
			Debug.exitMethod("*Throws EngineException Can't carry the "+i+" inside the "+this);
			throw new EngineException("Can't carry item");
		}
		Debug.exitMethod();
	}

	public boolean canContain(GItem it){
		if (getCarriedVolume() + it.volume <= volumeCapacity){
			if (getCarriedWeight() + it.weight <= weightCapacity){
				return true;
			}
		}
		return false;
	}

	public boolean containsItem (GItem it){
		Debug.enterMethod(this, "containsItem", it);
		for (int i = 0; i<items.size(); i++){
			if (items.elementAt(i) == it || ((GItem)items.elementAt(i)).containsItem(it)){
				Debug.exitMethod(true+"");
                return true;
			}
		}
		Debug.exitMethod(false+"");
		return false;
	}

	public void removeItem(GItem it) throws EngineException{
		if (items.has(it))
			items.remove(it);
		else {
        	for (int i = 0; i<items.size(); i++){
				if (items.elementAt(i).containsItem(it)){
                    items.elementAt(i).removeItem(it);
                    return;
				}
			}
			throw new EngineException("Tried to remove an item not contained");
		}
	}

	public void destroy(){
		//Remove from world
	}

	public Actor deriveActor(){
		try{
			Actor x = (Actor)super.clone();
			if (getActionSelector() != null){
				x.setActionSelector(getActionSelector().clonate());
			}
			return x;
		} catch (CloneNotSupportedException cnse){
			Debug.doAssert(false, "Failed deriving a Item");
			return null;
		}

	}

	public boolean containsItems(){
		return !items.isEmpty();
	}


// --------- Smart Accesors

	public int getLength(){
		return poleSize + bladeSize;
	}

	public int getCutResistance(){
        /** the value that the item can resist with nothing
		 * happening to it by */
		 return (int)(structStr / 15);
	}
	public int getCutTolerance(){
		return (int)(structStr / 5);
	}

	public int getSwingIndex(){
		return (int)((weight + volume + integrity /10)/3);
	}

	public String getDescription(){
		//Debug.doAssert(material != null, "The item must have a material@GItem.java");
		//System.out.println(super.toString());
		return material.getName()+" "+aName;
	}

	public String toString(){
		return getDescription()+"("+super.toString()+")";
	}

	    	private int getCarriedVolume(){
		int acum = 0;
		for (int i=0; i<items.size(); i++){
			acum += items.elementAt(i).volume;
		}
		return acum;
	}

	private int getCarriedWeight(){
		int acum = 0;
		for (int i=0; i<items.size(); i++){
			acum += items.elementAt(i).weight;
		}
		return acum;
	}



	public void setPosition(Point where){
//		Debug.enterMethod(this, "setPosition", where);
		super.setPosition(where);
		if (items != null)
			for (int i=0; i<items.size(); i++){
				if (items.elementAt(i) != this)
				items.elementAt(i).setPosition(where);
			}
	//	Debug.exitMethod();
	}



	// ---------  Dumb Accesors
	public int getBladeSize() {
		return bladeSize;
	}

	public void setBladeSize(int value) {
		bladeSize = value;
	}

	public int getBladeSharpness() {
		return bladeSharpness;
	}

	public void setBladeSharpness(int value) {
		bladeSharpness = value;
	}

	public String getID() {
		return ID;
	}

	public int getBodyPartCoverage() {
		return bodyPartCoverage;
	}

	public void setBodyPartCoverage(int value) {
		bodyPartCoverage = value;
	}

	public int getStructStr() {
		return structStr;
	}

	public void setStructStr(int value) {
		structStr = value;
	}

	public String getName(){
		return aName;
	}
				  /*
	public String getItemType() {
		return aItemType;
	}               */
			   /*
	public void setItemType(String value) {
		aItemType = value;
	}                      */

	public int getWeightCapacity() {
		return weightCapacity;
	}

	public void setWeightCapacity(int value) {
		weightCapacity = value;
	}

	public int getVolumeCapacity() {
		return volumeCapacity;
	}

	public void setVolumeCapacity(int value) {
		volumeCapacity = value;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int value) {
		weight = value;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int value) {
		volume = value;
	}

	public String[] getAllowedBodyParts() {
		return allowedBodyParts;
	}

	public void setAllowedBodyParts(String[] value) {
		allowedBodyParts = value;
	}

	public VItem getItems() {
		return items;
	}

	public void setItems(VItem value) {
		items = value;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material value) {
		material =value;
	}

	public Property[] getEffects() {
		return effects;
	}

	public void setEffects(Property[] value) {
		effects = value;
	}

	public int getPoleSize(){
		return poleSize;
	}

	public void setPoleSize(int pPoleSize){
		poleSize = pPoleSize;
	}

    public GItem (String ID, String pName){
		this.ID = ID;
		aName = pName;
		items = new VItem();
	}


	public int getIntegrity() {
		return integrity;
	}

	public void setIntegrity(int what){
		integrity = what;
	}
}
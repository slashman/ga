package guardian.being;

import util.*;
import guardian.item.*;
import guardian.world.*;

public class BodyPart extends GItem implements Cloneable{
	private GBeing owner;
	private int location;
	private int tolerance; /** Tolerance to wounds; this numerical value
	                        * is compared with the reduction to integrity
	                        * from the wounds to have a precental damage
	                        * of the integrity */
	private double relevance;
	private VGWound wounds;

	public static int HI = 0, LOW = 1, MED = 3;

	public final static String
		HEAD = "HEAD",
		BACK = "BACK",
		TORSO = "TORSO",
		ARM = "ARM",
		HAND = "HAND",
		LEG = "LEG",
		FOOT = "FOOT",
		FINGER = "FINGER",
		WAIST = "WAIST",
		WING = "WING";

	public BodyPart(String ID, String name, int location, double relevance, GBeing owner, Material material, int tolerance){
		super(ID, name);
		setMaterial(material);
		this.location = location;
		this.relevance = relevance;
		this.tolerance = tolerance;
		wounds = new VGWound();
	}

	public void setOwner(GBeing who){
		owner = who;
	}

	public GBeing getOwner(){
		return owner;
	}

	public Point getPosition(){
		if (owner != null)
			return owner.getPosition();
		else
			return super.getPosition();
	}

	public void addItem(GItem i) throws EngineException{ /** Overriden Method, bodyPart can
	                                     				   * only contain 1 item for now */
		/*Debug.enterMethod(this, "addItem", i);
		equip(i);
		Debug.exitMethod();*/
	}
	/*public void equip(GItem i) throws EngineException{
		Debug.enterMethod(this, "equip", i);
		if (items.isEmpty()){
			items.add(i);
		} else {
			Debug.exitMethod("*Throws EngineException BodyPart already Equipped*");
			throw new EngineException("BodyPart already Equipped");
		}
		Debug.exitMethod();
	} */

	public boolean canContain(GItem i){ /** Overriden Method, bodyPart can
	                                     * only contain 1 item for now */
		Debug.enterMethod(this, "canContain", i);
		/*if (items.isEmpty()){
            Debug.exitMethod("true");
            return true;
		} else {*/
            Debug.exitMethod("false");
            return false;
		//}
	}

	public int getIntegrity(){
		/* Calculate the integrity (percental value) from all the wounds*/
		int ret = 100;
		for (int i =0; i< wounds.size(); i++){
			ret -= (wounds.elementAt(i).getIntegrityReduction()/tolerance);
		}
		return ret;
	}

	public int getRelativeDamage(){
		return (int)((100 - getIntegrity()) * relevance);
	}

	public void recoverWounds(){
		/* Call the recover for all the wounds, recalc integrity */
		for (int i = 0; i <wounds.size(); i++){
			wounds.elementAt(i).recover();
		}
	}

	public void addWound(GWound w){
		/*add the wound to the wounds list*/
		wounds.add(w);
	}

    public int receivePoleBlow(GItem impactant, int blowStrength, GWorld space){

		 int remnant = super.receivePoleBlow(impactant, blowStrength, space);
		 int cuttingResult = (int)(impactant.getBladeSharpness() * blowStrength);
		 System.out.println("Cutting"+ cuttingResult);
		 System.out.println("Openness"+ (int)( (cuttingResult * 0.15) - (getCutTolerance() * 0.1)));
		 GWound cut = new Cut(cuttingResult - getCutTolerance() , this);
		 this.addWound(cut);
		 //int integrityReduction = (int)((bludgingResult * 0.4) - (structStr * 0.3));
		 return remnant;

	}

	public GItem getEquipedItem(){
		if (items.isEmpty()){
			return null;
		} else {
			return items.elementAt(0);
		}
	}

	public GItem unequip() throws EngineException{
		GItem ret = getEquipedItem();
		if (ret == null)
			throw new EngineException("Trying to unequip an unequiped bp");
		removeItem(ret);
		return ret;
	}

	/*private void reduceIntegrity(int v){
		integrity -= v;
		if (integrity < 0) destroy();
	} */

	/*private void destroy(){

	} */
	public Object clone(){
		try {
			return super.clone();
		} catch (CloneNotSupportedException cnse){
			return null;
		}
	}

	public int getCutRecovery(){
		return owner.getCutRecovery();
	}

	public int getRequiredDemembrationGravity(){
		return 80;
	}
}
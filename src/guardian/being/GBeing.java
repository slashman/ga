package guardian.being;

import engine.*;

import util.*;
import java.util.*;

import guardian.item.*;
import guardian.world.*;
import guardian.actor.*;
import guardian.ui.*;


public class GBeing extends PhysicalActor implements SensibleActor, Cloneable{
    private String name;
	private VBodyPart bodyParts;
	private GItem corpse;

	private int mecStr,
				flexStr,
				quickness,
				speed,
				sight,
				memoryCapacity = 1,
				health,
				combatSkill;

	private int weight, size;
	private int naturalRegeneration;

	private GRace race;
	private GWorld aGWorld;
	private CellMemory aCellMemory;
	private TimeLine eventMemory;
	private TimeLine recentEventMemory;
	private GTime lastActionTime;
	private Perception currentPerception;

	private boolean fainted = false;
	private EquipmentSlot //Shortcuts
			armorSlot,
			weaponSlot,
			shieldSlot,
			backPackSlot;
	private EquipmentSlot[] slots;

//------ Overriden methods
	public void performAction(Action a) throws EngineException{
		lastActionTime = new GTime(aGWorld.getTime());
		super.performAction(a);
    }


//------- Smart Accessors
	public GItem getEquipedWeapon(){
		if (weaponSlot.getItem() != null)
			return weaponSlot.getItem();
		else
			return weaponSlot.getBodyPart();
	}

	public GItem getBackPack(){
		return backPackSlot.getItem();
	}

	public GItem getEquipedShield(){
		if (shieldSlot.getItem() != null)
			return shieldSlot.getItem();
		else
			return shieldSlot.getBodyPart();
	}

	public BodyPart getExposedBodyPart(){
     	return bodyParts.elementAt(MathF.rand(0, bodyParts.size()-1));
	}

	public boolean hasBodyPart(BodyPart what){
		return bodyParts.has(what);
	}

    public boolean canCarry(GItem what){
		Debug.enterMethod(this, "canCarry", what);
		/*System.out.println("The "+what.getDescription()+" weights "+what.getWeight());
		System.out.println("Carrying "+getCarriedWeight()+" Capacity "+getCarryCapacity());*/
		if (what.getWeight() + getCarriedWeight() <= getCarryCapacity()){
			Debug.exitMethod("true");
			return true;
		} else {
			Debug.exitMethod("false");
			return false;
		}
	}

//------- Mutator Methods
	public Perception recalcPerception(){
		Debug.enterMethod(this, "getPerception");
		/* Visual Perception */
		//Perception p = shadowCast();
		//if (currentPerception != null)
			//currentPerception.freePoints();

		currentPerception = square();
		//currentPerception = shadowCast();
        Debug.exitMethod();
		return currentPerception;
	}

	public void activateCellMemory(){
		aCellMemory = new CellMemory();
	}

	public void reduceHealth(int damage){
		health -= damage;
		if (health < 0) die();
	}

	public void eraseRecentMemory(){
		recentEventMemory = new TimeLine();
		//eventMemory = new TimeLine();
	}

	public void eventHappened(Event what){
		if (currentPerception.hasPoint(what.getPosition())){
			memorizeEvent(what);
		}
	}


	public void setCarryPart(BodyPart pCarry){
		backPackSlot.setBodyPart(pCarry);
	}

	public void setSlots ( EquipmentSlot armorSlot, EquipmentSlot weaponSlot, EquipmentSlot shieldSlot, EquipmentSlot backPackSlot){
        this.armorSlot = armorSlot;
        this.weaponSlot = weaponSlot;
        this.backPackSlot = backPackSlot;
        this.shieldSlot = shieldSlot;
   		slots = new EquipmentSlot[] {
            armorSlot,
			weaponSlot,
			shieldSlot,
			backPackSlot
		};

	};

	public void setPosition(Point where){
	 	super.setPosition(where);
	 	moveTo(where);
	}




	public int getCarriedWeight(){
		int w = 0;
		for (int i=0; i<slots.length; i++){
			if (slots[i].getItem() != null){
				w += slots[i].getItem().getWeight();
			}
		}
		//w += weight;
		return w;
	}

	public void moveTo(Point where){
		Debug.enterMethod(this, "moveTo", where);
		moveItems(where);
		for (int i = 0; i < bodyParts.size(); i++){
			bodyParts.elementAt(i).setPosition(where);
		}

		for (int i = 0; i < slots.length; i++){
			if (slots[i].getItem() != null)
				slots[i].getItem().setPosition(where);
		}

		Debug.exitMethod();
	}

	public void moveItems(Point where){
		Debug.enterMethod(this, "moveItems", where);
		/** Moves all the items the Being has equipped as he moves */
		for (int i=0; i<slots.length; i++){
			if (slots[i].getItem() != null)
				slots[i].getItem().setPosition(where);
		}
		Debug.exitMethod();
	}

	public void removeFromInventory (GItem what) throws EngineException{
		 Debug.enterMethod(this, "removeFromInventory", what);
		 for (int i=0; i<slots.length; i++){
			if (slots[i].getItem() != null){
				if (slots[i].getItem() == what){
					slots[i].setItem(null);
				} else {
					slots[i].getItem().removeItem(what);
				}
			}
		}
		Debug.exitMethod();

	}

	public void addToInventory (GItem what) throws EngineException{
		Debug.enterMethod(this, "addToInventory", what);
		if (canCarry(what) == false){
			Debug.exitMethod("*Throws EngineException*");
			throw new EngineException("Trying to add an Item "+what+" which can't be carried");
		}

		if (backPackSlot.getItem() != null)
    		backPackSlot.getItem().addItem(what);
    	else
		if (backPackSlot.getBodyPart() != null && what.getVolumeCapacity() > 0)
    		backPackSlot.setItem(what);
		else
		if (weaponSlot.getItem() == null)
			weaponSlot.setItem(what);
		else
		if (shieldSlot.getItem() == null)
			shieldSlot.setItem(what);
		else {
            Debug.exitMethod("*Throws EngineException*");
            throw new EngineException("Couldn't find a place to hold the item");
		}
		Debug.exitMethod();
	}

	public EquipmentSlot [] getEquipmentSlots(){
		return slots;
	}

	public int getCarryCapacity(){
		return mecStr * 50;
	}

	public String getDesc(){
		if (name == null){
			return "The " + getRace().getName();
		} else {
        	return name;
		}
	}

	public Object clone(){
		try {
		    Debug.enterMethod(this, "clone");
			GBeing b = (GBeing) super.clone();
			VBodyPart bps = new VBodyPart();
			EquipmentSlot[] newSlots = new EquipmentSlot[slots.length];
			for (int i=0; i<bodyParts.size(); i++){
				BodyPart toBeAdded = (BodyPart) bodyParts.elementAt(i).clone();
				toBeAdded.setOwner(b);
				bps.add(toBeAdded);
				for (int k = 0; k< slots.length; k++){
					if (bodyParts.elementAt(i) == slots[k].getBodyPart()){
					    newSlots[k] = new EquipmentSlot(slots[k].getName(), toBeAdded);
					    if (slots[k] == armorSlot)
					    	b.setArmorSlot(newSlots[k]);
   					    if (slots[k] == weaponSlot)
					    	b.setWeaponSlot(newSlots[k]);
   					    if (slots[k] == shieldSlot)
					    	b.setShieldSlot(newSlots[k]);
   					    if (slots[k] == backPackSlot)
					    	b.setBackPackSlot(newSlots[k]);

					}
				}
			}
			b.setBodyParts(bps);
			Debug.exitMethod();
			return b;
		} catch (CloneNotSupportedException e) {

			return null;
		}
	}


	public void setWorld(World w){
		super.setWorld(w);
		aGWorld = (GWorld) w;
	}

    public VBodyPart getBodyParts(){
		return bodyParts;
	}

	public void setRace(GRace value) {
		race = value;
	}

	public void setBodyParts(VBodyPart bodyParts){
		this.bodyParts = bodyParts;
		for (int i = 0; i <bodyParts.size(); i++){
			bodyParts.elementAt(i).setOwner(this);
		}
	}

	public void setMecStr(int value) {
		mecStr = value;
	}

	public void setFlexStr(int value) {
		flexStr = value;
	}

	public void setCorpse(GItem what){
		corpse = what;
	}

	public GItem getCorpse(){
		return corpse;
	}

	public void setQuickness(int value) {
		quickness = value;
	}

	public GWorld getGWorld(){
		return aGWorld;
	}

	public int getMecStr() {
		return mecStr;
	}

	public int getFlexStr() {
		return flexStr;
	}

	public int getQuickness() {
		return quickness;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int value) {
		speed = value;
	}

	public int getSight() {
		return sight;
	}

	public void setSight(int value) {
		sight = value;
	}

	public int getIntegrity() {
		int returns = 100;
		for (int i = 0; i < bodyParts.size(); i++){
			returns -= bodyParts.elementAt(i).getRelativeDamage() ;
		}
		//returns = (int)(returns / bodyParts.size());
		return returns;
	}

	public void recover(){
		for (int i = 0; i <bodyParts.size(); i++){
			bodyParts.elementAt(i).recoverWounds();
		}
	}

	public void checkStatus(){
		recover();
		if (fainted && getIntegrity() >= 20){
			fainted = false;
			String msgYou = "You get up and fight!";
			String msgOther = getDesc() + " gets up and fights!";
			getGWorld().addEvent(msgYou, msgOther, this, null, null, getPosition());
			getGWorld().reactivateActor(this);
		}
		if (getIntegrity() < 0)
			die();
		if (getIntegrity() < 20){
            String msgYou = "***You faint because of your wounds!";
			String msgOther = getDesc() + " faints because of its wounds!";
			getGWorld().addEvent(msgYou, msgOther, this, null, null, getPosition());
			fainted = true;
			getGWorld().disableActor(this);

		}
	}



	public GRace getRace(){
		return race;
	}

	public TimeLine getRecentEventMemory(){
		return recentEventMemory;
	}

	private boolean insideBounds(int x, int y){
		if (aGWorld != null)
         	return (x >= 0 && x < aGWorld.getWidth(position.z) && y >= 0 && y < aGWorld.getHeight(position.z));
        return false;
	}

	private void memorizeEvent(Event e){
		recentEventMemory.insert(e);
		eventMemory.insert(e);
		if (eventMemory.size() > memoryCapacity){
			eventMemory.removeElementAt(eventMemory.size()-1);
		}
	}

	private void memorizeEvents(VEvent e){
		for (int i = 0; i < e.size(); i++){
			memorizeEvent(e.elementAt(i));
		}
	}



	public String toString(){
		if (name == null)
			return "A "+race.getName()+" ("+super.toString()+")";
		else
			return name +" the " + race.getName() + " ("+super.toString()+")";
	}

//-------- Dumb Accesors
	public TimeLine getEventMemory() {
		return eventMemory;
	}

    public GTime getLastActionTime() {
		return lastActionTime;
	}

	public void setLastActionTime(GTime value) {
		lastActionTime = value;
	}

	public int getWeight() {
		Debug.enterMethod(this, "getWeight");
		Debug.exitMethod(weight+"");
		return weight;
	}

	public void setWeight(int value) {
		weight = value;
	}

	public void setWeaponPart(BodyPart value) {
		weaponSlot.setBodyPart(value);
	}

	public void setShieldPart(BodyPart value) {
		shieldSlot.setBodyPart(value);
	}

	public int getCutRecovery(){
		return naturalRegeneration;
	}

	public void demembrateBodyPart(BodyPart which){
		bodyParts.remove(which);
	}

	public CellMemory getCellMemory(){
		return aCellMemory;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		name = value;
	}

	public Action getReflexAction(String input){
		return getActionSelector().selectReflexAction(input);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int value) {
		size = value;
	}

	public Perception getPerception() {
		return currentPerception;
	}

//--- Constructors

	public GBeing(){
		super();
		currentPerception = new Perception();
		eventMemory = new TimeLine();
		recentEventMemory = new TimeLine();
		bodyParts = new VBodyPart();
		slots = new EquipmentSlot[0];
	}

//----- private Methods
	private Perception square(){
		Debug.enterMethod(this, "{private} square");
		//Perception p = new Perception();
		currentPerception.erase();
		Perception p = currentPerception;


		int SEMIRADIUS = getSight();
		PhysicalActor tActor = null;
		VEvent vEvents = null;
		//System.out.println("Time to compare " + lastActionTime);
		for (int ygo = position.y - SEMIRADIUS; ygo<=position.y+SEMIRADIUS; ygo++){
			for (int xgo = position.x - SEMIRADIUS ; xgo <= position.x + SEMIRADIUS; xgo++){
				if (insideBounds(xgo, ygo)){
					//p.perceive(aGWorld.getMapCell(position.z, xgo, ygo).getAppearance(), Point.newPoint(xgo - position.x, ygo - position.y));
					//p.perceive(aGWorld.getMapCell(position.z, xgo, ygo).getAppearance(), new Point(xgo - position.x, ygo - position.y));
//					System.out.println(aGWorld.getMapCell(position.z, xgo, ygo));
					p.perceive(aGWorld.getMapCell(position.z, xgo, ygo).getAppearance(), xgo - position.x, ygo - position.y);
					//if (aCellMemory != null)
					aCellMemory.addCell(xgo, ygo, position.z, aGWorld.getMapCell(position.z, xgo, ygo).getAppearance());
					tActor = aGWorld.getFirstActor(xgo, ygo, position.z);
					if (tActor != null)
						//p.perceive(tActor, tActor.getAppearance(), Point.newPoint(xgo - position.x, ygo - position.y));
						p.perceive(tActor, tActor.getAppearance(), new Point(xgo - position.x, ygo - position.y));
				}
			}
		}
		Debug.exitMethod(p);
		return p;
	}

	private void die(){
		if (corpse != null){
			corpse.setPosition(position);
			getGWorld().addPhysicalActor(corpse);
		}

		getGWorld().removePhysicalActor(this);
		//getWorld().removeActor(this);
	}



/// ShadowCast

	private Perception shadowCast(){
		int depth = getSight();
		Perception x = currentPerception;
		scan2(x, -1, 1, 0, depth, 1, 1);
		scan2(x, -1, 1, 0, depth, -1, 1);
		scan2(x, -1, 1, 0, depth, 1, -1);
		scan2(x, -1, 1, 0, depth, -1, -1);
		scan (x, -1, 1, 1, depth, 1, 1);
		scan (x, -1, 1, 1, depth, -1, 1);
		scan (x, -1, 1, 1, depth, 1, -1);
		scan (x, -1, 1, 1, depth, -1, -1);
		row(x, 1,1,depth);
		row(x, -1,1,depth);
		row(x, 1,-1,depth);
		row(x, -1,-1,depth);

		return x;
	}

	private void row(Perception p, int xvar, int yvar, int depth){
		PhysicalActor tActor = null;
		for (int i=0; i<depth; i++){ // For each mapCell in the row
			// Perceive the appearance of the MapCell
			Appearance app = aGWorld.getMapCell(position.z, position.x + xvar*i, position.y + yvar *i).getAppearance();
			p.perceive(app, i*xvar, i*yvar);
			aCellMemory.addCell(position.x + xvar*i, position.y + yvar *i, position.z, app);
			// Perceive the Actor on the MapCell
			tActor = aGWorld.getFirstActor(position.x + xvar * i, position.y + yvar * i, position.z);
			if (tActor != null)
				p.perceive(tActor, tActor.getAppearance(), new Point(i *xvar, i*yvar));
			// Stop perceiving if a blocker is found
			if (aGWorld.getMapCell(position.z, position.x + i * xvar, position.y + i*yvar).isOpaque())
				break;
		}
	}

	private void scan2 (Perception p, float startSlope, float endSlope, int startDepth, int maxLevel, int xmod, int ymod){
		//System.out.println("Start Slope " + startSlope);
		//System.out.println("End Slope " + endSlope);
		//System.out.println("Start Depth " + startDepth);
		PhysicalActor tActor = null;
		boolean exit = false;
		for (int ygo = startDepth; ygo < maxLevel ; ygo++){
			int start;
			if (startSlope == -1)
				start = 0;
			else
				start = (int)(ygo / startSlope);
			int end = (int)(ygo / endSlope);
			//System.out.println("Start " + start);
			//System.out.println("End " + end);
			for (int xgo = start ; xgo <  end; xgo++){
				if (insideBounds(xgo*xmod + position.x, ygo*ymod + position.y)){
					if (aGWorld.getMapCell(position.z, xgo * xmod + position.x , ygo * ymod + position.y).isOpaque()){
						if (xgo - 1 > 0)
							scan2(p, startSlope, ygo / (xgo - 1), ygo+1, maxLevel, xmod, ymod);

						while (aGWorld.getMapCell(position.z, xgo * xmod + position.x, ygo * ymod + position.y).isOpaque() && xgo < end){
							Appearance app = aGWorld.getMapCell(position.z, xgo * xmod + position.x, ygo * ymod + position.y).getAppearance();
							p.perceive(app, xgo * xmod, ygo * ymod);
							aCellMemory.addCell(position.x + xgo * xmod, position.y + ygo * ymod, position.z, app);
							tActor = aGWorld.getFirstActor(xgo * xmod + position.x, ygo * ymod + position.y, position.z);
							if (tActor != null){
								p.perceive(tActor, tActor.getAppearance(), new Point(xgo * xmod , ygo*ymod));
							}
							xgo ++;
						}
						if (xgo < end && xgo +1> 0)
							scan2(p, ygo/(xgo+1), endSlope, ygo, maxLevel, xmod, ymod);
						return;
					} else {
						Appearance app = aGWorld.getMapCell(position.z, xgo * xmod + position.x, ygo * ymod + position.y).getAppearance();
						p.perceive(app, xgo * xmod, ygo*ymod);
						aCellMemory.addCell(position.x + xgo * xmod, position.y + ygo * ymod, position.z, app);
						tActor = aGWorld.getFirstActor(xgo * xmod + position.x, ygo * ymod + position.y, position.z);
						if (tActor != null){
							p.perceive(tActor, tActor.getAppearance(), new Point(xgo * xmod, ygo * ymod));
						}
					}
				}
			}
		}
	}

    private void scan (Perception p, double startSlope, double endSlope, int startDepth, int maxLevel, int xmod, int ymod){
		//System.out.println("Start Slope " + startSlope);
		//System.out.println("End Slope " + endSlope);
		//System.out.println("Start Depth " + startDepth);
		PhysicalActor tActor = null;
		boolean exit = false;
		for (int xgo = startDepth; xgo < maxLevel ; xgo++){
			int start;
			if (startSlope == -1)
				start = 0;
			else
				start = (int)(xgo / startSlope);
			int end = (int)(xgo / endSlope);
			//System.out.println("Start " + start);
			//System.out.println("End " + end);
			for (int ygo = start ; ygo <  end; ygo++){
				if (insideBounds(xgo*xmod + position.x, ygo*ymod + position.y)){
					if (aGWorld.getMapCell(position.z, xgo * xmod + position.x , ygo * ymod + position.y).isOpaque()){
						if (ygo - 1 > 0)
							scan(p, startSlope, xgo / (ygo - 1), xgo+1, maxLevel, xmod, ymod);

						while (aGWorld.getMapCell(position.z, xgo * xmod + position.x, ygo * ymod + position.y).isOpaque() && ygo < end){
							p.perceive(aGWorld.getMapCell(position.z, xgo * xmod + position.x, ygo * ymod + position.y).getAppearance(), xgo * xmod, ygo * ymod);
							tActor = aGWorld.getFirstActor(xgo * xmod + position.x, ygo * ymod + position.y, position.z);
							if (tActor != null){
								p.perceive(tActor, tActor.getAppearance(), xgo * xmod , ygo*ymod);
							}
							ygo ++;
						}
						if (ygo < end && ygo +1> 0)
							scan(p, xgo/(ygo+1), endSlope, xgo, maxLevel, xmod, ymod);
						return;
					} else {
						p.perceive(aGWorld.getMapCell(position.z, xgo * xmod + position.x, ygo * ymod + position.y).getAppearance(), xgo * xmod, ygo*ymod);
						tActor = aGWorld.getFirstActor(xgo * xmod + position.x, ygo * ymod + position.y, position.z);
						if (tActor != null){
							p.perceive(tActor, tActor.getAppearance(), xgo * xmod, ygo * ymod);
						}
					}
				}
			}
		}
	}

	public void setArmorSlot(EquipmentSlot value) {
		armorSlot = value;
	}

	public void setWeaponSlot(EquipmentSlot value) {
		weaponSlot = value;
	}

	public void setShieldSlot(EquipmentSlot value) {
		shieldSlot = value;
	}

	public void setBackPackSlot(EquipmentSlot value) {
		backPackSlot = value;
	}

	public int getHealth() {
		return health;
	}

	public int getCombatSkill() {
		return combatSkill;
	}

	public void setCombatSkill(int value) {
		combatSkill = value;
	}
}
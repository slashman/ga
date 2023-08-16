package guardian.being;

import guardian.item.*;

public class EquipmentSlot {
	private BodyPart bp;
	private GItem equiped;
	private String name;

	public void setBodyPart(BodyPart what){
		bp = what;
	}

	public void setItem(GItem what){
		equiped = what;
	}

	public BodyPart getBodyPart(){
		return bp;
	}

	public GItem getItem(){
		return equiped;
	}

	public String getName(){
		return name;
	}

	public EquipmentSlot(String name, BodyPart what){
		this.name = name;
		setBodyPart(what);
	}

}
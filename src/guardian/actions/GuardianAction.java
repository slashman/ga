package guardian.actions;
import engine.*;

import guardian.being.*;
import guardian.world.*;
import guardian.item.*;
import util.*;


public abstract class GuardianAction extends Action{
	protected BodyPart targetBodyPart;
	protected EquipmentSlot targetEquipmentSlot;
	protected GBeing targetFarBeing,
					targetNearBeing;
	protected GWorld targetFarMapCellW,
					targetNearMapCellW;

	protected Point targetFarMapCellP,
					targetNearMapCellP;

	protected GItem targetInventoryItem,
				targetWorldItem;

	protected int targetDirection;

	public void setTargetBodyPart(BodyPart value) {
		targetBodyPart = value;
	}

	public void setTargetFarBeing(GBeing value) {
		targetFarBeing = value;
	}

	public void setEquipmentSlot(EquipmentSlot value) {
		targetEquipmentSlot = value;
	}

	public void setTargetNearBeing(GBeing value) {
		targetNearBeing = value;
	}

	public void setTargetFarMapCell(GWorld world, Point position) {
		targetFarMapCellW = world;
		targetFarMapCellP = position;
	}

	public void setTargetNearMapCell(GWorld world, Point position) {
		targetNearMapCellW = world;
		targetNearMapCellP = position;
	}

	public void setTargetInventoryItem(GItem value) {
		targetInventoryItem = value;
	}

	public void setTargetWorldItem(GItem value) {
		targetWorldItem = value;
	}

	public final static int
		UP = 0,
		DOWN = 1,
		LEFT = 2,
		RIGHT = 3,
		UPRIGHT = 4,
		UPLEFT = 5,
		DOWNRIGHT = 6,
		DOWNLEFT = 7;


	public static Point getVariation(int direction){
		Debug.enterMethod("Static DirectionAction", "getVariation", direction+"");
		switch (direction){
			case UP:
				Point p = new Point(0,-1);
				Debug.exitMethod(p);
				return p;
			case DOWN:
				p = new Point(0,1);
				Debug.exitMethod(p);
				return p;
			case LEFT:
				p = new Point(-1,0);
				Debug.exitMethod(p);
				return p;
			case RIGHT:
				p = new Point(1,0);
				Debug.exitMethod(p);
				return p;
			case UPRIGHT:
				p = new Point(1,-1);
				Debug.exitMethod(p);
				return p;
			case UPLEFT:
				p = new Point(-1,-1);
				Debug.exitMethod(p);
				return p;
			case DOWNRIGHT:
				p = new Point(1,1);
				Debug.exitMethod(p);
				return p;
			case DOWNLEFT:
				p = new Point(-1,1);
				Debug.exitMethod(p);
				return p;
			default:
				Debug.exitMethod(null);
				return null;
		}
	}

	protected Point getVariation(){
	    switch (targetDirection){
			case UP:
				return new Point(0,-1);
			case DOWN:
				return new Point(0,1);
			case LEFT:
				return new Point(-1,0);
			case RIGHT:
				return new Point(1,0);
			case UPRIGHT:
				return new Point(1,-1);
			case UPLEFT:
				return new Point(-1,-1);
			case DOWNRIGHT:
				return new Point(1,1);
			case DOWNLEFT:
				return new Point(-1,1);
			default:
				return null;
		}
	}

	public void setTargetDirection(int value) {
		if (value > 7 || value < 0)
			targetDirection = 0;
		else
			targetDirection = value;
	}

	public boolean needsBodyPart() {
		return false;
	}

	public boolean needsFarBeing(){
		return false;
	}

	public boolean needsFarMapCell(){
		return false;
	}

	public boolean needsInventoryItem(){
		return false;
	}

	public boolean needsNearBeing(){
		return false;
	}

	public boolean needsNearMapCell(){
		return false;
	}

	public boolean needsWorldItem(){
		return false;
	}

	public boolean needsDirection(){
		return false;
	}

	public boolean needsEquipmentSlot(){
		return false;
	}

	public String getPromptBodyPart() {
		return "";
	}

	public String getPromptEquipmentSlot(){
		return "";
	}

	public String getPromptFarBeing(){
		return "";
	}

	public String getPromptFarMapCell() {
		return "";
	}

	public String getPromptInventoryItem() {
		return "";
	}

	public String getPromptNearBeing() {
		return "";
	}

	public String getPromptNearMapCell() {
		return "";
	}

	public String getPromptWorldItem() {
		return "";
	}

	public String getPromptDirection() {
		return "";
	}
}
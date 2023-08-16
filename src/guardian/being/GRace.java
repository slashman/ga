package guardian.being;

import engine.*;
import util.*;

public class GRace implements ActorDefinition{
    /** Defines common characteristics for a group of Beings
	 * June 19/2004
	 */

	private String name;
    private GBeing protobeing;

    public GRace (String name, GBeing protobeing){
	    Debug.enterMethod(this, "{constructor}");
    	this.name = name;
    	this.protobeing = protobeing;
    	Debug.exitMethod();
    }

    public Actor deriveActor(){
		GBeing x = (GBeing) protobeing.clone();
		//Debug.say(x);
		//x.setAI(protobeing.getAI().clonate());
		return x;
	}

	public String getID(){
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		name = value;
	}

	public GBeing getProtobeing() {
		return protobeing;
	}

	public void setProtobeing(GBeing value) {
		protobeing = value;
	}
}
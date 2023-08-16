package guardian.actor;

import util.*;

public class VPhysicalActor extends ObjectVector{
	public PhysicalActor elementAt(int i){
		return (PhysicalActor) v.elementAt(i);
	}
}
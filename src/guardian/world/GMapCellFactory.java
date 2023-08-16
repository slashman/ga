package guardian.world;

import util.*;
import engine.*;


public class GMapCellFactory {
	private GMapCell[] definitions;

	public void init (GMapCell[] defs){
		Debug.enterMethod(this, "init");
		definitions = defs;
		Debug.exitMethod();
	}
	public GMapCell createMapCell(String desc) throws EngineException{
		int i = 0;
		try {
			//Debug.enterMethod(this, "createMapCell("+desc+")");
			for (i=0; i<definitions.length; i++){

				if (definitions[i].getID().equals(desc)){
					//Debug.exitMethod();
					return definitions[i];
				}
			}
			//Debug.exitMethod();
			throw new EngineException("MapCell "+desc+" type inexistant at MapCell Factory");
		} catch (NullPointerException npe){
			Debug.say("i"+i);
			Debug.say("definitions[i]"+definitions[i]);
			Debug.say("definitions[i].getID()"+definitions[i].getID());
			return null;
		}
	}
}
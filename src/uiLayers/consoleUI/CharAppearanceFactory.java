package uiLayers.consoleUI;

import engine.*;
import util.*;
import guardian.ui.*;


public class CharAppearanceFactory implements AppearanceFactory{
	Appearance [] aAppearanceDefs;

	public void init(Appearance[] pAppearanceDefs){
		Debug.enterMethod(this, "init");
		aAppearanceDefs = pAppearanceDefs;
		Debug.exitMethod();
	}

	public Appearance createAppearance(String desc) throws EngineException{
		Debug.enterMethod(this, "createAppearance", desc);
		for (int i=0; i<aAppearanceDefs.length; i++){
			if (aAppearanceDefs[i].getID().equals(desc)){
				Debug.exitMethod(aAppearanceDefs[i].duplicate());
				return aAppearanceDefs[i].duplicate();
			}
		}
		throw new EngineException("No appearance found for ID "+ desc);
	}
}
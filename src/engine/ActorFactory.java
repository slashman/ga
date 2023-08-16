package engine;

import util.*;

public class ActorFactory {
	private ActionSelector [] aSelectors;
	private ActorDefinition[] aDefinitions;
	
	public void setActorDefinitions(ActorDefinition[] pDefinitions){
		aDefinitions = pDefinitions;
	}

	public void init(ActionSelector[] pSelectors, ActorDefinition [] pDefinitions){
		/** Sets the Action Selectors and definitions that will be used */
		aSelectors = pSelectors;
		aDefinitions = pDefinitions;
	}

	private ActionSelector getSelector(String sID) throws EngineException{
		/** Returns the selector which ID matches sID
		 *  Throws an Exception if the selector is inexistant
		 */
		for (int i=0; i<aSelectors.length; i++){
			if (aSelectors[i].getID().equals(sID)){
				return aSelectors[i];
			}
		}
		throw new EngineException ("Inexistant Selector " + sID);
	}

	public Actor createActor(String pID) throws EngineException{
		/** Creates an Actor derived from a definition which ID
		 * equals pID
		 *  Throws an EngineException if there is no ActorDefinition
		 * with an ID matching pID
		 */
        for (int i=0; i<aDefinitions.length; i++){
			if (aDefinitions[i].getID().equals(pID)){
				return aDefinitions[i].deriveActor();
			}
		}
		throw new EngineException("ActorDefinition ID "+pID+ " not found.");
	}

	public Actor createActor(String pID, String pSelector) throws EngineException{
		/** Creates an Actor derived from a definition which ID
		 * equals pID, setting its ActionSelector to pSelector.
		 *
		 *  Throws an EngineException if there is no ActorDefinition
		 * with an ID matching pID
		 */
		Actor ret = createActor(pID);
		ret.setActionSelector(getSelector(pSelector));
		return ret;
	}

}
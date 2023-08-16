package guardian.being;

import engine.*;
import util.*;

public class GBeingFactory extends ActorFactory{

	public void init (GRace[] races){
		Debug.enterMethod(this, "init");
		setActorDefinitions(races);
		Debug.exitMethod();
	}

	public GBeing createBeing(String race) throws EngineException{
		return (GBeing) createActor(race);
	}

	public GBeing createBeing(guardian.worldgen.BeingProfile profile) throws EngineException {
		return createBeing(profile.getRaceID());
	}

	public GBeing createBeing(String race, String AI) throws EngineException{
		Debug.enterMethod(this, "createBeing", race+","+AI+")");
		Debug.exitMethod();
		return (GBeing) createActor(race, AI);
	}

}
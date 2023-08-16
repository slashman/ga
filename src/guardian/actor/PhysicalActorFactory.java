package guardian.actor;
import util.*;
import engine.*;
import guardian.ui.*;

public class PhysicalActorFactory extends ActorFactory{

	public PhysicalActor createActor (String pID, String pSelector, String appearance) throws EngineException{
		PhysicalActor vPhysicalActor = (PhysicalActor ) createActor(pID, pSelector);
		vPhysicalActor.setAppearance(aAppearanceFactory.createAppearance(appearance));
		return vPhysicalActor;
	}

	public PhysicalActor  createActor (String pID, String pSelector, String appearance, Point pPosition) throws EngineException{
		PhysicalActor vPhysicalActor = createActor(pID, pSelector, appearance);
		vPhysicalActor.setPosition(pPosition);
		return vPhysicalActor;
	}

	public void init (AppearanceFactory pAppearanceFactory){
		aAppearanceFactory = pAppearanceFactory;
	}

	private AppearanceFactory aAppearanceFactory;

}
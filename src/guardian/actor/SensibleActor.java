package guardian.actor;

import util.*;

public interface SensibleActor {
	/** Represents an Actor which can get perceptions from aroung him
	 */
	public Perception getPerception() throws EngineException;
	public Perception recalcPerception() throws EngineException;
}
package engine;

public interface ActorDefinition {
	public String getID();
	public Actor deriveActor();

}
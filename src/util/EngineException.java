package util;

public class EngineException extends java.lang.Exception {
	public EngineException (String desc){
		Debug.say("RogueDev EngineException: " + desc);
	}
}
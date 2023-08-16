package util;

public class ParameterException extends Exception {
    public ParameterException (String desc){
		Debug.say("RogueDev Parameter Exception: " + desc);
	}

}
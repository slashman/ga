package uiLayers.consoleUI;

import util.Debug;
import guardian.ui.*;
import engine.*;

public class CharAppearance implements Appearance {
	private char aChar;
	private Color aColor;
	private String aID;

	public CharAppearance(String pID, char pChar, Color pColor){
		//Debug.enterMethod(this, "{constructor}", pID);
		aID = pID;
		aChar = pChar;
		aColor = pColor;
		//Debug.exitMethod();
	}

	public Appearance duplicate (){
		//Debug.enterMethod(this, "duplicate");
		CharAppearance ret = new CharAppearance(aID, aChar, aColor);
		//Debug.exitMethod();
		return ret;
	}

	public String getID(){
		return aID;
	}

	public char getChar() {
		return aChar;
	}

	public Color getColor() {
		return aColor;
	}
}
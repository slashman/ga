package uiLayers.consoleUI;

import engine.*;
import guardian.actions.*;

public class UserAction {
	/* Links an Action with a KeyCode with which it is triggered*/
	private int keyCode;
	private GuardianAction action;


	public int getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(int value) {
		if (value<0 || value > 115)
			keyCode = 0;
		else
			keyCode = value;
	}

	public GuardianAction getAction() {
		return action;
	}

	public void setAction(GuardianAction value) {
		action = value;
	}

	public UserAction(GuardianAction action, int key){
		setKeyCode(key);
		setAction(action);
	}
}
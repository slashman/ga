package uiLayers.consoleUI;

public class UserCommand {
	/* Links a Command with a KeyCode with which it is triggered*/
	private int keyCode;
	private int command;

	public int getKeyCode() {
		return keyCode;
	}

	private void setKeyCode(int value) {
		if (value<0 || value > 115)
			keyCode = 0;
		else
			keyCode = value;
	}

	public int getCommand() {
		return command;
	}

	public UserCommand(int command, int keycode){
		this.command = command;
		setKeyCode(keycode);
	}
}
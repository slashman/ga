package guardian.ui;

public interface CommandListener {
	public final static int
		QUIT = 0,
		SAVE = 1,
		NONE = 2;
	public void commandSelected(int pCommand);
}
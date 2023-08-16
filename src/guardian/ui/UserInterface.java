package guardian.ui;

import engine.*;
import guardian.being.*;
import guardian.actor.*;
import guardian.actions.*;

public interface UserInterface {
	/** Must inform the player on the PC perception and memory,
	 * and also inform the game about the Actions the player wishes
	 * to make his PC perform, and the commands he wants to issue to the game */

	public void setCommandListener(CommandListener g);
    public void setPlayerPerception(Perception p);
    public void refresh(); /** Redraws the perception of the PC */

	public void setPlayer(GBeing b);
	public GBeing getPlayer();

	public GuardianAction selectAction();

	//public Perception getPlayerPerception();
	//public void addMessage(String s); // Not to be used from Actions, but in commands


	/** Draws the screen based on the current parameters
	 * This method must take the current Perception and show it,
	 * it must also take the PC information and the World Information
	 * As well as redraw the messages
	 *
	 * Recommended Methods:
	 * private void drawPlayerPerception();
	 * private void drawPlayerStatus();
	 * private void drawWorldStatus();
	 * private void drawMessages();
	 *
	 * */

	/* All user interfaces must implement the following two methods,
	 * as they need to inform the Game of a player Action.
	 * However, the way the game is informed is up to the designer
	 * of the UI, and thus they are not included in the interface.
	 * 	private void informPlayerAction(Action a);
	 *  private void informPlayerCommand(String command);
	 * */

//	public void drawExplotion (util.Point where, int size);


}
package guardian.game;

import guardian.ui.*;
import engine.*;
import util.*;

public class UISelector extends ActionSelector {
	private UserInterface aUI;

	public void setUI(UserInterface pUI){
		aUI = pUI;
	}

	public Action selectReflexAction(String input){
		return null;
	}

    public Action selectAction(){
		 /** This methods selects the Action the player wants to do
		 * It uses the UI object to do so
		 * Turn Time: Must wait for a player action petition different than null
		 * Real Time: Must return the current Action or a pass if it is null
		 */

		 Action a = null;
		 while (a == null){
			 a = aUI.selectAction();
			 if (canPerform(a))
			 	return a;
			 else
			 	a = null;
		 }
		return a;
	}

	private boolean canPerform(Action a){
		return true;
	}

}
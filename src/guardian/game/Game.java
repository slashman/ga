package guardian.game;

import engine.*;
import guardian.being.*;
import guardian.actor.*;
import guardian.ui.*;
import guardian.world.*;
import util.*;

public class Game implements CommandListener {
	/* Responsabilities:
	 * Coordinate the interaction between the player and the PC
	 * Forward the Action performance of the Actors */

	private UserInterface aUI;
	private GBeing aPC;
	private GWorld aWorld;

	public void init(UserInterface pUI){
		Debug.enterMethod(this, "init");
		aUI = pUI;
		aUI.setCommandListener(this);
		Debug.exitMethod();
	}

	public void setPC(GBeing pPC){
		aPC = pPC;
		aUI.setPlayer(pPC);
	}

	public void run(){
		Debug.enterMethod(this, "run");
		try{
			aUI.setPlayerPerception(aPC.recalcPerception());
			aUI.refresh();
			while (true){
				Actor next = aWorld.getNextActor();
				if (next instanceof SensibleActor) {
                    //Perception x = ((SensibleActor)next).recalcPerception();
                    if (next == aPC){
						//aUI.setPlayerPerception(x);
						aUI.setPlayerPerception(aPC.recalcPerception());
						aPC.eraseRecentMemory();
						aUI.refresh();
					}
				}
				if (next instanceof GBeing){
					((GBeing)next).eraseRecentMemory();
				}
				ActionSelector sel = next.getActionSelector();
				Action choice = sel.selectAction();
				next.performAction(choice);
			}
		} catch (EngineException ee){
			Debug.say(ee.getMessage());
			System.exit(0);
		}
		Debug.exitMethod();

	}

    public void commandSelected(int pCommand) {
	    Debug.enterMethod(this, "commandSelected", pCommand+"");
		if (pCommand == CommandListener.QUIT){
			quit();
		}

		Debug.exitMethod();
    }

    private void quit(){
		/*aUI.addMessage("Bye bye!");
		aUI.refresh();*/
		System.exit(0);
	}

	public UserInterface getUI(){
		return aUI;
	}

	public void setWorld(GWorld value) {
		aWorld = value;
	}
}
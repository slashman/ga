/*
 * Guardian.java
 *
 * Created on 2 de septiembre de 2004, 19:07
 */

package guardian.game;

import engine.*;
import guardian.being.*;
import guardian.world.*;

public class Guardian {

    public static void main(String [] args){
   	    switch (mainMenu()){
			case NEWGAME:
				FirstLoader loader = new FirstLoader();
				PCBuilder pcb = new PCBuilder();

				loader.load();

				GWorld w = loader.wb.buildWorld(512);
				loader.pb.generateTowns(w);

				pcb.setBeingFactory(loader.beingFactory);
				GBeing PC = pcb.buildPC(w);
				UISelector uis = new UISelector();
				uis.setUI(loader.ui);
				PC.setActionSelector(uis);
				//loader.ui.setPlayerActionListener(uis);
				loader.g.setWorld(w);
				loader.g.setPC(PC);
				loader.g.run();
		}

    }

    private static int mainMenu(){
		return NEWGAME;
	}

	public final static int NEWGAME = 0;



}
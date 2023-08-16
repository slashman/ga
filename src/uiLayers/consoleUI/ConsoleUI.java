package uiLayers.consoleUI;

import engine.*;
import java.util.*;
import guardian.ui.*;
import guardian.being.*;
import guardian.game.*;
import guardian.world.*;
import guardian.item.*;
import guardian.actions.*;
import guardian.actor.*;
import util.*;

public class ConsoleUI implements UserInterface{
	/** The console UI shows the information of the game
	 * with characters. It is also able to inform about the
	 * actions and command of the player received via the keyboard,
	 * It must be listening to a Console System Interface
	 * */

	private final static int EVENTMEMORY = 5;

	private final static int
				UPARROW = CharKey.N8,
				DOWNARROW = CharKey.N2,
				LEFTARROW = CharKey.N4,
				RIGHTARROW = CharKey.N6,
				UPLEFTARROW = CharKey.N7,
				UPRIGHTARROW = CharKey.N9,
				DOWNLEFTARROW = CharKey.N1,
				DOWNRIGHTARROW = CharKey.N3,
				SELF = CharKey.N5;
				/** These are the keycodes for the various arrows, which must be
				 * changeable via a property file*/

	private final static int
				ST_WAIT_ACTION = 0,	/** Waiting for the player to press a key related to an Action */
				ST_WAIT_PROMPT = 1,	/** Waiting for the player to press a direction or a accept command */
				ST_INITIALIZING = 2; /** In the starter screen or not still in game */

	private final static Point
				VP_START = new Point(2,2),
				VP_END = new Point (2+35,2+21),
				PC_POS = new Point (2+17,2+10);

	private final static int FADE = -50;
	private ConsoleSI si;

	private UserAction[] gameActions;
	private UserCommand[] gameCommands;

	private GuardianAction advance; /** Used for "Bump" if there is not a Being in the direction of the arrow*/
	private GuardianAction attack;  /** Used for "Bump" if there is a Being in the direction of the arrow*/

	private GBeing PC;

    private CommandListener aCl;

	private int timeCount;
	/**Used for continuous timing of arrow key pressings, every time the VM registers
	 * a KeyPressed Method, this increments to certain degree before launching a new
	 * Movement Action*/

	private Perception PCPerception; /** The perception of the PC, it must be update by the Game */

    public void setCommandListener(CommandListener pCl) {
		aCl = pCl;
    }

    public GuardianAction selectAction(){
	    CharKey input = null;
	    GuardianAction ret = null;
	    while (ret == null){
			input = si.inkey();
			/* Command Catching Start */
			int com = getRelatedCommand(input.code);
			if (com != CommandListener.NONE){
				informPlayerCommand(com);
			} else {
            	if (input.code == CharKey.J){
					MatrixShower m = new MatrixShower();
					m.setTitle("Map");
					m.setColoringMode(MatrixShower.GEO);
					m.setMatrix(((guardian.world.GWorld)PC.getWorld()).getIntMapCells());
					ret = null;
				}
				if (input.code == CharKey.A){
					showDebug();
				}
			}
			/* Command Catching End */

			if (isArrow(input.code)){
                switch (input.code){
					case UPARROW:
						advance.setTargetDirection(GuardianAction.UP);
						break;
					case DOWNARROW:
						advance.setTargetDirection(GuardianAction.DOWN);
						break;
					case LEFTARROW:
						advance.setTargetDirection(GuardianAction.LEFT);
						break;
					case RIGHTARROW:
						advance.setTargetDirection(GuardianAction.RIGHT);
						break;
					case UPRIGHTARROW:
						advance.setTargetDirection(GuardianAction.UPRIGHT);
						break;
					case DOWNRIGHTARROW:
						advance.setTargetDirection(GuardianAction.DOWNRIGHT);
						break;
					case UPLEFTARROW:
						advance.setTargetDirection(GuardianAction.UPLEFT);
						break;
					case DOWNLEFTARROW:
						advance.setTargetDirection(GuardianAction.DOWNLEFT);
						break;
				}
				return advance;
			} else {
            	ret = getRelatedAction(input.code);

            	try {
	            	if (ret != null)
		        		setTargets(ret);
				}
				catch (ActionCancelException ace){
					ret = null;
				}
				if (ret != null)
					return ret;
				refresh();
			}
		}
		return null;
	}

    // -- Output Methods

    public void refresh(){
	    si.cls();
	    //drawOutline();
	    drawPlayerMemory();
    	drawPlayerPerception();
	 	drawEventMemory();
	  	si.refresh();
    }

    private void drawEventMemory(){
	    /*
		String [] nearEvents = new String[EVENTMEMORY];
		for (int i=0; i<EVENTMEMORY ; i++){
			if (i >= PC.getEventMemory().size()) break;
			Event e = PC.getEventMemory().elementAt(i);
			if (e != null){
				if (e.getSource() == PC)
                	nearEvents[i] = e.getDescriptionYou();
                else
                	nearEvents[i] = e.getDescriptionOther();
			}
			else
				break;
		}
		int base = 30;
    	for (int i=EVENTMEMORY-1; i>=0; i--){
    		si.locate (5, base - i);
    		si.print("                                                                                ");
    		si.locate (5, base - i);
    		if (nearEvents[i] != null)
    			si.print(nearEvents[i]);
    	}
		*/
		String nearEvents = "";;
		TimeLine x = PC.getRecentEventMemory();
		//TimeLine x = PC.getEventMemory();
		for (int i=0; i<x.size(); i++){
			Event e = x.elementAt(i);
			if (e != null){
				if (e.getSource() == PC)
                	nearEvents += e.getDescriptionYou() + ";";
                else
                	nearEvents += e.getDescriptionOther() + ";";
			}
		}
		si.locate (5,30);
		si.print(nearEvents);
	}

    private void drawOutline(){
		for (int x = VP_START.x-1; x < VP_END.x +1; x++){
			si.locate(x, VP_START.y-1);
			si.print("*");
			si.locate(x, VP_END.y +1);
			si.print("*");
		}
		for (int y = VP_START.y-1; y< VP_END.y +2; y++){
			si.locate(VP_START.x-1, y);
			si.print("*");
			si.locate(VP_END.x +1,y);
			si.print("*");
		}
	}

    /*private void drawMessages(){
    	int base = 30;
    	for (int i=messages.length-1; i>=0; i--){
    		si.locate (5, base - i);
    		si.print("                                                                                ");
    		si.locate (5, base - i);
    		si.print(messages[i]);
    	}

    }*/


    private void drawPlayerPerception() {
	    Debug.enterMethod(this, "drawPlayerPerception");
	    //cleanViewPort();
//	    VAppearance appearances = PCPerception.getAppearances();
		int xstart = PC_POS.x - (int)(PCPerception.getWidth() / 2);
		int ystart = PC_POS.y - (int)(PCPerception.getHeight() / 2);
		for (int x = 0; x < PCPerception.getWidth(); x++){
			for (int y = 0; y < PCPerception.getHeight(); y++){
				si.locate(x + xstart, y + ystart);
				Appearance inThere = PCPerception.getAppearance(x,y);
				if (inThere != null)
					si.print(""+((CharAppearance)inThere).getChar(), ((CharAppearance)inThere).getColor());
			}
		}
/*		si.locate(1,1);
		si.print("Time: "+PC.getGWorld().getTime());
		si.locate(1,2);
		si.print("Position: "+PC.getPosition());*/
		VPhysicalActor perceivedActors = PCPerception.getPhysicalActors();
		for (int i=0; i<perceivedActors.size(); i++){
			si.locate(40,i+20);
			PhysicalActor x = perceivedActors.elementAt(i);
			if (x instanceof GBeing){
            	si.print(((GBeing)x).getDesc());
			} else
			if (x instanceof GItem){
				si.print(((GItem)x).getDescription());
			}
		}
		Debug.exitMethod();
    }

    private void drawPlayerMemory(){
		/** Draws the messages of events that happened or are happening
		 * Also draws memories of the player regarding other actors */
		/*for (int i = 0; i<messages.length; i++){
			si.locate(3, si.getYDim() - i);
			si.print(messages[i]);
		}
		si.refresh();*/
		int VPWIDTH = VP_END.x - VP_START.x;
		int VPHEIGHT = VP_END.y - VP_START.y;
		int xstart = PC.getPosition().x - (int) (VPWIDTH /2);
		int ystart = PC.getPosition().y - (int) (VPHEIGHT /2);
		for (int xgo = 0; xgo < VPWIDTH; xgo++)
			for (int ygo = 0; ygo < VPHEIGHT; ygo++){
				if (PC.getCellMemory().remembers(xgo+xstart, ygo+ystart, PC.getPosition().z)){
					si.locate(xgo + VP_START.x, ygo+VP_START.y);
					CharAppearance charap = (CharAppearance) PC.getCellMemory().remembered(xgo+xstart, ygo+ystart, PC.getPosition().z);

					si.print(charap.getChar() + "", charap.getColor().modify(FADE,FADE,FADE));
				}
			}

	}

	private void drawPlayerStatus(){
	    Debug.enterMethod(this, "drawPlayerStatus");
    	int base = 45;

    	si.locate(base,5);
		GBeing GPC = (GBeing) PC;
		si.print("Name:      " + GPC.getDesc());
		si.locate(base,9);
		si.print("Integrity:  "+ GPC.getIntegrity() +"/"+GPC.getHealth());
		si.locate(base,10);
		si.print("Cargando:    " + GPC.getCarriedWeight()+"/" +GPC.getCarryCapacity());
		si.locate(base,13);
		si.print("Fuerza Flex: "+ GPC.getFlexStr());
		si.locate(base,14);
		si.print("Fuerza Mec:  "+ GPC.getMecStr());
		si.locate(base,15);
		si.print("Reflejos:    "+ GPC.getQuickness());
		si.locate(base,16);
		si.print("Velocidad:   "+ GPC.getSpeed());
		si.locate(base,17);
		si.print("Vision:      "+ GPC.getSight());

  		Debug.exitMethod();
    }

    private void drawWorldStatus(){
	    Debug.enterMethod(this, "drawWorldStatus");
    	World w = PC.getWorld();
    	int xbase = 45, ybase = 15;
    	si.locate (xbase, ybase);
    	/*if (w instanceof RWorld){
    		si.print("World: "+((RWorld)w).getName());
    		si.locate(xbase, ybase + 1);
    		si.print(((RWorld)w).getTime());
    	} */
    	Debug.exitMethod();
    }


    private void cleanViewPort(){
	    Debug.enterMethod(this, "cleanViewPort");
    	String spaces = "";

    	for (int i= 0; i< VP_END.x - VP_START.x; i++){
    		spaces+=" ";
    	}

    	for (int i= VP_START.y; i<= VP_END.y; i++){
    		si.locate(VP_START.x, i);
    		si.print(spaces);
    	}
    	Debug.exitMethod();
	}

    private boolean insideViewPort(Point p){
    	/** Checks if the point, relative to the console coordinates, is inside the
    	 * ViewPort */
    	 return (p.x>=VP_START.x && p.x <= VP_END.x  && p.y >= VP_START.y && p.y <= VP_END.y );
    }

	// -- Input Methods

	private CharKey waitForArrow(){
		Debug.enterMethod(this, "waitForArrow");
		Debug.exitMethod();
		return new CharKey(UPARROW);
		/*int previousState = state;
		state = ST_WAIT_PROMPT;
		//while (!isArrow(inkeyBuffer.code));
		while (!isArrow(si.inkey().code));
		state = previousState;
		Debug.exitMethod();
		return inkeyBuffer;*/
	}


	private void showDebug(){
		//si.cls();
		si.locate(1,1);
		si.print("DEBUG");
		si.locate(1,2);
		si.print("World Time "+ this.PC.getGWorld().getTime());
	}

	//-- private Methods

	protected int charKeyToDirection(CharKey ck){
		switch (ck.code){
			case UPARROW:
				return GuardianAction.UP;
			case LEFTARROW:
				return GuardianAction.LEFT;
			case RIGHTARROW:
				return GuardianAction.RIGHT;
			case DOWNARROW:
				return GuardianAction.DOWN;
			case UPRIGHTARROW:
				return GuardianAction.UPRIGHT;
			case UPLEFTARROW:
				return GuardianAction.UPLEFT;
			case DOWNLEFTARROW:
				return GuardianAction.DOWNLEFT;
			case DOWNRIGHTARROW:
				return GuardianAction.DOWNRIGHT;
		}

		return -1;
	}

    private void informPlayerCommand(int command) {
	    Debug.enterMethod(this, "informPlayerCommand", command+"");
		aCl.commandSelected(command);
		Debug.exitMethod();
    }

	private CharAppearance getCharApp(Appearance a){
		return ((CharAppearance)a);
	}

    private GuardianAction getRelatedAction(int keyCode){
		for (int i=0; i<gameActions.length; i++){
			if (gameActions[i].getKeyCode() == keyCode){
				return gameActions[i].getAction();
			}
		}
		return null;
	}

	private int getRelatedCommand(int keyCode){
		for (int i=0; i<gameCommands.length; i++){
			if (gameCommands[i].getKeyCode() == keyCode){
				return gameCommands[i].getCommand();
			}
		}
		return CommandListener.NONE;
	}

	private int pickDirection(String prompt) throws ActionCancelException{
		Debug.enterMethod(this, "pickDirection");
		si.locate(1,1);
		si.print(prompt);
		si.refresh();

		CharKey x = new CharKey(CharKey.NONE);
		while (x.code == CharKey.NONE)
			x = si.inkey();
		if (isArrow(x.code)){
        	Debug.exitMethod(charKeyToDirection(x)+"");
        	return charKeyToDirection(x);
		} else {
			Debug.exitMethod("*Throws ActionCancelException*");
        	throw new ActionCancelException();
		}
	}

	private GItem pickWorldItem(String prompt) throws ActionCancelException{
		Debug.enterMethod(this, "pickWorldItem");
		// If the PC has some sort of telekinesis etc etc
		GItem[] worldItems = PC.getGWorld().getItemsAt(PC.getPosition());
		if (worldItems.length == 1) {
            Debug.exitMethod(worldItems[0]);
			return worldItems[0];
		}
		else{
			si.locate(20,1);
			si.print(prompt);

			for (int i=0; i<worldItems.length; i++){
				GItem temp = worldItems[i];
				si.locate(20,i+2);
				si.print((char) (65+i) +"-" + ((CharAppearance)temp.getAppearance()).getChar()+"." + temp.getDescription(), ((CharAppearance)temp.getAppearance()).getColor());
			}

		}
		si.refresh();
		CharKey x = si.inkey();
		if (x.code - 90 >= 0 && x.code - 90 <worldItems.length){
        	Debug.exitMethod(worldItems[x.code - 90]);
        	return worldItems[x.code - 90];
		} else {
			Debug.exitMethod("*Throws ActionCancelException*");
        	throw new ActionCancelException();
		}
	}

	private GItem pickInventoryItem(String prompt) throws ActionCancelException{
		Debug.enterMethod(this, "pickInventoryItem");
		/** Shows all items in the backpack, plus allows access to the
		 * body to drop equipped things
		 * Allows browsing containers */

        if (PC.getBackPack() != null && PC.getBackPack().getItems().size()>0){
	        GItem selected = browseContainer(PC.getBackPack(), prompt);
			Debug.exitMethod(selected);
            return selected;
        } else {

	        GItem selected = selectBodyPart(prompt).getEquipedItem();
			if (selected == null)
				throw new ActionCancelException();//"No Item equipped on BodyPart"
			Debug.exitMethod(selected);
            return selected;
		}
	}

	private EquipmentSlot pickEquipmentSlot(String prompt) throws ActionCancelException {
    	Debug.enterMethod(this, "pickEquipmentSlot");
		si.cls();
		si.locate(1,1);
		si.print(prompt);
		EquipmentSlot[] slots = PC.getEquipmentSlots();
        int yrun = 3;
        for (int i = 0; i < slots.length; i++, yrun++){
            si.locate (1, yrun);
            si.print( (char)(i+97) + "-" + slots[i].getName());
            si.locate (30, yrun);
            GItem equipped = slots[i].getItem();
            if (equipped != null )
                si.print( equipped.getDescription());
            else
                si.print( "Nothing" );
        }
        si.refresh();

        CharKey x = si.inkey();

		if (x.code - 90 >= 0 && x.code - 90 <slots.length){
			Debug.exitMethod(slots[x.code-90]);
			return slots[x.code-90];
		} else {
			Debug.exitMethod("*Throws ActionCancelException*");
        	throw new ActionCancelException();
		}
	}

	private BodyPart selectBodyPart(String prompt) throws ActionCancelException{
		Debug.enterMethod(this, "selectBodyPart");
		si.cls();
		si.locate(1,1);
		si.print(prompt);
		VBodyPart bp = PC.getBodyParts();
        int yrun = 3;
        for (int i = 0; i < bp.size(); i++, yrun++){
            si.locate (1, yrun);
            si.print( (char)(i+97) + "-" + bp.elementAt(i).getName());
            si.locate (30, yrun);
            GItem equipped = bp.elementAt(i).getEquipedItem();
            if (equipped != null )
                si.print( equipped.getDescription());
            else
                si.print( "Nothing" );
        }

        si.refresh();

        CharKey x = si.inkey();

		if (x.code - 90 >= 0 && x.code - 90 <bp.size()){
			Debug.exitMethod(bp.elementAt(x.code-90));
			return bp.elementAt(x.code-90);
		} else {
			Debug.exitMethod("*Throws ActionCancelException*");
        	throw new ActionCancelException();
		}
    }

	private GItem browseContainer(GItem it, String prompt) throws ActionCancelException{
		Debug.enterMethod(this, "browseContainer", it);
        si.cls();
        if (it != null && it.getItems().size() != 0){
            si.locate(1,2);
            //System.out.println("Container" + it);
            si.print(prompt);
            //si.print("Browsing Container: " + it.getDescription());
            VItem contents = it.getItems();
            //System.out.println("Contents " + contents);
            //System.out.println("Contents.size " + contents.size());
            for (int i = 0; i<contents.size(); i++){
                si.locate(1, i+4);
                //System.out.println("Contents.elementAt["+i+"]" + contents.elementAt(i));
                GItem itemI = contents.elementAt(i);
                si.print( (char)(i+97) + ")" + ((CharAppearance)itemI.getAppearance()).getChar() + " - " + itemI.getDescription());
                si.print( (char)(i+97) + ")");
                si.locate(3, i+4);
                si.print( ((CharAppearance)itemI.getAppearance()).getChar()+"",
                           ((CharAppearance)itemI.getAppearance()).getColor());
                si.locate(6,i+4);
                si.print(" - " + itemI.getDescription());
            }
            si.refresh();

            // Let the user pick a item inside the Container
			CharKey x = si.inkey();


			if (x.code - 90 >= 0 && x.code - 90 <contents.size()){

	        	GItem subItem = it.getItems().elementAt(x.code - 90);

                //if (subItem.getID().equals(GItem.CONTAINER)){
                if (subItem.containsItems()){
	                subItem = browseContainer(subItem, prompt);
					Debug.exitMethod(subItem);
                    //return browseContainer(subItem, prompt);
                    return subItem;
                } else {
	                Debug.exitMethod(subItem);
                    return subItem;
                }
			} else {
				Debug.exitMethod("*Throws ActionCancelException*");
	        	throw new ActionCancelException();
			}
        } else return null;
    }

	private GBeing pickNearBeing(String prompt) throws ActionCancelException{
		Debug.enterMethod(this, "pickNearBeing");
		si.locate(1,1);
		si.print(prompt);
		si.refresh();
		CharKey x = si.inkey();
		if (isArrow(x.code)){
			Point position = Point.add(
									PC.getPosition(),
									GuardianAction.getVariation(
										charKeyToDirection(x)
									));
			System.out.println("Being at "+position);
			GBeing target = PC.getGWorld().getBeingAt(position);
			System.out.println(target);

			if (target != null){
                Debug.exitMethod(target);
                return target;
			}

		}
		Debug.exitMethod("*Throws ActionCancelException*");
		throw new ActionCancelException();

	}

	private void pickNearMapCell(GuardianAction nma) throws ActionCancelException{
		Debug.enterMethod(this, "pickNearMapCell");
		// The PC may be able to use far mapcells as if they were near
		si.locate(1,1);
		si.print(nma.getPromptNearMapCell());
		si.refresh();

		CharKey x = si.inkey();
		if (isArrow(x.code)){
			nma.setTargetNearMapCell(PC.getGWorld(), Point.add(
									PC.getPosition(),
									GuardianAction.getVariation(
										charKeyToDirection(x)
									)
								));
		} else {
			Debug.exitMethod("*Throws ActionCancelException*");
        	throw new ActionCancelException();
		}
		Debug.exitMethod();
	}

    private boolean isArrow(int c){
	    Debug.enterMethod(this, "isArrow", c+"");
	    boolean ret = (c == RIGHTARROW || c == UPARROW || c == LEFTARROW || c == DOWNARROW || c == DOWNLEFTARROW || c == DOWNRIGHTARROW || c == UPLEFTARROW || c == UPRIGHTARROW);
	    Debug.exitMethod(ret + "");
		return ret;
	}

	private void setTargets(GuardianAction a) throws ActionCancelException{

		if (a.needsDirection()){
			a.setTargetDirection(pickDirection(a.getPromptDirection()));
		}
		if (a.needsNearBeing()){
			a.setTargetNearBeing(pickNearBeing(a.getPromptNearBeing()));
		}
		if (a.needsInventoryItem()){
			a.setTargetInventoryItem(pickInventoryItem(a.getPromptInventoryItem()));
		}
		if (a.needsBodyPart()){
			a.setTargetBodyPart(selectBodyPart(a.getPromptBodyPart()));
		}
		if (a.needsEquipmentSlot()){
			a.setEquipmentSlot(pickEquipmentSlot(a.getPromptEquipmentSlot()));
		}
		if (a.needsNearMapCell()){
			pickNearMapCell(a);
		}

		if (a.needsWorldItem()){
			a.setTargetWorldItem(pickWorldItem(a.getPromptWorldItem()));
		}
		//si.cls();
	}

	// -- Bean Methods

    public void setPlayerPerception(Perception p){
	    Debug.enterMethod(this, "setPlayerPerception", p);
		this.PCPerception = p;
		Debug.exitMethod();
	}

	public void setPlayer(GBeing b) {
		Debug.enterMethod(this, "setPlayer", b);
		PC = b;
		Debug.exitMethod();
    }

    public void init(ConsoleSI si, UserAction[] gameActions, UserCommand[] gameCommands, GuardianAction advance, GuardianAction attack) {
	    Debug.enterMethod(this, "init");
		this.advance = advance;
		this.attack = attack;
		this.gameActions = gameActions;
		this.gameCommands = gameCommands;
		this.si = si;

		Debug.exitMethod();
    }

    /*public void init(ConsoleSI si) {
	    Debug.say("Must send the gameActions");
	    System.exit(0);

    } */

	public GBeing getPlayer() {
		return PC;
	}

	public Perception getPlayerPerception() {
		return PCPerception;
	}
}
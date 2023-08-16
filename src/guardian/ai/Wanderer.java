package guardian.ai;

import engine.*;
import guardian.actions.*;
import util.*;

public class Wanderer extends ActionSelector{

	public Action selectReflexAction(String input){
		return null;
	}

    public Action selectAction() {
    	GuardianAction advance = null;
    	try {
    		advance = (GuardianAction) getAction("ADVANCE");
    	} catch (EngineException ee){
    		/* The performer can't Advance because its AI can-t handle it*/
    		Debug.doAssert(false, "Wanderer doesnt' knw how to advance");
    	}

        switch (MathF.rand(1,4)){
            case 1:
				advance.setTargetDirection(GuardianAction.UP);
                return advance;
            case 2:
				advance.setTargetDirection(GuardianAction.DOWN);
                return advance;
            case 3:
                advance.setTargetDirection(GuardianAction.LEFT);
                return advance;
            default:
				advance.setTargetDirection(GuardianAction.RIGHT);
                return advance;
        }
    }


}
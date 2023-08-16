package guardian.game;

import engine.*;
import util.*;
import guardian.being.*;
import guardian.world.*;

public class PCBuilder {
	private GBeingFactory gbf;

	public void setBeingFactory(GBeingFactory gbf){
		this.gbf = gbf;
	}

	public GBeing buildPC(GWorld w){
		try{
        	GBeing PC = gbf.createBeing("Angel");
        	PC.setPosition(200,200,0);
        	PC.setName("Guardian Angel 0.1 - Test");
        	w.addPhysicalActor(PC, true);
        	PC.activateCellMemory();
        	return PC;
		} catch (EngineException e){
			Debug.doAssert(false, "PCBuilder failed");
		}
		return null;

	}

}
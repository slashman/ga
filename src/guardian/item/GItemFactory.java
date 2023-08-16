package guardian.item;

import util.*;
import engine.*;

public class GItemFactory extends ActorFactory {

	private Material[] materials;

	public void init(GItem[] baseItems, Material[] materials){
		setActorDefinitions((ActorDefinition[])baseItems);
	    this.materials = materials;
	}

	public GItem createMaterialItem(String ID, String material) throws EngineException{
		Debug.enterMethod(this, "createMaterialItem", ID+" "+material);
		GItem ret = (GItem) createActor(ID);
		ret.setMaterial(getMaterial(material));
		Debug.exitMethod(ret);
		return ret;
	}

	public GItem createItem(guardian.worldgen.ContentDefinition definition) throws EngineException{
		Debug.enterMethod(this, "createItem", definition);
		GItem ret = createMaterialItem(definition.getItemID(), definition.getMaterial());
		Debug.exitMethod(ret);
		return ret;
	}

	public GItem createMaterialItem(String ID, String material, Property[] effects) throws EngineException{
		GItem ret = createMaterialItem(ID, material);
		ret.setEffects(effects);
		return ret;
	}

	public Material getMaterial(String what){
		for (int i=0; i<materials.length; i++){
			if (materials[i].getID().equals(what)){
				return materials[i];
			}
		}
		return null;
	}

}